package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import io.github.dbc2201.cineconnectgraphqldemo.domain.ReactionTag;

import java.util.List;

/**
 * Input type for creating a new review.
 * Maps to GraphQL CreateReviewInput.
 */
public record CreateReviewInput(
    Long movieId,
    Double rating,
    String content,
    Boolean containsSpoiler,
    List<ReactionTag> reactionTags
) {}
