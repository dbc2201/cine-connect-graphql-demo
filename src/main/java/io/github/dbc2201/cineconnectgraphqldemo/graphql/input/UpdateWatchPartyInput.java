package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import java.time.Instant;

/**
 * Input for updating a watch party.
 */
public record UpdateWatchPartyInput(
    String title,
    String description,
    Instant scheduledAt,
    Integer maxParticipants,
    Boolean isPublic
) {}
