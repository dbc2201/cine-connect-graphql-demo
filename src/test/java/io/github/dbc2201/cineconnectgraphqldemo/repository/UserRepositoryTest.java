package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for UserRepository.
 * Uses the test profile which connects to the local Docker PostgreSQL container.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("UserRepository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        savedUser = userRepository.save(new User("testuser", "test@example.com", "hashedPassword"));
    }

    @Nested
    @DisplayName("findByUsernameIgnoreCase")
    class FindByUsernameIgnoreCase {

        @Test
        @DisplayName("should find user by exact username")
        void shouldFindUserByExactUsername() {
            Optional<User> found = userRepository.findByUsernameIgnoreCase("testuser");

            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("should find user by username with different case")
        void shouldFindUserByUsernameDifferentCase() {
            Optional<User> found = userRepository.findByUsernameIgnoreCase("TESTUSER");

            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("should return empty for non-existent username")
        void shouldReturnEmptyForNonExistentUsername() {
            Optional<User> found = userRepository.findByUsernameIgnoreCase("nonexistent");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByEmail")
    class FindByEmail {

        @Test
        @DisplayName("should find user by email")
        void shouldFindUserByEmail() {
            Optional<User> found = userRepository.findByEmail("test@example.com");

            assertThat(found).isPresent();
            assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("should return empty for non-existent email")
        void shouldReturnEmptyForNonExistentEmail() {
            Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByUsernameIgnoreCase")
    class ExistsByUsernameIgnoreCase {

        @Test
        @DisplayName("should return true for existing username")
        void shouldReturnTrueForExistingUsername() {
            boolean exists = userRepository.existsByUsernameIgnoreCase("testuser");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("should return true for existing username with different case")
        void shouldReturnTrueForExistingUsernameDifferentCase() {
            boolean exists = userRepository.existsByUsernameIgnoreCase("TestUser");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("should return false for non-existent username")
        void shouldReturnFalseForNonExistentUsername() {
            boolean exists = userRepository.existsByUsernameIgnoreCase("nonexistent");

            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByEmail")
    class ExistsByEmail {

        @Test
        @DisplayName("should return true for existing email")
        void shouldReturnTrueForExistingEmail() {
            boolean exists = userRepository.existsByEmail("test@example.com");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("should return false for non-existent email")
        void shouldReturnFalseForNonExistentEmail() {
            boolean exists = userRepository.existsByEmail("nonexistent@example.com");

            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("CRUD Operations")
    class CrudOperations {

        @Test
        @DisplayName("should save user and generate id")
        void shouldSaveUserAndGenerateId() {
            User newUser = new User("newuser", "new@example.com", "hash");

            User saved = userRepository.save(newUser);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getUsername()).isEqualTo("newuser");
        }

        @Test
        @DisplayName("should update user")
        void shouldUpdateUser() {
            savedUser.setDisplayName("Test Display Name");
            savedUser.setBio("Updated bio");

            User updated = userRepository.save(savedUser);

            assertThat(updated.getDisplayName()).isEqualTo("Test Display Name");
            assertThat(updated.getBio()).isEqualTo("Updated bio");
        }

        @Test
        @DisplayName("should delete user")
        void shouldDeleteUser() {
            userRepository.delete(savedUser);

            Optional<User> found = userRepository.findById(savedUser.getId());
            assertThat(found).isEmpty();
        }
    }
}
