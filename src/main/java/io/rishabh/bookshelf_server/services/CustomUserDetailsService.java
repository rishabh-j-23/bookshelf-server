package io.rishabh.bookshelf_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.rishabh.bookshelf_server.model.User;
import io.rishabh.bookshelf_server.repository.UserRepository;
import io.rishabh.bookshelf_server.security.auth.UserPrincipal;

import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CustomUserDetailsService implements  UserDetailsService {

    // constructor injection for user repo
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserPrincipal(user);

    }
 
    
}
