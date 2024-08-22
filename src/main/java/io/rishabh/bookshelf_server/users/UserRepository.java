package io.rishabh.bookshelf_server.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID>{
    public User findByUsername(String username);
}
