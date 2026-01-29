package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Follower;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchlistItem;
import io.github.dbc2201.cineconnectgraphqldemo.repository.FollowerRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MovieRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.WatchlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for social features: following users and watchlist management.
 */
@Service
@Transactional(readOnly = true)
public class SocialService {

    private final FollowerRepository followerRepository;
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public SocialService(FollowerRepository followerRepository,
                         WatchlistRepository watchlistRepository,
                         UserRepository userRepository,
                         MovieRepository movieRepository) {
        this.followerRepository = followerRepository;
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // ========== Follower Operations ==========

    /**
     * Follow a user.
     *
     * @throws IllegalArgumentException if users don't exist or trying to self-follow
     * @throws IllegalStateException if already following
     */
    @Transactional
    public Follower followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        if (followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("Already following this user");
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("Follower not found: " + followerId));
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("User to follow not found: " + followingId));

        Follower relationship = new Follower(follower, following);
        return followerRepository.save(relationship);
    }

    /**
     * Unfollow a user.
     *
     * @return true if unfollowed, false if wasn't following
     */
    @Transactional
    public boolean unfollowUser(Long followerId, Long followingId) {
        if (followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followerRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
            return true;
        }
        return false;
    }

    /**
     * Check if user A is following user B.
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    /**
     * Get users that a specific user is following.
     */
    public Page<User> getFollowing(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return followerRepository.findFollowingByUserId(userId, pageable);
    }

    /**
     * Get followers of a specific user.
     */
    public Page<User> getFollowers(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return followerRepository.findFollowersByUserId(userId, pageable);
    }

    /**
     * Count users that a specific user is following.
     */
    public long getFollowingCount(Long userId) {
        return followerRepository.countByFollowerId(userId);
    }

    /**
     * Count followers of a specific user.
     */
    public long getFollowerCount(Long userId) {
        return followerRepository.countByFollowingId(userId);
    }

    /**
     * Get IDs of users that a specific user is following.
     * Useful for feed generation.
     */
    public List<Long> getFollowingIds(Long userId) {
        return followerRepository.findFollowingIdsByUserId(userId);
    }

    // ========== Watchlist Operations ==========

    /**
     * Add a movie to user's watchlist.
     *
     * @throws IllegalStateException if movie already in watchlist
     */
    @Transactional
    public WatchlistItem addToWatchlist(Long userId, Long movieId, String notes) {
        if (watchlistRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new IllegalStateException("Movie already in watchlist");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        WatchlistItem item = new WatchlistItem(user, movie);
        if (notes != null) {
            item.setNotes(notes);
        }

        return watchlistRepository.save(item);
    }

    /**
     * Remove a movie from user's watchlist.
     *
     * @return true if removed, false if wasn't in watchlist
     */
    @Transactional
    public boolean removeFromWatchlist(Long userId, Long movieId) {
        if (watchlistRepository.existsByUserIdAndMovieId(userId, movieId)) {
            watchlistRepository.deleteByUserIdAndMovieId(userId, movieId);
            return true;
        }
        return false;
    }

    /**
     * Check if a movie is in user's watchlist.
     */
    public boolean isInWatchlist(Long userId, Long movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    /**
     * Get user's watchlist.
     */
    public Page<WatchlistItem> getWatchlist(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return watchlistRepository.findByUserIdOrderByAddedAtDesc(userId, pageable);
    }

    /**
     * Count items in user's watchlist.
     */
    public long getWatchlistCount(Long userId) {
        return watchlistRepository.countByUserId(userId);
    }

    /**
     * Update notes on a watchlist item.
     */
    @Transactional
    public WatchlistItem updateWatchlistNotes(Long userId, Long movieId, String notes) {
        WatchlistItem item = watchlistRepository.findByUserIdAndMovieId(userId, movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie not in watchlist"));

        item.setNotes(notes);
        return watchlistRepository.save(item);
    }
}
