-- V3: Add social features - followers and watchlist

-- Follower relationship table (asymmetric: A follows B doesn't mean B follows A)
CREATE TABLE followers (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    following_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- A user can only follow another user once
    CONSTRAINT uq_follower_following UNIQUE (follower_id, following_id),
    -- A user cannot follow themselves
    CONSTRAINT chk_no_self_follow CHECK (follower_id != following_id)
);

-- Indexes for efficient lookups
CREATE INDEX idx_followers_follower_id ON followers(follower_id);
CREATE INDEX idx_followers_following_id ON followers(following_id);

-- Watchlist table - movies a user wants to watch
CREATE TABLE watchlist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    added_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,

    -- A user can only add a movie to their watchlist once
    CONSTRAINT uq_user_movie_watchlist UNIQUE (user_id, movie_id)
);

-- Index for efficient user watchlist lookup
CREATE INDEX idx_watchlist_user_id ON watchlist(user_id);
CREATE INDEX idx_watchlist_added_at ON watchlist(user_id, added_at DESC);
