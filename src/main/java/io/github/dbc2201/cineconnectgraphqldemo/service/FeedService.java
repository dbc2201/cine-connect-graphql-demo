package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import io.github.dbc2201.cineconnectgraphqldemo.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for generating activity feeds.
 * Provides personalized feeds based on who a user follows.
 */
@Service
@Transactional(readOnly = true)
public class FeedService {

    private final ReviewRepository reviewRepository;
    private final SocialService socialService;

    public FeedService(ReviewRepository reviewRepository, SocialService socialService) {
        this.reviewRepository = reviewRepository;
        this.socialService = socialService;
    }

    /**
     * Get the activity feed for a user.
     * Shows recent reviews from users they follow.
     *
     * @param userId The user requesting the feed
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Page of reviews from followed users, ordered by most recent
     */
    public Page<Review> getFriendsFeed(Long userId, int page, int size) {
        // Get IDs of users this user is following
        List<Long> followingIds = socialService.getFollowingIds(userId);

        if (followingIds.isEmpty()) {
            // Return empty page if not following anyone
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByUserIdIn(followingIds, pageable);
    }

    /**
     * Get the global feed showing recent reviews from all users.
     * Useful for discovery when user isn't following many people.
     *
     * @param page Page number (0-indexed)
     * @param size Page size
     * @return Page of all recent reviews
     */
    public Page<Review> getGlobalFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findAll(pageable);
    }
}
