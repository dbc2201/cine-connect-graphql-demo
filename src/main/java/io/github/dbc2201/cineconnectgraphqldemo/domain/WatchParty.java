package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Watch party entity for synchronized movie viewing sessions.
 * Users can create parties, invite friends, and vote on movies to watch.
 */
@Entity
@Table(name = "watch_parties")
public class WatchParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PartyStatus status = PartyStatus.SCHEDULED;

    @Column(name = "max_participants")
    private Integer maxParticipants = 10;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<WatchPartyParticipant> participants = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<WatchPartyMovieSuggestion> movieSuggestions = new HashSet<>();

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

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Instant scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public PartyStatus getStatus() {
        return status;
    }

    public void setStatus(PartyStatus status) {
        this.status = status;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<WatchPartyParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<WatchPartyParticipant> participants) {
        this.participants = participants;
    }

    public Set<WatchPartyMovieSuggestion> getMovieSuggestions() {
        return movieSuggestions;
    }

    public void setMovieSuggestions(Set<WatchPartyMovieSuggestion> movieSuggestions) {
        this.movieSuggestions = movieSuggestions;
    }

    // Helper methods
    public void addParticipant(WatchPartyParticipant participant) {
        participants.add(participant);
        participant.setParty(this);
    }

    public void removeParticipant(WatchPartyParticipant participant) {
        participants.remove(participant);
        participant.setParty(null);
    }

    public void addMovieSuggestion(WatchPartyMovieSuggestion suggestion) {
        movieSuggestions.add(suggestion);
        suggestion.setParty(this);
    }

    public int getParticipantCount() {
        return (int) participants.stream()
            .filter(p -> p.getStatus() == ParticipantStatus.JOINED)
            .count();
    }

    public boolean isFull() {
        return maxParticipants != null && getParticipantCount() >= maxParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchParty that)) return false;
        // Use business key (host + title + scheduledAt) for equality
        return Objects.equals(host, that.host) &&
               Objects.equals(title, that.title) &&
               Objects.equals(scheduledAt, that.scheduledAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, title, scheduledAt);
    }

    @Override
    public String toString() {
        return "WatchParty{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", status=" + status +
               ", scheduledAt=" + scheduledAt +
               '}';
    }
}
