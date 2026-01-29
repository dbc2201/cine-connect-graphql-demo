package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their exact username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their username (case-insensitive).
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Find a user by their email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a username is already taken (exact match).
     */
    boolean existsByUsername(String username);

    /**
     * Check if a username is already taken (case-insensitive).
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Check if an email is already registered.
     */
    boolean existsByEmail(String email);
}
