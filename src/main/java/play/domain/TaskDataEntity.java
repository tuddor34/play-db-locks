package play.domain;

import javax.persistence.*;

@Entity
@Table(schema = "domain", name = "task_data")
public class TaskDataEntity {

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "user_input")
    private String userInput;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus taskStatus;

    @Column(name = "open_timestamp")
    private Long openTimestamp;
    @Column(name = "finish_timestamp")
    private Long finishTimestamp;

    @ManyToOne
    @JoinColumn(name = "completed_by")
    private UserEntity completedBy;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getOpenTimestamp() {
        return openTimestamp;
    }

    public void setOpenTimestamp(Long openTimestamp) {
        this.openTimestamp = openTimestamp;
    }

    public Long getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(Long finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public UserEntity getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(UserEntity completedBy) {
        this.completedBy = completedBy;
    }
}
