package ru.clevertec.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.auth.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
