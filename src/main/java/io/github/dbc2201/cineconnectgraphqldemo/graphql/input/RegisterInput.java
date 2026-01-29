package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

/**
 * Input for user registration.
 */
public record RegisterInput(
    String username,
    String email,
    String password,
    String displayName
) {}
