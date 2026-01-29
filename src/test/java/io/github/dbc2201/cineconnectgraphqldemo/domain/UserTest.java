package io.github.dbc2201.cineconnectgraphqldemo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the User entity.
 */
@DisplayName("User Entity")
class UserTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create user with required fields")
        void shouldCreateUserWithRequiredFields() {
            User user = new User("testuser", "test@example.com", "hashedPassword123");

            assertThat(user.getUsername()).isEqualTo("testuser");
            assertThat(user.getEmail()).isEqualTo("test@example.com");
            assertThat(user.getPasswordHash()).isEqualTo("hashedPassword123");
        }

        @Test
        @DisplayName("should have null optional fields by default")
        void shouldHaveNullOptionalFieldsByDefault() {
            User user = new User("testuser", "test@example.com", "hash");

            assertThat(user.getDisplayName()).isNull();
            assertThat(user.getAvatarUrl()).isNull();
            assertThat(user.getBio()).isNull();
        }

        @Test
        @DisplayName("should have null id before persistence")
        void shouldHaveNullIdBeforePersistence() {
            User user = new User("testuser", "test@example.com", "hash");

            assertThat(user.getId()).isNull();
        }
    }

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("should update display name")
        void shouldUpdateDisplayName() {
            User user = new User("testuser", "test@example.com", "hash");

            user.setDisplayName("Test User");

            assertThat(user.getDisplayName()).isEqualTo("Test User");
        }

        @Test
        @DisplayName("should update avatar URL")
        void shouldUpdateAvatarUrl() {
            User user = new User("testuser", "test@example.com", "hash");

            user.setAvatarUrl("https://example.com/avatar.png");

            assertThat(user.getAvatarUrl()).isEqualTo("https://example.com/avatar.png");
        }

        @Test
        @DisplayName("should update bio")
        void shouldUpdateBio() {
            User user = new User("testuser", "test@example.com", "hash");

            user.setBio("Movie enthusiast");

            assertThat(user.getBio()).isEqualTo("Movie enthusiast");
        }

        @Test
        @DisplayName("should update password hash")
        void shouldUpdatePasswordHash() {
            User user = new User("testuser", "test@example.com", "oldHash");

            user.setPasswordHash("newHash");

            assertThat(user.getPasswordHash()).isEqualTo("newHash");
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal when ids match")
        void shouldBeEqualWhenIdsMatch() {
            // Note: In practice, IDs are set by JPA. This tests the equals logic.
            User user1 = new User("user1", "user1@example.com", "hash1");
            User user2 = new User("user2", "user2@example.com", "hash2");

            // Both have null IDs, so they should be equal by the equals() implementation
            assertThat(user1).isEqualTo(user2);
        }

        @Test
        @DisplayName("should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            User user = new User("testuser", "test@example.com", "hash");

            int hash1 = user.hashCode();
            int hash2 = user.hashCode();

            assertThat(hash1).isEqualTo(hash2);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("should include username and email")
        void shouldIncludeUsernameAndEmail() {
            User user = new User("testuser", "test@example.com", "hash");

            String result = user.toString();

            assertThat(result)
                .contains("testuser")
                .contains("test@example.com");
        }
    }
}
