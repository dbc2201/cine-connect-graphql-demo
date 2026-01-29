package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * Participant in a watch party.
 * Tracks invitation status and which movie they voted for.
 */
@Entity
@Table(name = "watch_party_participants",
       uniqueConstraints = @UniqueConstraint(columnNames = {"party_id", "user_id"}))
public class WatchPartyParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id", nullable = false)
    private WatchParty party;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParticipantStatus status = ParticipantStatus.INVITED;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voted_movie_id")
    private Movie votedMovie;

    @Column(name = "joined_at")
    private Instant joinedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public Movie getVotedMovie() {
        return votedMovie;
    }

    public void setVotedMovie(Movie votedMovie) {
        this.votedMovie = votedMovie;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Helper methods
    public void join() {
        this.status = ParticipantStatus.JOINED;
        this.joinedAt = Instant.now();
    }

    public void decline() {
        this.status = ParticipantStatus.DECLINED;
    }

    public void leave() {
        this.status = ParticipantStatus.LEFT;
    }

    public boolean hasJoined() {
        return status == ParticipantStatus.JOINED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchPartyParticipant that)) return false;
        // Use business key (party + user) for equality
        return Objects.equals(party, that.party) &&
               Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(party, user);
    }

    @Override
    public String toString() {
        return "WatchPartyParticipant{" +
               "id=" + id +
               ", userId=" + (user != null ? user.getId() : null) +
               ", status=" + status +
               '}';
    }
}
