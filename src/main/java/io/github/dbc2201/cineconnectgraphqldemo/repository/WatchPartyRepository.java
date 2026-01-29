package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.PartyStatus;
import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository for WatchParty entity operations.
 */
@Repository
public interface WatchPartyRepository extends JpaRepository<WatchParty, Long> {

    /**
     * Find parties hosted by a specific user.
     */
    Page<WatchParty> findByHostId(Long hostId, Pageable pageable);

    /**
     * Find parties by status.
     */
    Page<WatchParty> findByStatus(PartyStatus status, Pageable pageable);

    /**
     * Find public parties that are scheduled.
     */
    @Query("SELECT wp FROM WatchParty wp WHERE wp.isPublic = true AND wp.status = :status ORDER BY wp.scheduledAt ASC")
    Page<WatchParty> findPublicPartiesByStatus(@Param("status") PartyStatus status, Pageable pageable);

    /**
     * Find upcoming public parties.
     */
    @Query("SELECT wp FROM WatchParty wp WHERE wp.isPublic = true AND wp.status = 'SCHEDULED' AND wp.scheduledAt > :now ORDER BY wp.scheduledAt ASC")
    Page<WatchParty> findUpcomingPublicParties(@Param("now") Instant now, Pageable pageable);

    /**
     * Find parties a user is participating in.
     */
    @Query("SELECT wp FROM WatchParty wp JOIN wp.participants p WHERE p.user.id = :userId ORDER BY wp.scheduledAt DESC")
    Page<WatchParty> findPartiesForParticipant(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find parties a user is participating in with specific status.
     */
    @Query("SELECT wp FROM WatchParty wp JOIN wp.participants p WHERE p.user.id = :userId AND wp.status = :status ORDER BY wp.scheduledAt ASC")
    Page<WatchParty> findPartiesForParticipantByStatus(@Param("userId") Long userId,
                                                        @Param("status") PartyStatus status,
                                                        Pageable pageable);

    /**
     * Find live parties for a user (either hosting or participating).
     */
    @Query("SELECT wp FROM WatchParty wp LEFT JOIN wp.participants p " +
           "WHERE wp.status = 'LIVE' AND (wp.host.id = :userId OR p.user.id = :userId)")
    List<WatchParty> findLivePartiesForUser(@Param("userId") Long userId);

    /**
     * Count parties hosted by a user.
     */
    long countByHostId(Long hostId);

    /**
     * Count parties by status.
     */
    long countByStatus(PartyStatus status);
}
