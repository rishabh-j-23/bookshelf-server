package io.rishabh.bookshelf_server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.rishabh.bookshelf_server.security.auth.roles.Role;
import io.rishabh.bookshelf_server.security.auth.roles.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(UserRole name);
}
