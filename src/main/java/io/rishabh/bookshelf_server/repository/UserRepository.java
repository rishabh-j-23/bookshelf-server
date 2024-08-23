package io.rishabh.bookshelf_server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.rishabh.bookshelf_server.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{
    public User findByUsername(String username);
}
