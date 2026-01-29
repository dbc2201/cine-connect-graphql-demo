package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Follower;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Follower entity operations.
 */
@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    /**
     * Find a specific follow relationship.
     */
    Optional<Follower> findByFollowerAndFollowing(User follower, User following);

    /**
     * Check if a follow relationship exists.
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * Get all users that a specific user is following.
     */
    @Query("SELECT f.following FROM Follower f WHERE f.follower.id = :userId")
    Page<User> findFollowingByUserId(Long userId, Pageable pageable);

    /**
     * Get all users that follow a specific user.
     */
    @Query("SELECT f.follower FROM Follower f WHERE f.following.id = :userId")
    Page<User> findFollowersByUserId(Long userId, Pageable pageable);

    /**
     * Count how many users a specific user is following.
     */
    long countByFollowerId(Long followerId);

    /**
     * Count how many followers a specific user has.
     */
    long countByFollowingId(Long followingId);

    /**
     * Get the IDs of users that a specific user is following.
     * Useful for feed queries.
     */
    @Query("SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByUserId(Long userId);

    /**
     * Delete a follow relationship.
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
