package io.github.dbc2201.cineconnectgraphqldemo.graphql.input;

import java.util.List;

/**
 * Input type for updating an existing movie.
 * Maps to GraphQL UpdateMovieInput.
 * All fields are optional - only provided fields will be updated.
 */
public record UpdateMovieInput(
    String title,
    String originalTitle,
    Integer releaseYear,
    Integer durationMinutes,
    String synopsis,
    String posterUrl,
    String backdropUrl,
    String language,
    List<Long> genreIds,
    List<Long> moodIds
) {}
