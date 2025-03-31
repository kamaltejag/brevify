package com.kamalteja.brevify.user.repository;

import com.kamalteja.brevify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
