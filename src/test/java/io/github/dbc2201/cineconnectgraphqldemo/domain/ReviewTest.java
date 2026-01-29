package io.github.dbc2201.cineconnectgraphqldemo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the Review entity.
 */
@DisplayName("Review Entity")
class ReviewTest {

    private User testUser;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testUser = new User("reviewer", "reviewer@example.com", "hash");
        testMovie = new Movie("Test Movie");
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create review with user, movie, and rating")
        void shouldCreateReviewWithRequiredFields() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.5"));

            assertThat(review.getUser()).isEqualTo(testUser);
            assertThat(review.getMovie()).isEqualTo(testMovie);
            assertThat(review.getRating()).isEqualByComparingTo(new BigDecimal("4.5"));
        }

        @Test
        @DisplayName("should have empty reaction tags by default")
        void shouldHaveEmptyReactionTagsByDefault() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.0"));

            assertThat(review.getReactionTags()).isEmpty();
        }

        @Test
        @DisplayName("should not contain spoiler by default")
        void shouldNotContainSpoilerByDefault() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.0"));

            assertThat(review.isContainsSpoiler()).isFalse();
        }

        @Test
        @DisplayName("should have null content by default")
        void shouldHaveNullContentByDefault() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.0"));

            assertThat(review.getContent()).isNull();
        }
    }

    @Nested
    @DisplayName("Reaction Tags")
    class ReactionTags {

        @Test
        @DisplayName("should add reaction tag")
        void shouldAddReactionTag() {
            Review review = new Review(testUser, testMovie, new BigDecimal("5.0"));

            review.addReactionTag(ReactionTag.MIND_BENDING);

            assertThat(review.getReactionTags()).contains(ReactionTag.MIND_BENDING);
        }

        @Test
        @DisplayName("should add multiple reaction tags")
        void shouldAddMultipleReactionTags() {
            Review review = new Review(testUser, testMovie, new BigDecimal("5.0"));

            review.addReactionTag(ReactionTag.MIND_BENDING);
            review.addReactionTag(ReactionTag.PLOT_TWIST);
            review.addReactionTag(ReactionTag.REWATCHABLE);

            assertThat(review.getReactionTags())
                .hasSize(3)
                .contains(
                    ReactionTag.MIND_BENDING,
                    ReactionTag.PLOT_TWIST,
                    ReactionTag.REWATCHABLE
                );
        }

        @Test
        @DisplayName("should remove reaction tag")
        void shouldRemoveReactionTag() {
            Review review = new Review(testUser, testMovie, new BigDecimal("5.0"));
            review.addReactionTag(ReactionTag.MIND_BENDING);
            review.addReactionTag(ReactionTag.PLOT_TWIST);

            review.removeReactionTag(ReactionTag.MIND_BENDING);

            assertThat(review.getReactionTags())
                .hasSize(1)
                .contains(ReactionTag.PLOT_TWIST)
                .doesNotContain(ReactionTag.MIND_BENDING);
        }

        @Test
        @DisplayName("should set reaction tags")
        void shouldSetReactionTags() {
            Review review = new Review(testUser, testMovie, new BigDecimal("5.0"));
            review.addReactionTag(ReactionTag.MIND_BENDING);

            review.setReactionTags(Set.of(ReactionTag.COMFORT_WATCH, ReactionTag.REWATCHABLE));

            assertThat(review.getReactionTags())
                .hasSize(2)
                .contains(ReactionTag.COMFORT_WATCH, ReactionTag.REWATCHABLE)
                .doesNotContain(ReactionTag.MIND_BENDING);
        }
    }

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("should update rating")
        void shouldUpdateRating() {
            Review review = new Review(testUser, testMovie, new BigDecimal("3.0"));

            review.setRating(new BigDecimal("4.5"));

            assertThat(review.getRating()).isEqualByComparingTo(new BigDecimal("4.5"));
        }

        @Test
        @DisplayName("should update content")
        void shouldUpdateContent() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.0"));

            review.setContent("Great movie with amazing visuals!");

            assertThat(review.getContent()).isEqualTo("Great movie with amazing visuals!");
        }

        @Test
        @DisplayName("should update spoiler flag")
        void shouldUpdateSpoilerFlag() {
            Review review = new Review(testUser, testMovie, new BigDecimal("4.0"));

            review.setContainsSpoiler(true);

            assertThat(review.isContainsSpoiler()).isTrue();
        }
    }

    @Nested
    @DisplayName("ReactionTag Enum")
    class ReactionTagEnum {

        @Test
        @DisplayName("should have display name")
        void shouldHaveDisplayName() {
            assertThat(ReactionTag.MIND_BENDING.getDisplayName()).isEqualTo("Mind Bending");
            assertThat(ReactionTag.PLOT_TWIST.getDisplayName()).isEqualTo("Plot Twist!");
            assertThat(ReactionTag.UNDERRATED_GEM.getDisplayName()).isEqualTo("Underrated Gem");
        }

        @Test
        @DisplayName("should have description")
        void shouldHaveDescription() {
            assertThat(ReactionTag.MIND_BENDING.getDescription()).contains("thought-provoking");
            assertThat(ReactionTag.COMFORT_WATCH.getDescription()).contains("feel-good");
        }

        @Test
        @DisplayName("should have all expected tags")
        void shouldHaveAllExpectedTags() {
            assertThat(ReactionTag.values())
                .hasSize(12)
                .contains(
                    ReactionTag.MIND_BENDING,
                    ReactionTag.PLOT_TWIST,
                    ReactionTag.UNDERRATED_GEM,
                    ReactionTag.COMFORT_WATCH,
                    ReactionTag.REWATCHABLE,
                    ReactionTag.NOT_WORTH_HYPE,
                    ReactionTag.TEARJERKER,
                    ReactionTag.CROWD_PLEASER,
                    ReactionTag.SLOW_BURN,
                    ReactionTag.VISUAL_MASTERPIECE,
                    ReactionTag.GREAT_SOUNDTRACK,
                    ReactionTag.STRONG_PERFORMANCE
                );
        }
    }
}
