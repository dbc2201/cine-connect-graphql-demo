package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import java.time.Instant;

/**
 * Input for creating a watch party.
 */
public record CreateWatchPartyInput(
    String title,
    String description,
    Instant scheduledAt,
    Long movieId,
    Integer maxParticipants,
    Boolean isPublic
) {}
