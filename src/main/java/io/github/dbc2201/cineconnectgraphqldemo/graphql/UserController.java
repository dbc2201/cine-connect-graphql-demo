package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.ReviewRepository;
import io.github.dbc2201.cineconnectgraphqldemo.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller for User queries.
 */
@Controller
public class UserController {

    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public UserController(UserService userService, ReviewRepository reviewRepository) {
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    // ========== Queries ==========

    @QueryMapping
    public User user(@Argument Long id) {
        return userService.findById(id).orElse(null);
    }

    @QueryMapping
    public User userByUsername(@Argument String username) {
        return userService.findByUsername(username).orElse(null);
    }

    // ========== Field Resolvers ==========

    @SchemaMapping(typeName = "User", field = "reviewCount")
    public int reviewCount(User user) {
        return (int) reviewRepository.findByUserId(user.getId(),
            org.springframework.data.domain.Pageable.unpaged()).getTotalElements();
    }

    // Note: followerCount, followingCount, watchlistCount, and isFollowedByMe
    // field resolvers are implemented in SocialController (Phase 5)
}
