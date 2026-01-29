package io.github.dbc2201.cineconnectgraphqldemo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Represents a movie mood/vibe (e.g., Feel Good, Mind Bending, Tearjerker).
 * Moods help users find movies matching their current emotional state.
 */
@Entity
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Size(max = 10)
    @Column(length = 10)
    private String emoji;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    protected Mood() {}

    public Mood(String name, String emoji, String description) {
        this.name = name;
        this.emoji = emoji;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mood mood = (Mood) o;
        return Objects.equals(id, mood.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mood{id=%d, name='%s', emoji='%s'}".formatted(id, name, emoji);
    }
}
