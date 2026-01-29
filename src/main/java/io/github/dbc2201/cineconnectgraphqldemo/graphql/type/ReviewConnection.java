package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;

import java.util.List;

/**
 * Connection type for paginated review results.
 * Maps to GraphQL ReviewConnection type.
 */
public record ReviewConnection(
    List<Review> content,
    PageInfo pageInfo
) {}
