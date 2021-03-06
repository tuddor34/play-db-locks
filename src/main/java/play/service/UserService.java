package play.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.repository.UserRepository;
import play.domain.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class UserService {

    private Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static <T> T getOrElse(Map<String, Object> map, String key, boolean throwException) {

        T value = (T) map.get(key);
        if (value == null && throwException) {
            throw new IllegalArgumentException("Expected value for key " + key);
        }
        return value;
    }

    @Transactional
    public UserEntity saveStudent(Map<String, Object> userAttributes) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(getOrElse(userAttributes, "firstName", true));
        userEntity.setLastName(getOrElse(userAttributes, "lastName", true));
        userEntity.setBalance((int) userAttributes.getOrDefault("balance", 0));
        return userRepository.save(userEntity);
    }


    @Transactional
    public UserEntity updateUser(Long id, Map<String, Object> studentAttributes) {

        UserEntity userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found in db."));

        Optional.ofNullable((String) studentAttributes.get("firstName"))
                .ifPresent(userFromDb::setFirstName);

        Optional.ofNullable((String) studentAttributes.get("lastName"))
                .ifPresent(userFromDb::setLastName);


        Optional.ofNullable((Integer) studentAttributes.get("balance"))
                .ifPresent(userFromDb::setBalance);

        return userRepository.save(userFromDb);
    }


    @Transactional
    public List<UserEntity> findUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity increaseBalance(Long id, int amount, boolean slow) {
        UserEntity userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found in db."));


        int newBalance = userFromDb.getBalance() != null ? userFromDb.getBalance() + amount : amount;
        userFromDb.setBalance(newBalance);
        if (slow) {
            sleep(SECONDS, 15);
        }
        userRepository.save(userFromDb);

        log.info("Number of users is: {}", userRepository.count());

        return userFromDb;
    }


    private void sleep(TimeUnit timeUnit, long amount) {
        try {
            timeUnit.sleep(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
