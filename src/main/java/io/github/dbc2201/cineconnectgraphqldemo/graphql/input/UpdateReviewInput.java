package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import io.github.dbc2201.cineconnectgraphqldemo.domain.ReactionTag;

import java.util.List;

/**
 * Input type for updating an existing review.
 * Maps to GraphQL UpdateReviewInput.
 * All fields are optional - only provided fields will be updated.
 */
public record UpdateReviewInput(
    Double rating,
    String content,
    Boolean containsSpoiler,
    List<ReactionTag> reactionTags
) {}
