package io.github.dbc2201.cineconnectgraphqldemo.graphql.type;

import io.github.dbc2201.cineconnectgraphqldemo.domain.WatchlistItem;

import java.util.List;

/**
 * Paginated connection type for watchlist items.
 */
public record WatchlistConnection(
    List<WatchlistItem> content,
    PageInfo pageInfo
) {}
