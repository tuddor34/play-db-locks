package play;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import play.domain.UserEntity;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<UserEntity> findById(Long Id);
}
