package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.*;
import io.github.dbc2201.cineconnectgraphqldemo.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing watch parties.
 * Handles creation, joining, leaving, and movie voting.
 */
@Service
@Transactional
public class WatchPartyService {

    private final WatchPartyRepository partyRepository;
    private final WatchPartyParticipantRepository participantRepository;
    private final WatchPartyMovieSuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchPartyService(WatchPartyRepository partyRepository,
                             WatchPartyParticipantRepository participantRepository,
                             WatchPartyMovieSuggestionRepository suggestionRepository,
                             UserRepository userRepository,
                             MovieRepository movieRepository) {
        this.partyRepository = partyRepository;
        this.participantRepository = participantRepository;
        this.suggestionRepository = suggestionRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // ========== Party CRUD Operations ==========

    /**
     * Create a new watch party.
     */
    public WatchParty createParty(Long hostId, String title, String description,
                                   Instant scheduledAt, Long movieId,
                                   Integer maxParticipants, boolean isPublic) {
        User host = userRepository.findById(hostId)
            .orElseThrow(() -> new IllegalArgumentException("Host not found"));

        WatchParty party = new WatchParty();
        party.setHost(host);
        party.setTitle(title);
        party.setDescription(description);
        party.setScheduledAt(scheduledAt);
        party.setStatus(PartyStatus.SCHEDULED);
        party.setPublic(isPublic);

        if (maxParticipants != null) {
            party.setMaxParticipants(maxParticipants);
        }

        if (movieId != null) {
            Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
            party.setMovie(movie);
        }

        party = partyRepository.save(party);

        // Host automatically joins as a participant
        WatchPartyParticipant hostParticipant = new WatchPartyParticipant();
        hostParticipant.setParty(party);
        hostParticipant.setUser(host);
        hostParticipant.setStatus(ParticipantStatus.JOINED);
        hostParticipant.setJoinedAt(Instant.now());
        participantRepository.save(hostParticipant);

        return party;
    }

    /**
     * Get a party by ID.
     */
    @Transactional(readOnly = true)
    public Optional<WatchParty> findById(Long partyId) {
        return partyRepository.findById(partyId);
    }

    /**
     * Update party details.
     */
    public WatchParty updateParty(Long partyId, Long userId, String title,
                                   String description, Instant scheduledAt,
                                   Integer maxParticipants, Boolean isPublic) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Only the host can update the party");
        }

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Cannot update a party that has started or ended");
        }

        if (title != null) party.setTitle(title);
        if (description != null) party.setDescription(description);
        if (scheduledAt != null) party.setScheduledAt(scheduledAt);
        if (maxParticipants != null) party.setMaxParticipants(maxParticipants);
        if (isPublic != null) party.setPublic(isPublic);

        return partyRepository.save(party);
    }

    /**
     * Cancel a party.
     */
    public WatchParty cancelParty(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Only the host can cancel the party");
        }

        if (party.getStatus() == PartyStatus.ENDED || party.getStatus() == PartyStatus.CANCELLED) {
            throw new IllegalStateException("Party is already ended or cancelled");
        }

        party.setStatus(PartyStatus.CANCELLED);
        return partyRepository.save(party);
    }

    /**
     * Start a party (transition to LIVE).
     */
    public WatchParty startParty(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Only the host can start the party");
        }

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Party must be in SCHEDULED status to start");
        }

        party.setStatus(PartyStatus.LIVE);
        party.setStartedAt(Instant.now());

        // If no movie was set, use the top voted suggestion
        if (party.getMovie() == null) {
            List<WatchPartyMovieSuggestion> suggestions =
                suggestionRepository.findTopSuggestionForParty(partyId);
            if (!suggestions.isEmpty()) {
                party.setMovie(suggestions.getFirst().getMovie());
            }
        }

        return partyRepository.save(party);
    }

    /**
     * End a party.
     */
    public WatchParty endParty(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Only the host can end the party");
        }

        if (party.getStatus() != PartyStatus.LIVE) {
            throw new IllegalStateException("Party must be LIVE to end");
        }

        party.setStatus(PartyStatus.ENDED);
        party.setEndedAt(Instant.now());
        return partyRepository.save(party);
    }

    // ========== Party Discovery ==========

    /**
     * Get upcoming public parties.
     */
    @Transactional(readOnly = true)
    public Page<WatchParty> getUpcomingPublicParties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return partyRepository.findUpcomingPublicParties(Instant.now(), pageable);
    }

    /**
     * Get parties hosted by a user.
     */
    @Transactional(readOnly = true)
    public Page<WatchParty> getPartiesHostedBy(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledAt").descending());
        return partyRepository.findByHostId(userId, pageable);
    }

    /**
     * Get parties a user is participating in.
     */
    @Transactional(readOnly = true)
    public Page<WatchParty> getPartiesForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return partyRepository.findPartiesForParticipant(userId, pageable);
    }

    /**
     * Get live parties for a user.
     */
    @Transactional(readOnly = true)
    public List<WatchParty> getLivePartiesForUser(Long userId) {
        return partyRepository.findLivePartiesForUser(userId);
    }

    // ========== Participant Management ==========

    /**
     * Invite a user to a party.
     */
    public WatchPartyParticipant inviteUser(Long partyId, Long inviterId, Long inviteeId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(inviterId)) {
            throw new IllegalStateException("Only the host can invite users");
        }

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Can only invite to scheduled parties");
        }

        if (participantRepository.existsByPartyIdAndUserId(partyId, inviteeId)) {
            throw new IllegalStateException("User is already invited or participating");
        }

        User invitee = userRepository.findById(inviteeId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        WatchPartyParticipant participant = new WatchPartyParticipant();
        participant.setParty(party);
        participant.setUser(invitee);
        participant.setStatus(ParticipantStatus.INVITED);

        return participantRepository.save(participant);
    }

    /**
     * Join a party (accept invitation or join public party).
     */
    public WatchPartyParticipant joinParty(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (party.getStatus() != PartyStatus.SCHEDULED && party.getStatus() != PartyStatus.LIVE) {
            throw new IllegalStateException("Cannot join a cancelled or ended party");
        }

        Optional<WatchPartyParticipant> existingParticipant =
            participantRepository.findByPartyIdAndUserId(partyId, userId);

        if (existingParticipant.isPresent()) {
            WatchPartyParticipant participant = existingParticipant.get();
            if (participant.getStatus() == ParticipantStatus.JOINED) {
                throw new IllegalStateException("Already joined this party");
            }
            participant.join();
            return participantRepository.save(participant);
        }

        // New participant joining a public party
        if (!party.isPublic()) {
            throw new IllegalStateException("Cannot join a private party without an invitation");
        }

        if (party.isFull()) {
            throw new IllegalStateException("Party is full");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        WatchPartyParticipant participant = new WatchPartyParticipant();
        participant.setParty(party);
        participant.setUser(user);
        participant.join();

        return participantRepository.save(participant);
    }

    /**
     * Decline a party invitation.
     */
    public WatchPartyParticipant declineInvitation(Long partyId, Long userId) {
        WatchPartyParticipant participant = participantRepository.findByPartyIdAndUserId(partyId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (participant.getStatus() != ParticipantStatus.INVITED) {
            throw new IllegalStateException("Can only decline pending invitations");
        }

        participant.decline();
        return participantRepository.save(participant);
    }

    /**
     * Leave a party.
     */
    public WatchPartyParticipant leaveParty(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Host cannot leave their own party");
        }

        WatchPartyParticipant participant = participantRepository.findByPartyIdAndUserId(partyId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Not a participant in this party"));

        participant.leave();
        return participantRepository.save(participant);
    }

    /**
     * Get participants for a party.
     */
    @Transactional(readOnly = true)
    public List<WatchPartyParticipant> getParticipants(Long partyId) {
        return participantRepository.findByPartyId(partyId);
    }

    /**
     * Check if a user is a participant (or host) of a party.
     */
    @Transactional(readOnly = true)
    public boolean isParticipant(Long partyId, Long userId) {
        WatchParty party = partyRepository.findById(partyId).orElse(null);
        if (party == null) return false;
        if (party.getHost().getId().equals(userId)) return true;
        return participantRepository.existsByPartyIdAndUserId(partyId, userId);
    }

    // ========== Movie Suggestions & Voting ==========

    /**
     * Suggest a movie for the party.
     */
    public WatchPartyMovieSuggestion suggestMovie(Long partyId, Long userId, Long movieId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Can only suggest movies for scheduled parties");
        }

        if (party.getMovie() != null) {
            throw new IllegalStateException("Party already has a movie set");
        }

        if (!isParticipant(partyId, userId)) {
            throw new IllegalStateException("Only participants can suggest movies");
        }

        if (suggestionRepository.existsByPartyIdAndMovieId(partyId, movieId)) {
            throw new IllegalStateException("Movie has already been suggested");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        WatchPartyMovieSuggestion suggestion = new WatchPartyMovieSuggestion();
        suggestion.setParty(party);
        suggestion.setMovie(movie);
        suggestion.setSuggestedBy(user);
        suggestion.setVoteCount(1); // Suggester's vote counts

        return suggestionRepository.save(suggestion);
    }

    /**
     * Vote for a movie suggestion.
     */
    public WatchPartyMovieSuggestion voteForMovie(Long partyId, Long userId, Long movieId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Can only vote for scheduled parties");
        }

        WatchPartyParticipant participant = participantRepository.findByPartyIdAndUserId(partyId, userId)
            .orElseThrow(() -> new IllegalStateException("Only participants can vote"));

        if (participant.getStatus() != ParticipantStatus.JOINED) {
            throw new IllegalStateException("Must join the party to vote");
        }

        WatchPartyMovieSuggestion suggestion = suggestionRepository.findByPartyIdAndMovieId(partyId, movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie suggestion not found"));

        // Remove vote from previous movie if any
        if (participant.getVotedMovie() != null && !participant.getVotedMovie().getId().equals(movieId)) {
            suggestionRepository.findByPartyIdAndMovieId(partyId, participant.getVotedMovie().getId())
                .ifPresent(oldSuggestion -> {
                    oldSuggestion.decrementVoteCount();
                    suggestionRepository.save(oldSuggestion);
                });
        }

        // Add vote to new movie
        if (participant.getVotedMovie() == null || !participant.getVotedMovie().getId().equals(movieId)) {
            suggestion.incrementVoteCount();
            participant.setVotedMovie(suggestion.getMovie());
            participantRepository.save(participant);
        }

        return suggestionRepository.save(suggestion);
    }

    /**
     * Get movie suggestions for a party.
     */
    @Transactional(readOnly = true)
    public List<WatchPartyMovieSuggestion> getMovieSuggestions(Long partyId) {
        return suggestionRepository.findByPartyIdOrderByVoteCountDesc(partyId);
    }

    /**
     * Set the final movie for the party (host only).
     */
    public WatchParty setPartyMovie(Long partyId, Long userId, Long movieId) {
        WatchParty party = partyRepository.findById(partyId)
            .orElseThrow(() -> new IllegalArgumentException("Party not found"));

        if (!party.getHost().getId().equals(userId)) {
            throw new IllegalStateException("Only the host can set the movie");
        }

        if (party.getStatus() != PartyStatus.SCHEDULED) {
            throw new IllegalStateException("Can only set movie for scheduled parties");
        }

        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        party.setMovie(movie);
        return partyRepository.save(party);
    }
}
