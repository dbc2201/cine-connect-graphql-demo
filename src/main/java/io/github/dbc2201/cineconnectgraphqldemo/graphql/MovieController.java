package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Genre;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Mood;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.CreateMovieInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.MovieFilterInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.UpdateMovieInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.MovieConnection;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.PageInfo;
import io.github.dbc2201.cineconnectgraphqldemo.service.MovieService;
import io.github.dbc2201.cineconnectgraphqldemo.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

/**
 * GraphQL controller for Movie queries and mutations.
 */
@Controller
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    public MovieController(MovieService movieService, ReviewService reviewService) {
        this.movieService = movieService;
        this.reviewService = reviewService;
    }

    // ========== Queries ==========

    @QueryMapping
    public Movie movie(@Argument Long id) {
        return movieService.findById(id).orElse(null);
    }

    @QueryMapping
    public MovieConnection movies(@Argument MovieFilterInput filter,
                                  @Argument Integer page,
                                  @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<Movie> moviePage;

        if (filter != null && filter.genreSlug() != null) {
            moviePage = movieService.findByGenreSlug(filter.genreSlug(), pageNum, pageSize);
        } else if (filter != null && filter.moodName() != null) {
            moviePage = movieService.findByMoodName(filter.moodName(), pageNum, pageSize);
        } else {
            moviePage = movieService.findAll(pageNum, pageSize);
        }

        return toMovieConnection(moviePage);
    }

    @QueryMapping
    public MovieConnection searchMovies(@Argument String query,
                                        @Argument Integer page,
                                        @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<Movie> moviePage = movieService.searchByTitle(query, pageNum, pageSize);
        return toMovieConnection(moviePage);
    }

    @QueryMapping
    public List<Genre> genres() {
        return movieService.findAllGenres();
    }

    @QueryMapping
    public List<Mood> moods() {
        return movieService.findAllMoods();
    }

    // ========== Mutations ==========

    @MutationMapping
    public Movie createMovie(@Argument CreateMovieInput input) {
        return movieService.createMovie(
            input.title(),
            input.originalTitle(),
            input.releaseYear(),
            input.durationMinutes(),
            input.synopsis(),
            input.posterUrl(),
            input.backdropUrl(),
            input.language(),
            input.tmdbId(),
            input.imdbId(),
            input.genreIds(),
            input.moodIds()
        );
    }

    @MutationMapping
    public Movie updateMovie(@Argument Long id, @Argument UpdateMovieInput input) {
        return movieService.updateMovie(
            id,
            input.title(),
            input.originalTitle(),
            input.releaseYear(),
            input.durationMinutes(),
            input.synopsis(),
            input.posterUrl(),
            input.backdropUrl(),
            input.language(),
            input.genreIds(),
            input.moodIds()
        ).orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
    }

    // ========== Field Resolvers ==========

    @SchemaMapping(typeName = "Movie", field = "averageRating")
    public Float averageRating(Movie movie) {
        return reviewService.getAverageRatingForMovie(movie.getId())
            .map(BigDecimal::floatValue)
            .orElse(null);
    }

    @SchemaMapping(typeName = "Movie", field = "reviewCount")
    public int reviewCount(Movie movie) {
        return (int) reviewService.getReviewCountForMovie(movie.getId());
    }

    // ========== Helper Methods ==========

    private MovieConnection toMovieConnection(Page<Movie> page) {
        PageInfo pageInfo = new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );
        return new MovieConnection(page.getContent(), pageInfo);
    }
}
