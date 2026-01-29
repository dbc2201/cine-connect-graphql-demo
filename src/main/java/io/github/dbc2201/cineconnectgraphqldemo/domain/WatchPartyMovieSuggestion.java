package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * Movie suggestion for a watch party.
 * Participants can suggest movies and vote on them before the party starts.
 */
@Entity
@Table(name = "watch_party_movie_suggestions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"party_id", "movie_id"}))
public class WatchPartyMovieSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private WatchParty party;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suggested_by_id", nullable = false)
    private User suggestedBy;

    @Column(name = "vote_count", nullable = false)
    private int voteCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WatchParty getParty() {
        return party;
    }

    public void setParty(WatchParty party) {
        this.party = party;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getSuggestedBy() {
        return suggestedBy;
    }

    public void setSuggestedBy(User suggestedBy) {
        this.suggestedBy = suggestedBy;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Helper methods
    public void incrementVoteCount() {
        this.voteCount++;
    }

    public void decrementVoteCount() {
        if (this.voteCount > 0) {
            this.voteCount--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchPartyMovieSuggestion that)) return false;
        // Use business key (party + movie) for equality
        return Objects.equals(party, that.party) &&
               Objects.equals(movie, that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(party, movie);
    }

    @Override
    public String toString() {
        return "WatchPartyMovieSuggestion{" +
               "id=" + id +
               ", movieId=" + (movie != null ? movie.getId() : null) +
               ", voteCount=" + voteCount +
               '}';
    }
}
