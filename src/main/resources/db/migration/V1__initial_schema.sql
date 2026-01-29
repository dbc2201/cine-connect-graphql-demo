-- CineConnect Initial Schema
-- Version 1: Foundation tables

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    avatar_url VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster lookups
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Movies table
CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    original_title VARCHAR(255),
    release_year INTEGER,
    duration_minutes INTEGER,
    synopsis TEXT,
    poster_url VARCHAR(500),
    backdrop_url VARCHAR(500),
    language VARCHAR(10),
    tmdb_id INTEGER UNIQUE,
    imdb_id VARCHAR(20) UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index for search
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_release_year ON movies(release_year);

-- Genres lookup table
CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    slug VARCHAR(50) NOT NULL UNIQUE
);

-- Movie-Genre junction table
CREATE TABLE movie_genres (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
);

-- Moods lookup table
CREATE TABLE moods (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    emoji VARCHAR(10),
    description VARCHAR(255)
);

-- Movie-Mood junction table
CREATE TABLE movie_moods (
    movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    mood_id INTEGER REFERENCES moods(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, mood_id)
);

-- Seed default genres
INSERT INTO genres (name, slug) VALUES
    ('Action', 'action'),
    ('Adventure', 'adventure'),
    ('Animation', 'animation'),
    ('Comedy', 'comedy'),
    ('Crime', 'crime'),
    ('Documentary', 'documentary'),
    ('Drama', 'drama'),
    ('Family', 'family'),
    ('Fantasy', 'fantasy'),
    ('Horror', 'horror'),
    ('Mystery', 'mystery'),
    ('Romance', 'romance'),
    ('Science Fiction', 'sci-fi'),
    ('Thriller', 'thriller'),
    ('War', 'war'),
    ('Western', 'western');

-- Seed default moods
INSERT INTO moods (name, emoji, description) VALUES
    ('Feel Good', '😊', 'Light-hearted and uplifting'),
    ('Mind Bending', '🤯', 'Complex and thought-provoking'),
    ('Edge of Seat', '😰', 'Tense and suspenseful'),
    ('Tearjerker', '😢', 'Emotionally powerful'),
    ('Comfort Watch', '☕', 'Cozy and familiar'),
    ('Adrenaline Rush', '💥', 'Action-packed excitement'),
    ('Dark & Gritty', '🌑', 'Mature and intense themes'),
    ('Laugh Out Loud', '😂', 'Hilarious comedy');
