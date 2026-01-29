package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchlistItem;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.FollowPayload;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.PageInfo;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.ReviewConnection;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.UserConnection;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.WatchlistConnection;
import io.github.dbc2201.cineconnectgraphqldemo.security.CineConnectUserDetailsService.CineConnectUserPrincipal;
import io.github.dbc2201.cineconnectgraphqldemo.service.FeedService;
import io.github.dbc2201.cineconnectgraphqldemo.service.SocialService;
import io.github.dbc2201.cineconnectgraphqldemo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller for social features: following and watchlist.
 */
@Controller
public class SocialController {

    private final SocialService socialService;
    private final UserService userService;
    private final FeedService feedService;

    public SocialController(SocialService socialService, UserService userService, FeedService feedService) {
        this.socialService = socialService;
        this.userService = userService;
        this.feedService = feedService;
    }

    // ========== Queries ==========

    @QueryMapping
    public UserConnection following(@Argument Long userId,
                                    @Argument Integer page,
                                    @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<User> users = socialService.getFollowing(userId, pageNum, pageSize);
        return toUserConnection(users);
    }

    @QueryMapping
    public UserConnection followers(@Argument Long userId,
                                    @Argument Integer page,
                                    @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<User> users = socialService.getFollowers(userId, pageNum, pageSize);
        return toUserConnection(users);
    }

    @QueryMapping
    public WatchlistConnection watchlist(@Argument Long userId,
                                         @Argument Integer page,
                                         @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<WatchlistItem> items = socialService.getWatchlist(userId, pageNum, pageSize);
        return toWatchlistConnection(items);
    }

    @QueryMapping
    public boolean isFollowing(@Argument Long userId,
                               @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.isFollowing(principal.getId(), userId);
    }

    @QueryMapping
    public boolean isInWatchlist(@Argument Long movieId,
                                 @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.isInWatchlist(principal.getId(), movieId);
    }

    @QueryMapping
    public ReviewConnection friendsFeed(@Argument Integer page,
                                        @Argument Integer size,
                                        @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<Review> reviews = feedService.getFriendsFeed(principal.getId(), pageNum, pageSize);
        return toReviewConnection(reviews);
    }

    @QueryMapping
    public ReviewConnection globalFeed(@Argument Integer page,
                                       @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<Review> reviews = feedService.getGlobalFeed(pageNum, pageSize);
        return toReviewConnection(reviews);
    }

    // ========== Mutations ==========

    @MutationMapping
    public FollowPayload followUser(@Argument Long userId,
                                    @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        socialService.followUser(principal.getId(), userId);
        User followedUser = userService.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new FollowPayload(true, followedUser);
    }

    @MutationMapping
    public boolean unfollowUser(@Argument Long userId,
                                @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.unfollowUser(principal.getId(), userId);
    }

    @MutationMapping
    public WatchlistItem addToWatchlist(@Argument Long movieId,
                                        @Argument String notes,
                                        @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.addToWatchlist(principal.getId(), movieId, notes);
    }

    @MutationMapping
    public boolean removeFromWatchlist(@Argument Long movieId,
                                       @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.removeFromWatchlist(principal.getId(), movieId);
    }

    @MutationMapping
    public WatchlistItem updateWatchlistNotes(@Argument Long movieId,
                                              @Argument String notes,
                                              @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return socialService.updateWatchlistNotes(principal.getId(), movieId, notes);
    }

    // ========== Field Resolvers for User ==========

    @SchemaMapping(typeName = "User", field = "followingCount")
    public int followingCount(User user) {
        return (int) socialService.getFollowingCount(user.getId());
    }

    @SchemaMapping(typeName = "User", field = "followerCount")
    public int followerCount(User user) {
        return (int) socialService.getFollowerCount(user.getId());
    }

    @SchemaMapping(typeName = "User", field = "watchlistCount")
    public int watchlistCount(User user) {
        return (int) socialService.getWatchlistCount(user.getId());
    }

    @SchemaMapping(typeName = "User", field = "isFollowedByMe")
    public Boolean isFollowedByMe(User user,
                                  @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return socialService.isFollowing(principal.getId(), user.getId());
    }

    // ========== Helper Methods ==========

    private UserConnection toUserConnection(Page<User> page) {
        PageInfo pageInfo = new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );
        return new UserConnection(page.getContent(), pageInfo);
    }

    private WatchlistConnection toWatchlistConnection(Page<WatchlistItem> page) {
        PageInfo pageInfo = new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );
        return new WatchlistConnection(page.getContent(), pageInfo);
    }

    private ReviewConnection toReviewConnection(Page<Review> page) {
        PageInfo pageInfo = new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );
        return new ReviewConnection(page.getContent(), pageInfo);
    }
}
