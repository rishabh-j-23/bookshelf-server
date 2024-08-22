package io.rishabh.bookshelf_server.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user/signin")
    public Object addUser(@RequestBody User user) {
        // check if user already exists by username
        System.out.println(user.toString());
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return userRepository.findByUsername(user.getUsername());
        }

        if (user.getUsername() == null || user.getPassword() == null || user.getName() == null || user.getEmail() == null) {
            return new ResponseEntity<>("incomplete data", HttpStatus.BAD_REQUEST);
        }
        return userRepository.save(user);
    }

    @PostMapping("/user/login")
    public Object loginUser(@RequestBody User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (!userFromDb.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        }
        return userFromDb;
    }

}
