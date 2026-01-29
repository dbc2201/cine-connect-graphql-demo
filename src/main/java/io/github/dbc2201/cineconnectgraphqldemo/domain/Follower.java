package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a follower relationship between two users.
 * This is an asymmetric relationship: if A follows B, B doesn't automatically follow A.
 */
@Entity
@Table(
    name = "followers",
    uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who is following (the follower).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    /**
     * The user being followed (the one gaining a follower).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Follower() {}

    public Follower(User follower, User following) {
        if (follower.equals(following)) {
            throw new IllegalArgumentException("A user cannot follow themselves");
        }
        this.follower = follower;
        this.following = following;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowing() {
        return following;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Equality based on follower+following composite (business key).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follower that)) return false;
        return Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }

    @Override
    public String toString() {
        return "Follower{followerId=%d, followingId=%d}".formatted(
            follower != null ? follower.getId() : null,
            following != null ? following.getId() : null
        );
    }
}
