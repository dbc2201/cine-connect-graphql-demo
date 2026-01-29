package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Repository for Review entity operations.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find a user's review for a specific movie.
     */
    Optional<Review> findByUserIdAndMovieId(Long userId, Long movieId);

    /**
     * Find all reviews by a user.
     */
    Page<Review> findByUserId(Long userId, Pageable pageable);

    /**
     * Find all reviews for a movie.
     */
    Page<Review> findByMovieId(Long movieId, Pageable pageable);

    // TODO: Add findFriendReviewsForMovie in Phase 5 when Follower entity is created

    /**
     * Calculate average rating for a movie.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Optional<BigDecimal> findAverageRatingByMovieId(@Param("movieId") Long movieId);

    /**
     * Count reviews for a movie.
     */
    long countByMovieId(Long movieId);

    /**
     * Check if a user has reviewed a movie.
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
