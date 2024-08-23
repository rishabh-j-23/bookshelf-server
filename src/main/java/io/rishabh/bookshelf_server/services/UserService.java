package io.rishabh.bookshelf_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.rishabh.bookshelf_server.model.User;
import io.rishabh.bookshelf_server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //strength = rounds
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public User register(User user) {
        user.setPassword(
            encoder.encode(user.getPassword())
        );
        return userRepository.save(user);
    }

    public User verifyUser(User user) {
        User verifyUser = userRepository.findByUsername(user.getUsername());
        if (verifyUser != null && encoder.matches(user.getPassword(), verifyUser.getPassword())) {
            return verifyUser;
        }
        return null;
    }
}

