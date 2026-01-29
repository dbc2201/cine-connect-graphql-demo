package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

/**
 * Pagination information for GraphQL connections.
 * Maps to GraphQL PageInfo type.
 */
public record PageInfo(
    boolean hasNextPage,
    boolean hasPreviousPage,
    int totalPages,
    int totalElements
) {}
