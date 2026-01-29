package io.github.dbc2201.cineconnectgraphqldemo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the Movie entity.
 */
@DisplayName("Movie Entity")
class MovieTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create movie with title")
        void shouldCreateMovieWithTitle() {
            Movie movie = new Movie("Inception");

            assertThat(movie.getTitle()).isEqualTo("Inception");
        }

        @Test
        @DisplayName("should have empty genre and mood sets by default")
        void shouldHaveEmptyGenreAndMoodSetsByDefault() {
            Movie movie = new Movie("Test Movie");

            assertThat(movie.getGenres()).isEmpty();
            assertThat(movie.getMoods()).isEmpty();
        }

        @Test
        @DisplayName("should have null optional fields by default")
        void shouldHaveNullOptionalFieldsByDefault() {
            Movie movie = new Movie("Test Movie");

            assertThat(movie.getOriginalTitle()).isNull();
            assertThat(movie.getReleaseYear()).isNull();
            assertThat(movie.getDurationMinutes()).isNull();
            assertThat(movie.getSynopsis()).isNull();
            assertThat(movie.getPosterUrl()).isNull();
            assertThat(movie.getLanguage()).isNull();
            assertThat(movie.getTmdbId()).isNull();
            assertThat(movie.getImdbId()).isNull();
        }
    }

    @Nested
    @DisplayName("Genre Management")
    class GenreManagement {

        @Test
        @DisplayName("should add genre to movie")
        void shouldAddGenreToMovie() {
            Movie movie = new Movie("Inception");
            Genre sciFi = new Genre("Science Fiction", "sci-fi");

            movie.addGenre(sciFi);

            assertThat(movie.getGenres()).contains(sciFi);
        }

        @Test
        @DisplayName("should remove genre from movie")
        void shouldRemoveGenreFromMovie() {
            Movie movie = new Movie("Inception");
            Genre sciFi = new Genre("Science Fiction", "sci-fi");
            movie.addGenre(sciFi);

            movie.removeGenre(sciFi);

            assertThat(movie.getGenres()).doesNotContain(sciFi);
        }

        @Test
        @DisplayName("should not add duplicate genres")
        void shouldNotAddDuplicateGenres() {
            Movie movie = new Movie("Inception");
            Genre sciFi = new Genre("Science Fiction", "sci-fi");

            movie.addGenre(sciFi);
            movie.addGenre(sciFi);

            assertThat(movie.getGenres()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Mood Management")
    class MoodManagement {

        @Test
        @DisplayName("should add mood to movie")
        void shouldAddMoodToMovie() {
            Movie movie = new Movie("Inception");
            Mood mindBending = new Mood("Mind Bending", "🤯", "Complex, thought-provoking");

            movie.addMood(mindBending);

            assertThat(movie.getMoods()).contains(mindBending);
        }

        @Test
        @DisplayName("should remove mood from movie")
        void shouldRemoveMoodFromMovie() {
            Movie movie = new Movie("Inception");
            Mood mindBending = new Mood("Mind Bending", "🤯", "Complex");
            movie.addMood(mindBending);

            movie.removeMood(mindBending);

            assertThat(movie.getMoods()).doesNotContain(mindBending);
        }
    }

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("should update all fields")
        void shouldUpdateAllFields() {
            Movie movie = new Movie("Original Title");

            movie.setTitle("New Title");
            movie.setOriginalTitle("原題");
            movie.setReleaseYear(2010);
            movie.setDurationMinutes(148);
            movie.setSynopsis("A thief who steals corporate secrets...");
            movie.setPosterUrl("https://example.com/poster.jpg");
            movie.setBackdropUrl("https://example.com/backdrop.jpg");
            movie.setLanguage("en");
            movie.setTmdbId(27205);
            movie.setImdbId("tt1375666");

            assertThat(movie.getTitle()).isEqualTo("New Title");
            assertThat(movie.getOriginalTitle()).isEqualTo("原題");
            assertThat(movie.getReleaseYear()).isEqualTo(2010);
            assertThat(movie.getDurationMinutes()).isEqualTo(148);
            assertThat(movie.getSynopsis()).startsWith("A thief");
            assertThat(movie.getPosterUrl()).contains("poster");
            assertThat(movie.getBackdropUrl()).contains("backdrop");
            assertThat(movie.getLanguage()).isEqualTo("en");
            assertThat(movie.getTmdbId()).isEqualTo(27205);
            assertThat(movie.getImdbId()).isEqualTo("tt1375666");
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("should include title and year")
        void shouldIncludeTitleAndYear() {
            Movie movie = new Movie("Inception");
            movie.setReleaseYear(2010);

            String result = movie.toString();

            assertThat(result)
                .contains("Inception")
                .contains("2010");
        }
    }
}
