package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user's review of a movie.
 * Reviews include a numeric rating, optional text content,
 * spoiler flag, and reaction tags.
 */
@Entity
@Table(
    name = "reviews",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"})
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull
    @DecimalMin("0.5")
    @DecimalMax("5.0")
    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "contains_spoiler", nullable = false)
    private boolean containsSpoiler = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "review_reaction_tags",
        joinColumns = @JoinColumn(name = "review_id")
    )
    @Column(name = "reaction_tag", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<ReactionTag> reactionTags = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Review() {}

    public Review(User user, Movie movie, BigDecimal rating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public boolean isContainsSpoiler() {
        return containsSpoiler;
    }

    /**
     * Returns an unmodifiable view of reaction tags.
     * Use addReactionTag/removeReactionTag/setReactionTags methods to modify.
     */
    public Set<ReactionTag> getReactionTags() {
        return Collections.unmodifiableSet(reactionTags);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContainsSpoiler(boolean containsSpoiler) {
        this.containsSpoiler = containsSpoiler;
    }

    // Reaction tag management
    public void addReactionTag(ReactionTag tag) {
        this.reactionTags.add(tag);
    }

    public void removeReactionTag(ReactionTag tag) {
        this.reactionTags.remove(tag);
    }

    public void setReactionTags(Set<ReactionTag> tags) {
        this.reactionTags = tags;
    }

    /**
     * Equality based on user+movie composite (business key), not database ID.
     * This aligns with the database unique constraint on (user_id, movie_id)
     * and ensures correct behavior in collections before and after persistence.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return Objects.equals(user, review.user) && Objects.equals(movie, review.movie);
    }

    /**
     * Hash code based on user+movie composite (business key) for consistency with equals().
     * Using stable business keys prevents issues when entities are added to
     * HashSets before persistence (when ID is null).
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, movie);
    }

    @Override
    public String toString() {
        return "Review{id=%d, userId=%d, movieId=%d, rating=%s}"
            .formatted(id, user != null ? user.getId() : null,
                      movie != null ? movie.getId() : null, rating);
    }
}
