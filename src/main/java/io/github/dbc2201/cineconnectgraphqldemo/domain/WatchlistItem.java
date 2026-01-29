package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a movie in a user's watchlist.
 * Users can save movies they want to watch later with optional notes.
 */
@Entity
@Table(
    name = "watchlist",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"})
)
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    protected WatchlistItem() {}

    public WatchlistItem(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
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

    public Instant getAddedAt() {
        return addedAt;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Equality based on user+movie composite (business key).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchlistItem that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, movie);
    }

    @Override
    public String toString() {
        return "WatchlistItem{userId=%d, movieId=%d}".formatted(
            user != null ? user.getId() : null,
            movie != null ? movie.getId() : null
        );
    }
}
