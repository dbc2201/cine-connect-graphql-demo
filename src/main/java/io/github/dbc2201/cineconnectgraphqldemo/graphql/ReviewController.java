package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.ReactionTag;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.CreateReviewInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.UpdateReviewInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.PageInfo;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.ReviewConnection;
import io.github.dbc2201.cineconnectgraphqldemo.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * GraphQL controller for Review queries and mutations.
 */
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ========== Queries ==========

    @QueryMapping
    public Review review(@Argument Long id) {
        return reviewService.findById(id).orElse(null);
    }

    @QueryMapping
    public ReviewConnection reviewsForMovie(@Argument Long movieId,
                                            @Argument Integer page,
                                            @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<Review> reviewPage = reviewService.findByMovieId(movieId, pageNum, pageSize);
        return toReviewConnection(reviewPage);
    }

    // ========== Mutations ==========

    @MutationMapping
    public Review createReview(@Argument CreateReviewInput input) {
        // TODO: In Phase 4, get userId from authenticated user context
        // For now, we need a way to identify the user - this should be fixed
        // when authentication is implemented
        Long userId = 1L; // Placeholder - will be replaced with auth context

        Set<ReactionTag> tags = input.reactionTags() != null
            ? new HashSet<>(input.reactionTags())
            : null;

        return reviewService.createReview(
            userId,
            input.movieId(),
            BigDecimal.valueOf(input.rating()),
            input.content(),
            input.containsSpoiler() != null ? input.containsSpoiler() : false,
            tags
        );
    }

    @MutationMapping
    public Review updateReview(@Argument Long id, @Argument UpdateReviewInput input) {
        Set<ReactionTag> tags = input.reactionTags() != null
            ? new HashSet<>(input.reactionTags())
            : null;

        BigDecimal rating = input.rating() != null
            ? BigDecimal.valueOf(input.rating())
            : null;

        return reviewService.updateReview(id, rating, input.content(),
                input.containsSpoiler(), tags)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
    }

    @MutationMapping
    public boolean deleteReview(@Argument Long id) {
        return reviewService.deleteReview(id);
    }

    // ========== Helper Methods ==========

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
