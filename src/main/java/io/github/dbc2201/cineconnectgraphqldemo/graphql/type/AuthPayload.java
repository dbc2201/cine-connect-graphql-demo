package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;

/**
 * Authentication response containing tokens and user data.
 */
public record AuthPayload(
    String accessToken,
    String refreshToken,
    User user
) {}
