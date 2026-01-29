package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
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

    /**
     * Find reviews by multiple users (for activity feed).
     */
    Page<Review> findByUserIdIn(Collection<Long> userIds, Pageable pageable);

    /**
     * Find reviews for a specific movie by users that the current user follows.
     * Useful for showing "Your friends' reviews" on a movie page.
     */
    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId AND r.user.id IN :friendIds ORDER BY r.createdAt DESC")
    Page<Review> findFriendReviewsForMovie(@Param("movieId") Long movieId,
                                           @Param("friendIds") Collection<Long> friendIds,
                                           Pageable pageable);

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
     * Count reviews by a user.
     */
    long countByUserId(Long userId);

    /**
     * Check if a user has reviewed a movie.
     */
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
