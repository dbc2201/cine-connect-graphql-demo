package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.ParticipantStatus;
import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchPartyParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for WatchPartyParticipant entity operations.
 */
@Repository
public interface WatchPartyParticipantRepository extends JpaRepository<WatchPartyParticipant, Long> {

    /**
     * Find a participant by party and user.
     */
    Optional<WatchPartyParticipant> findByPartyIdAndUserId(Long partyId, Long userId);

    /**
     * Find all participants for a party.
     */
    List<WatchPartyParticipant> findByPartyId(Long partyId);

    /**
     * Find participants by party and status.
     */
    List<WatchPartyParticipant> findByPartyIdAndStatus(Long partyId, ParticipantStatus status);

    /**
     * Check if a user is a participant in a party.
     */
    boolean existsByPartyIdAndUserId(Long partyId, Long userId);

    /**
     * Count participants with a specific status.
     */
    long countByPartyIdAndStatus(Long partyId, ParticipantStatus status);

    /**
     * Count all participants who have joined a party.
     */
    @Query("SELECT COUNT(p) FROM WatchPartyParticipant p WHERE p.party.id = :partyId AND p.status = 'JOINED'")
    long countJoinedParticipants(@Param("partyId") Long partyId);

    /**
     * Find participants who voted for a specific movie.
     */
    List<WatchPartyParticipant> findByPartyIdAndVotedMovieId(Long partyId, Long movieId);

    /**
     * Delete all participants for a party.
     */
    void deleteByPartyId(Long partyId);
}
