package play.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import play.domain.TaskDataEntity;

public interface TaskDataRepository extends JpaRepository<TaskDataEntity, String> {


}
