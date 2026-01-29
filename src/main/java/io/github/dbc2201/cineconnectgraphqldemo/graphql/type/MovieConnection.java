package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;

import java.util.List;

/**
 * Connection type for paginated movie results.
 * Maps to GraphQL MovieConnection type.
 */
public record MovieConnection(
    List<Movie> content,
    PageInfo pageInfo
) {}
