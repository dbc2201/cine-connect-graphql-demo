package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Genre;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Mood;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.repository.GenreRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MoodRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer for Movie operations.
 * Handles business logic and transaction management.
 */
@Service
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MoodRepository moodRepository;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        MoodRepository moodRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.moodRepository = moodRepository;
    }

    // Note: Not caching JPA entities directly as they have lazy-loaded proxies
    // that don't serialize well. Consider caching DTOs instead in production.
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Page<Movie> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> findByGenreSlug(String genreSlug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return movieRepository.findByGenreSlug(genreSlug, pageable);
    }

    public Page<Movie> findByMoodName(String moodName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return movieRepository.findByMoodName(moodName, pageable);
    }

    public Page<Movie> searchByTitle(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return movieRepository.findByTitleContainingIgnoreCase(query, pageable);
    }

    // Note: Genre and Mood are simple entities but still JPA-managed.
    // Caching can be enabled with proper DTO conversion for production use.
    public List<Genre> findAllGenres() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    public List<Mood> findAllMoods() {
        return moodRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Movie createMovie(String title, String originalTitle, Integer releaseYear,
                             Integer durationMinutes, String synopsis, String posterUrl,
                             String backdropUrl, String language, Integer tmdbId,
                             String imdbId, List<Long> genreIds, List<Long> moodIds) {
        Movie movie = new Movie(title);
        movie.setOriginalTitle(originalTitle);
        movie.setReleaseYear(releaseYear);
        movie.setDurationMinutes(durationMinutes);
        movie.setSynopsis(synopsis);
        movie.setPosterUrl(posterUrl);
        movie.setBackdropUrl(backdropUrl);
        movie.setLanguage(language);
        movie.setTmdbId(tmdbId);
        movie.setImdbId(imdbId);

        if (genreIds != null && !genreIds.isEmpty()) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(
                genreIds.stream().map(Long::intValue).toList()
            ));
            genres.forEach(movie::addGenre);
        }

        if (moodIds != null && !moodIds.isEmpty()) {
            Set<Mood> moods = new HashSet<>(moodRepository.findAllById(
                moodIds.stream().map(Long::intValue).toList()
            ));
            moods.forEach(movie::addMood);
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Optional<Movie> updateMovie(Long id, String title, String originalTitle,
                                       Integer releaseYear, Integer durationMinutes,
                                       String synopsis, String posterUrl, String backdropUrl,
                                       String language, List<Long> genreIds, List<Long> moodIds) {
        return movieRepository.findById(id).map(movie -> {
            if (title != null) movie.setTitle(title);
            if (originalTitle != null) movie.setOriginalTitle(originalTitle);
            if (releaseYear != null) movie.setReleaseYear(releaseYear);
            if (durationMinutes != null) movie.setDurationMinutes(durationMinutes);
            if (synopsis != null) movie.setSynopsis(synopsis);
            if (posterUrl != null) movie.setPosterUrl(posterUrl);
            if (backdropUrl != null) movie.setBackdropUrl(backdropUrl);
            if (language != null) movie.setLanguage(language);

            if (genreIds != null) {
                // Clear and re-add genres
                new HashSet<>(movie.getGenres()).forEach(movie::removeGenre);
                genreRepository.findAllById(genreIds.stream().map(Long::intValue).toList())
                    .forEach(movie::addGenre);
            }

            if (moodIds != null) {
                // Clear and re-add moods
                new HashSet<>(movie.getMoods()).forEach(movie::removeMood);
                moodRepository.findAllById(moodIds.stream().map(Long::intValue).toList())
                    .forEach(movie::addMood);
            }

            return movieRepository.save(movie);
        });
    }
}
