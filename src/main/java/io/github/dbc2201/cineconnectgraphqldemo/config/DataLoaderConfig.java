package io.github.dbc2201.cineconnectgraphqldemo.config;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MovieRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Configuration for GraphQL DataLoaders.
 * DataLoaders solve the N+1 query problem by batching database calls.
 *
 * Instead of loading users one-by-one for 100 reviews (100 queries),
 * a DataLoader collects all user IDs and loads them in a single query.
 */
@Configuration
public class DataLoaderConfig {

    public static final String USER_LOADER = "userLoader";
    public static final String MOVIE_LOADER = "movieLoader";

    public DataLoaderConfig(BatchLoaderRegistry registry,
                            UserRepository userRepository,
                            MovieRepository movieRepository) {

        // Register User DataLoader
        registry.forTypePair(Long.class, User.class)
            .withName(USER_LOADER)
            .registerMappedBatchLoader((userIds, env) -> {
                Set<Long> ids = Set.copyOf(userIds);
                Map<Long, User> usersById = userRepository.findAllByIdIn(ids)
                    .stream()
                    .collect(Collectors.toMap(User::getId, Function.identity()));
                return Mono.just(usersById);
            });

        // Register Movie DataLoader
        registry.forTypePair(Long.class, Movie.class)
            .withName(MOVIE_LOADER)
            .registerMappedBatchLoader((movieIds, env) -> {
                Set<Long> ids = Set.copyOf(movieIds);
                Map<Long, Movie> moviesById = movieRepository.findAllByIdIn(ids)
                    .stream()
                    .collect(Collectors.toMap(Movie::getId, Function.identity()));
                return Mono.just(moviesById);
            });
    }
}
