package play.controller;

import org.springframework.web.bind.annotation.*;
import play.domain.TaskCompleteRequestEntity;
import play.domain.TaskDataEntity;
import play.service.TaskService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public TaskDataEntity createTask() {
        return taskService.createTask();
    }

    @GetMapping("/tasks")
    public List<TaskDataEntity> getTasks() {
        return taskService.getTasks();
    }

    @PutMapping("/tasks")
    public TaskDataEntity completeTask(@RequestBody Map<String, String> requestBody) {

        String userId = Optional.of(requestBody.get("user_id"))
                .orElseThrow(() -> new IllegalArgumentException("user_id is missing from request body"));

        String taskId = Optional.of(requestBody.get("task_id"))
                .orElseThrow(() -> new IllegalArgumentException("task_id is missing from request body"));

        String userInput = Optional.of(requestBody.get("user_input"))
                .orElseThrow(() -> new IllegalArgumentException("user_input is missing from request body"));

        TaskCompleteRequestEntity taskCompleteRequestEntity = taskService.lockTaskForRequest(Long.valueOf(userId), taskId);
        try {
            TaskDataEntity taskDataEntity = taskService.completeTask(Long.valueOf(userId), taskId, userInput);
            taskService.unlockTaskForRequest(taskCompleteRequestEntity, true);
            return taskDataEntity;
        } catch (Exception ex) {
            taskService.unlockTaskForRequest(taskCompleteRequestEntity, false);
            throw ex;
        }


    }


}
