package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

/**
 * Input type for filtering movies.
 * Maps to GraphQL MovieFilterInput.
 */
public record MovieFilterInput(
    String genreSlug,
    String moodName,
    Integer releaseYear,
    String language,
    Integer maxDurationMinutes,
    Float minRating
) {}
