package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;

import java.util.List;

/**
 * Paginated connection type for users.
 */
public record UserConnection(
    List<User> content,
    PageInfo pageInfo
) {}
