package play;

import org.springframework.data.jpa.repository.JpaRepository;
import play.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
   // List<UserEntity> findAll();
}
