package io.rishabh.bookshelf_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.rishabh.bookshelf_server.model.User;
import io.rishabh.bookshelf_server.repository.UserRepository;
import io.rishabh.bookshelf_server.services.UserService;

import java.util.List;

@RestController
public class UserController {

    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user/register")
    public Object addUser(@RequestBody User user) {
        // check if user already exists by username
        if (userRepository.findByUsername(user.getUsername()) != null) {
            user = userRepository.findByUsername(user.getUsername());
            user.setPassword(null);
            return user;
        }

        if (user.getUsername() == null || user.getPassword() == null || user.getName() == null || user.getEmail() == null) {
            return new ResponseEntity<>("incomplete data", HttpStatus.BAD_REQUEST);
        }
        User savedUser = userService.register(user);
        savedUser.setPassword(null);
        return savedUser;
    }

    @PostMapping("/user/login")
    public Object loginUser(@RequestBody User user) {
        User verifiedUser = userService.verifyUser(user);
        if (verifiedUser == null) {
            return new ResponseEntity<>("invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        verifiedUser.setPassword(null);
        return verifiedUser;
    }
}
