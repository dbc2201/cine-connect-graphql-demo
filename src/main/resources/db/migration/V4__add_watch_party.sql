-- Watch Party Feature Migration
-- Enables users to create and join synchronized movie watching sessions

-- Watch party table
CREATE TABLE watch_parties (
    id BIGSERIAL PRIMARY KEY,
    host_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    movie_id BIGINT REFERENCES movies(id) ON DELETE SET NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE,
    ended_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    max_participants INT DEFAULT 10,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_party_status CHECK (status IN ('SCHEDULED', 'LIVE', 'ENDED', 'CANCELLED'))
);

-- Watch party participants table
CREATE TABLE watch_party_participants (
    id BIGSERIAL PRIMARY KEY,
    party_id BIGINT NOT NULL REFERENCES watch_parties(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'INVITED',
    voted_movie_id BIGINT REFERENCES movies(id) ON DELETE SET NULL,
    joined_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_party_user UNIQUE (party_id, user_id),
    CONSTRAINT chk_participant_status CHECK (status IN ('INVITED', 'JOINED', 'DECLINED', 'LEFT'))
);

-- Movie suggestions for watch party voting
CREATE TABLE watch_party_movie_suggestions (
    id BIGSERIAL PRIMARY KEY,
    party_id BIGINT NOT NULL REFERENCES watch_parties(id) ON DELETE CASCADE,
    movie_id BIGINT NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    suggested_by_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    vote_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_party_movie_suggestion UNIQUE (party_id, movie_id)
);

-- Indexes for watch party queries
CREATE INDEX idx_watch_parties_host ON watch_parties(host_id);
CREATE INDEX idx_watch_parties_status ON watch_parties(status);
CREATE INDEX idx_watch_parties_scheduled ON watch_parties(scheduled_at);
CREATE INDEX idx_watch_parties_public ON watch_parties(is_public) WHERE is_public = TRUE;

CREATE INDEX idx_participants_party ON watch_party_participants(party_id);
CREATE INDEX idx_participants_user ON watch_party_participants(user_id);
CREATE INDEX idx_participants_status ON watch_party_participants(status);

CREATE INDEX idx_suggestions_party ON watch_party_movie_suggestions(party_id);
CREATE INDEX idx_suggestions_votes ON watch_party_movie_suggestions(vote_count DESC);

-- Comments
COMMENT ON TABLE watch_parties IS 'Watch party events for synchronized movie viewing';
COMMENT ON TABLE watch_party_participants IS 'Participants in watch parties with their RSVP status';
COMMENT ON TABLE watch_party_movie_suggestions IS 'Movie suggestions for watch party voting';
