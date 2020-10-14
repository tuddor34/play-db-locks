package play.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.UserRepository;
import play.domain.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

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
    public UserEntity increaseBalance(Long id, int amount) {
        UserEntity userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found in db."));


        int newBalance = userFromDb.getBalance() != null ? userFromDb.getBalance() + amount : amount;
        userFromDb.setBalance(newBalance);

        return userRepository.save(userFromDb);
    }

}
