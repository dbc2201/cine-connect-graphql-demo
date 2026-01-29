package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchPartyMovieSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for WatchPartyMovieSuggestion entity operations.
 */
@Repository
public interface WatchPartyMovieSuggestionRepository extends JpaRepository<WatchPartyMovieSuggestion, Long> {

    /**
     * Find a suggestion by party and movie.
     */
    Optional<WatchPartyMovieSuggestion> findByPartyIdAndMovieId(Long partyId, Long movieId);

    /**
     * Find all suggestions for a party ordered by vote count.
     */
    @Query("SELECT s FROM WatchPartyMovieSuggestion s WHERE s.party.id = :partyId ORDER BY s.voteCount DESC, s.createdAt ASC")
    List<WatchPartyMovieSuggestion> findByPartyIdOrderByVoteCountDesc(@Param("partyId") Long partyId);

    /**
     * Find the top suggestion (most votes) for a party.
     */
    @Query("SELECT s FROM WatchPartyMovieSuggestion s WHERE s.party.id = :partyId ORDER BY s.voteCount DESC, s.createdAt ASC")
    List<WatchPartyMovieSuggestion> findTopSuggestionForParty(@Param("partyId") Long partyId);

    /**
     * Check if a movie has already been suggested for a party.
     */
    boolean existsByPartyIdAndMovieId(Long partyId, Long movieId);

    /**
     * Count suggestions for a party.
     */
    long countByPartyId(Long partyId);

    /**
     * Delete all suggestions for a party.
     */
    void deleteByPartyId(Long partyId);
}
