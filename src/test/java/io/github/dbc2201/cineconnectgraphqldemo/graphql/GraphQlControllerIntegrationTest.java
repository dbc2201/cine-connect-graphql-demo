package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MovieRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.ReviewRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GraphQlControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Movie testMovie;
    private User testUser;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);

        reviewRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User("integrationuser", "integration@test.com", "password123");
        testUser.setDisplayName("Integration User");
        testUser = userRepository.save(testUser);

        testMovie = new Movie("Integration Test Movie");
        testMovie.setReleaseYear(2024);
        testMovie.setSynopsis("A movie for integration testing");
        testMovie.setLanguage("en");
        testMovie = movieRepository.save(testMovie);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> executeGraphQL(String query) {
        // Create proper JSON manually - no need to escape quotes if using Map
        Map<String, String> requestBody = Map.of("query", query.replace("\n", " ").trim());

        return restClient.post()
            .uri("/graphql")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .retrieve()
            .body(Map.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getData(Map<String, Object> response) {
        return (Map<String, Object>) response.get("data");
    }

    @Nested
    @DisplayName("Health Query Tests")
    class HealthQueryTests {

        @Test
        @DisplayName("should return health status")
        @SuppressWarnings("unchecked")
        void shouldReturnHealthStatus() {
            Map<String, Object> response = executeGraphQL("query { health { status version } }");

            Map<String, Object> data = getData(response);
            Map<String, Object> health = (Map<String, Object>) data.get("health");
            assertThat(health.get("status")).isEqualTo("UP");
            assertThat(health.get("version")).isNotNull();
        }
    }

    @Nested
    @DisplayName("Genre Query Tests")
    class GenreQueryTests {

        @Test
        @DisplayName("should return all genres")
        @SuppressWarnings("unchecked")
        void shouldReturnAllGenres() {
            Map<String, Object> response = executeGraphQL("query { genres { id name slug } }");

            Map<String, Object> data = getData(response);
            List<Map<String, Object>> genres = (List<Map<String, Object>>) data.get("genres");
            assertThat(genres).isNotEmpty();
            assertThat(genres.getFirst().get("name")).isNotNull();
            assertThat(genres.getFirst().get("slug")).isNotNull();
        }
    }

    @Nested
    @DisplayName("Mood Query Tests")
    class MoodQueryTests {

        @Test
        @DisplayName("should return all moods")
        @SuppressWarnings("unchecked")
        void shouldReturnAllMoods() {
            Map<String, Object> response = executeGraphQL("query { moods { id name emoji } }");

            Map<String, Object> data = getData(response);
            List<Map<String, Object>> moods = (List<Map<String, Object>>) data.get("moods");
            assertThat(moods).isNotEmpty();
            assertThat(moods.getFirst().get("name")).isNotNull();
            assertThat(moods.getFirst().get("emoji")).isNotNull();
        }
    }

    @Nested
    @DisplayName("Movie Query Tests")
    class MovieQueryTests {

        @Test
        @DisplayName("should return movie by id")
        @SuppressWarnings("unchecked")
        void shouldReturnMovieById() {
            String query = "query { movie(id: %d) { id title releaseYear synopsis } }".formatted(testMovie.getId());
            Map<String, Object> response = executeGraphQL(query);

            Map<String, Object> data = getData(response);
            Map<String, Object> movie = (Map<String, Object>) data.get("movie");
            assertThat(movie.get("id")).isEqualTo(testMovie.getId().toString());
            assertThat(movie.get("title")).isEqualTo("Integration Test Movie");
            assertThat(movie.get("releaseYear")).isEqualTo(2024);
        }

        @Test
        @DisplayName("should return null for non-existent movie")
        void shouldReturnNullForNonExistentMovie() {
            Map<String, Object> response = executeGraphQL("query { movie(id: 99999) { id title } }");

            Map<String, Object> data = getData(response);
            assertThat(data.get("movie")).isNull();
        }

        @Test
        @DisplayName("should return paginated movies")
        @SuppressWarnings("unchecked")
        void shouldReturnPaginatedMovies() {
            Map<String, Object> response = executeGraphQL(
                "query { movies(page: 0, size: 10) { content { id title } pageInfo { totalElements } } }");

            Map<String, Object> data = getData(response);
            Map<String, Object> movies = (Map<String, Object>) data.get("movies");
            List<Map<String, Object>> content = (List<Map<String, Object>>) movies.get("content");
            assertThat(content).isNotEmpty();
        }

        @Test
        @DisplayName("should search movies by title")
        @SuppressWarnings("unchecked")
        void shouldSearchMoviesByTitle() {
            Map<String, Object> response = executeGraphQL(
                "query { searchMovies(query: \"Integration\", page: 0, size: 10) { content { id title } } }");

            Map<String, Object> data = getData(response);
            Map<String, Object> searchResult = (Map<String, Object>) data.get("searchMovies");
            List<Map<String, Object>> content = (List<Map<String, Object>>) searchResult.get("content");
            assertThat(content.getFirst().get("title").toString()).contains("Integration");
        }
    }

    @Nested
    @DisplayName("User Query Tests")
    class UserQueryTests {

        @Test
        @DisplayName("should return user by id")
        @SuppressWarnings("unchecked")
        void shouldReturnUserById() {
            String query = "query { user(id: %d) { id username displayName email } }".formatted(testUser.getId());
            Map<String, Object> response = executeGraphQL(query);

            Map<String, Object> data = getData(response);
            Map<String, Object> user = (Map<String, Object>) data.get("user");
            assertThat(user.get("username")).isEqualTo("integrationuser");
            assertThat(user.get("displayName")).isEqualTo("Integration User");
            assertThat(user.get("email")).isEqualTo("integration@test.com");
        }

        @Test
        @DisplayName("should return user by username")
        @SuppressWarnings("unchecked")
        void shouldReturnUserByUsername() {
            Map<String, Object> response = executeGraphQL(
                "query { userByUsername(username: \"integrationuser\") { id username displayName } }");

            Map<String, Object> data = getData(response);
            Map<String, Object> user = (Map<String, Object>) data.get("userByUsername");
            assertThat(user.get("username")).isEqualTo("integrationuser");
        }
    }

    @Nested
    @DisplayName("Movie Mutation Tests")
    class MovieMutationTests {

        @Test
        @DisplayName("should create a new movie")
        @SuppressWarnings("unchecked")
        void shouldCreateNewMovie() {
            Map<String, Object> response = executeGraphQL(
                "mutation { createMovie(input: { title: \"New Test Movie\", releaseYear: 2025, synopsis: \"A brand new test movie\", language: \"en\" }) { id title releaseYear synopsis } }");

            Map<String, Object> data = getData(response);
            Map<String, Object> movie = (Map<String, Object>) data.get("createMovie");
            assertThat(movie.get("title")).isEqualTo("New Test Movie");
            assertThat(movie.get("releaseYear")).isEqualTo(2025);
            assertThat(movie.get("id")).isNotNull();
        }

        @Test
        @DisplayName("should update an existing movie")
        @SuppressWarnings("unchecked")
        void shouldUpdateExistingMovie() {
            String query = "mutation { updateMovie(id: %d, input: { synopsis: \"Updated synopsis for testing\" }) { id title synopsis } }".formatted(testMovie.getId());
            Map<String, Object> response = executeGraphQL(query);

            Map<String, Object> data = getData(response);
            Map<String, Object> movie = (Map<String, Object>) data.get("updateMovie");
            assertThat(movie.get("id")).isEqualTo(testMovie.getId().toString());
            assertThat(movie.get("synopsis")).isEqualTo("Updated synopsis for testing");
        }
    }

    @Nested
    @DisplayName("Field Resolver Tests")
    class FieldResolverTests {

        @Test
        @DisplayName("should resolve averageRating and reviewCount for movie")
        @SuppressWarnings("unchecked")
        void shouldResolveMovieFieldResolvers() {
            String query = "query { movie(id: %d) { id title averageRating reviewCount } }".formatted(testMovie.getId());
            Map<String, Object> response = executeGraphQL(query);

            Map<String, Object> data = getData(response);
            Map<String, Object> movie = (Map<String, Object>) data.get("movie");
            assertThat(movie.get("reviewCount")).isEqualTo(0);
            assertThat(movie.get("averageRating")).isNull();
        }
    }
}
