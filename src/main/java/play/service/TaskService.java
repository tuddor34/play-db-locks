package play.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.domain.*;
import play.exception.AlreadyRunningTaskRequestException;
import play.repository.TaskCompleteRequestRepository;
import play.repository.TaskDataRepository;
import play.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private static final String DEFAULT_TASK_NAME = "user_task_";
    private static final String NOT_ALLOWED_TERM = "idiot";
    private final TaskDataRepository taskDataRepository;
    private final TaskCompleteRequestRepository taskCompleteRequestRepository;
    private final UserRepository userRepository;
    private Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskDataRepository taskDataRepository, TaskCompleteRequestRepository taskCompleteRequestRepository, UserRepository userRepository) {
        this.taskDataRepository = taskDataRepository;
        this.taskCompleteRequestRepository = taskCompleteRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskDataEntity createTask() {
        TaskDataEntity taskDataEntity = new TaskDataEntity();

        Instant now = Instant.now();
        taskDataEntity.setTaskId(DEFAULT_TASK_NAME + Instant.now().getEpochSecond());
        taskDataEntity.setTaskStatus(TaskStatus.OPEN);
        taskDataEntity.setOpenTimestamp(now.toEpochMilli());

        return taskDataRepository.save(taskDataEntity);
    }

    @Transactional
    public List<TaskDataEntity> getTasks() {
        return taskDataRepository.findAll();
    }

    @Transactional
    public TaskDataEntity completeTask(Long userId, String taskId, String userInput) {
        TaskDataEntity task = getTask(taskId);
        UserEntity user = getUser(userId);

        sleep(TimeUnit.SECONDS, 7);

        if (userInput.contains(NOT_ALLOWED_TERM)) {
            throw new IllegalArgumentException("Can't complete task with forbidden input");
        }

        task.setUserInput(userInput);
        task.setFinishTimestamp(Instant.now().toEpochMilli());
        task.setCompletedBy(user);
        task.setTaskStatus(TaskStatus.FINISHED);

        return taskDataRepository.save(task);
    }

    @Transactional
    @Retryable(
            backoff = @Backoff(1000L),
            maxAttempts = 4,
            include = {AlreadyRunningTaskRequestException.class},
            listeners = "retryListener"
    )
    public TaskCompleteRequestEntity lockTaskForRequest(Long userId,
                                                        String taskId) {
        log.info("Lock task {} by user {}", taskId, userId);

        UserEntity user = getUser(userId);
        getTask(taskId);

        sleep(TimeUnit.SECONDS, 7);

        Optional<TaskCompleteRequestEntity> optionalTaskRequest = taskCompleteRequestRepository.findByTaskId(taskId);
        if (optionalTaskRequest.isPresent()) {
            TaskCompleteRequestEntity existingRequest = optionalTaskRequest.get();
            switch (existingRequest.getTaskRequestStatus()) {
                case COMPLETED:
                    throw new IllegalArgumentException("Task with id " + taskId + " was already completed by " + userId);
                case RUNNING:
                    return handleRunningTaskRequestEntity(existingRequest, user);
                case FAILED:
                    return saveTaskRequestEntity(existingRequest, user);
                default:
                    throw new IllegalStateException("Unkonwn value of request " + existingRequest.getId());
            }
        } else {
            TaskCompleteRequestEntity newTaskCompleteRequest = new TaskCompleteRequestEntity();
            newTaskCompleteRequest.setTaskId(taskId);
            return saveTaskRequestEntity(newTaskCompleteRequest, user);
        }
    }

    private TaskCompleteRequestEntity handleRunningTaskRequestEntity(TaskCompleteRequestEntity runningRequest, UserEntity user) {

        // check if existing task  is already running for more than 2 minutes
        if (Instant.now().toEpochMilli() - runningRequest.getTimestamp() > 2 * 60 * 1000) {
            return saveTaskRequestEntity(runningRequest, user);
        }
        throw new AlreadyRunningTaskRequestException("Task completion with id " + runningRequest.getTaskId() + " is already processing");

    }

    @Transactional
    public void unlockTaskForRequest(TaskCompleteRequestEntity taskCompleteRequestEntity, boolean taskCompleted) {
        taskCompleteRequestEntity.setTaskRequestStatus(taskCompleted ? TaskRequestStatus.COMPLETED : TaskRequestStatus.FAILED);
        log.info("Task request for user {}, is {}", taskCompleteRequestEntity.getUser().getId(), taskCompleteRequestEntity.getTaskRequestStatus());
        taskCompleteRequestRepository.save(taskCompleteRequestEntity);
    }


    private TaskCompleteRequestEntity saveTaskRequestEntity(TaskCompleteRequestEntity taskRequest,
                                                            UserEntity user) {
        taskRequest.setTaskRequestStatus(TaskRequestStatus.RUNNING);
        taskRequest.setUser(user);
        taskRequest.setTimestamp(Instant.now().toEpochMilli());
        return taskCompleteRequestRepository.save(taskRequest);
    }

    private UserEntity getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found in db"));
    }

    private TaskDataEntity getTask(String id) {
        return taskDataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found in db"));
    }

    private void sleep(TimeUnit timeUnit, long amount) {
        try {
            timeUnit.sleep(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
