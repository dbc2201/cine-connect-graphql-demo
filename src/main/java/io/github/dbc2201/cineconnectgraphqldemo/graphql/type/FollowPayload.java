package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;

/**
 * Response payload for follow mutation.
 */
public record FollowPayload(
    boolean success,
    User user
) {}
