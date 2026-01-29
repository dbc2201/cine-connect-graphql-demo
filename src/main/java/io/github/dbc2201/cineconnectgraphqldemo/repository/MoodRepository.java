package io.github.dbc2201.cineconnectgraphqldemo.repository;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Mood entity operations.
 */
@Repository
public interface MoodRepository extends JpaRepository<Mood, Integer> {

    Optional<Mood> findByNameIgnoreCase(String name);

    List<Mood> findAllByOrderByNameAsc();
}
