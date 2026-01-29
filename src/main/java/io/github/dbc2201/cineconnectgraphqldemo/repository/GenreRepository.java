package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Genre entity operations.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findBySlug(String slug);

    Optional<Genre> findByNameIgnoreCase(String name);
}
