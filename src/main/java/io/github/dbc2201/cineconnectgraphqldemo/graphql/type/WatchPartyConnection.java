package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchParty;

import java.util.List;

/**
 * Paginated connection type for watch parties.
 */
public record WatchPartyConnection(
    List<WatchParty> content,
    PageInfo pageInfo
) {}
