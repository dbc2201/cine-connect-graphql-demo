package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import java.util.List;

/**
 * Input type for creating a new movie.
 * Maps to GraphQL CreateMovieInput.
 */
public record CreateMovieInput(
    String title,
    String originalTitle,
    Integer releaseYear,
    Integer durationMinutes,
    String synopsis,
    String posterUrl,
    String backdropUrl,
    String language,
    Integer tmdbId,
    String imdbId,
    List<Long> genreIds,
    List<Long> moodIds
) {}
