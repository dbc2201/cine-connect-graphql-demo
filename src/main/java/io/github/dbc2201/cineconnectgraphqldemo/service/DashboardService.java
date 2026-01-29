package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.config.RedisConfig;
import io.github.dbc2201.cineconnectgraphqldemo.domain.PartyStatus;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchParty;
import io.github.dbc2201.cineconnectgraphqldemo.repository.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for generating user dashboard statistics.
 * Provides aggregated data for the user's activity and engagement.
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FollowerRepository followerRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchPartyRepository watchPartyRepository;
    private final MovieRepository movieRepository;

    public DashboardService(UserRepository userRepository,
                            ReviewRepository reviewRepository,
                            FollowerRepository followerRepository,
                            WatchlistRepository watchlistRepository,
                            WatchPartyRepository watchPartyRepository,
                            MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.followerRepository = followerRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchPartyRepository = watchPartyRepository;
        this.movieRepository = movieRepository;
    }

    /**
     * Get dashboard statistics for a user.
     */
    public UserDashboard getUserDashboard(Long userId) {
        // Basic counts
        long reviewCount = reviewRepository.countByUserId(userId);
        long followerCount = followerRepository.countByFollowingId(userId);
        long followingCount = followerRepository.countByFollowerId(userId);
        long watchlistCount = watchlistRepository.countByUserId(userId);
        long hostedPartiesCount = watchPartyRepository.countByHostId(userId);

        // Recent activity
        List<Review> recentReviews = reviewRepository.findByUserId(userId,
            PageRequest.of(0, 5, Sort.by("createdAt").descending())).getContent();

        // Upcoming parties (hosted or participating)
        List<WatchParty> upcomingParties = watchPartyRepository.findPartiesForParticipantByStatus(
            userId, PartyStatus.SCHEDULED, PageRequest.of(0, 5)).getContent();

        return new UserDashboard(
            reviewCount,
            followerCount,
            followingCount,
            watchlistCount,
            hostedPartiesCount,
            recentReviews,
            upcomingParties
        );
    }

    /**
     * Get platform-wide statistics.
     */
    @Cacheable(value = RedisConfig.CACHE_STATS, key = "'platform'")
    public PlatformStats getPlatformStats() {
        long totalUsers = userRepository.count();
        long totalMovies = movieRepository.count();
        long totalReviews = reviewRepository.count();
        long scheduledParties = watchPartyRepository.countByStatus(PartyStatus.SCHEDULED);
        long liveParties = watchPartyRepository.countByStatus(PartyStatus.LIVE);

        return new PlatformStats(
            totalUsers,
            totalMovies,
            totalReviews,
            scheduledParties,
            liveParties
        );
    }

    /**
     * User dashboard data.
     */
    public record UserDashboard(
        long reviewCount,
        long followerCount,
        long followingCount,
        long watchlistCount,
        long hostedPartiesCount,
        List<Review> recentReviews,
        List<WatchParty> upcomingParties
    ) {}

    /**
     * Platform-wide statistics.
     */
    public record PlatformStats(
        long totalUsers,
        long totalMovies,
        long totalReviews,
        long scheduledParties,
        long liveParties
    ) {}
}
