package play.controller;

import org.springframework.web.bind.annotation.*;
import play.domain.UserEntity;
import play.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserEntity> findUsers() {
        return userService.findUsers();
    }

    @PostMapping("/users")
    public UserEntity addUser(@RequestBody Map<String, Object> requestBody) {
        return userService.saveStudent(requestBody);
    }

    @PutMapping("/users/{id}")
    public UserEntity updateStudent(@PathVariable(name = "id") Long id,
                                    @RequestBody Map<String, Object> requestBody) throws InterruptedException {
        return userService.updateUser(id, requestBody);
    }

    @PutMapping("/users/{id}/increase-balance")
    public UserEntity increaseBalance(@PathVariable(name = "id") Long id,
                                      @RequestBody Map<String, Integer> requestBody) {
        Integer amount = Optional.ofNullable(requestBody.get("amount"))
                .orElseThrow(() -> new IllegalArgumentException("Missing amount parameter"));

        return userService.increaseBalance(id, amount);
    }

}
