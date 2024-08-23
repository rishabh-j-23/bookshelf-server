package io.rishabh.bookshelf_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.rishabh.bookshelf_server.model.User;
import io.rishabh.bookshelf_server.repository.UserRepository;
import io.rishabh.bookshelf_server.services.jwt.JwtService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    //strength = rounds
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    public User register(User user) {
        user.setPassword(
            encoder.encode(user.getPassword())
        );
        return userRepository.save(user);
    }

    public String verifyUser(User user) {
        Authentication authentication  = authenticationManager
            .authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(), 
                    user.getPassword()
                )
            );

        if (authentication.isAuthenticated()){ 
            return jwtService.generateToken(user.getUsername());
        }
        return null;
    }
}

