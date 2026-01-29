package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository for Movie entity operations.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Find a movie by its TMDB ID.
     */
    Optional<Movie> findByTmdbId(Integer tmdbId);

    /**
     * Find a movie by its IMDB ID.
     */
    Optional<Movie> findByImdbId(String imdbId);

    /**
     * Search movies by title (case-insensitive, partial match).
     */
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Find movies by release year.
     */
    List<Movie> findByReleaseYear(Integer year);

    /**
     * Find movies by genre slug.
     */
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.slug = :genreSlug")
    Page<Movie> findByGenreSlug(@Param("genreSlug") String genreSlug, Pageable pageable);

    /**
     * Find movies by mood name.
     */
    @Query("SELECT m FROM Movie m JOIN m.moods mood WHERE mood.name = :moodName")
    Page<Movie> findByMoodName(@Param("moodName") String moodName, Pageable pageable);

    /**
     * Find movies by language.
     */
    Page<Movie> findByLanguage(String language, Pageable pageable);

    /**
     * Find movies shorter than a given duration.
     */
    Page<Movie> findByDurationMinutesLessThanEqual(Integer maxDuration, Pageable pageable);

    /**
     * Batch load movies by their IDs (for DataLoader).
     */
    List<Movie> findAllByIdIn(Set<Long> ids);
}
