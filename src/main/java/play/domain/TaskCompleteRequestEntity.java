package play.domain;

import javax.persistence.*;

@Entity
@Table(schema = "domain", name = "task_complete_request")
public class TaskCompleteRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskRequestStatus taskRequestStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "time_stamp_ms")
    private Long timestamp;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskRequestStatus getTaskRequestStatus() {
        return taskRequestStatus;
    }

    public void setTaskRequestStatus(TaskRequestStatus taskRequestStatus) {
        this.taskRequestStatus = taskRequestStatus;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
