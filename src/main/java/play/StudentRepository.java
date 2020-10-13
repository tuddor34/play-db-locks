package play;

import org.springframework.data.jpa.repository.JpaRepository;
import play.domain.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
