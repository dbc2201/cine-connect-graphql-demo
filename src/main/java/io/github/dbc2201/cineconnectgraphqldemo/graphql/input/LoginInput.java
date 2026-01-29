package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

/**
 * Input for user login.
 */
public record LoginInput(
    String usernameOrEmail,
    String password
) {}
