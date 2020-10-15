package play.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import play.domain.TaskCompleteRequestEntity;
import play.domain.TaskDataEntity;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface TaskCompleteRequestRepository extends JpaRepository<TaskCompleteRequestEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<TaskCompleteRequestEntity> findByTaskId(String taskId);
}
