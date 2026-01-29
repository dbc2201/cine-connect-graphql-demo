package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for WatchlistItem entity operations.
 */
@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {

    /**
     * Find a specific watchlist item.
     */
    Optional<WatchlistItem> findByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Check if a movie is in a user's watchlist.
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Get a user's watchlist, ordered by most recently added.
     */
    Page<WatchlistItem> findByUserIdOrderByAddedAtDesc(Long userId, Pageable pageable);

    /**
     * Count items in a user's watchlist.
     */
    long countByUserId(Long userId);

    /**
     * Delete a movie from a user's watchlist.
     */
    void deleteByUserIdAndMovieId(Long userId, Long movieId);
}
