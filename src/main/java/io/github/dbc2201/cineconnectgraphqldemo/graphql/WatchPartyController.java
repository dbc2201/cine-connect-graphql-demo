package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.domain.*;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.CreateWatchPartyInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.UpdateWatchPartyInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.PageInfo;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.WatchPartyConnection;
import io.github.dbc2201.cineconnectgraphqldemo.security.CineConnectUserDetailsService.CineConnectUserPrincipal;
import io.github.dbc2201.cineconnectgraphqldemo.service.WatchPartyService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL controller for watch party operations.
 */
@Controller
public class WatchPartyController {

    private final WatchPartyService watchPartyService;

    public WatchPartyController(WatchPartyService watchPartyService) {
        this.watchPartyService = watchPartyService;
    }

    // ========== Queries ==========

    @QueryMapping
    public WatchParty watchParty(@Argument Long id) {
        return watchPartyService.findById(id).orElse(null);
    }

    @QueryMapping
    public WatchPartyConnection upcomingParties(@Argument Integer page,
                                                 @Argument Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<WatchParty> parties = watchPartyService.getUpcomingPublicParties(pageNum, pageSize);
        return toWatchPartyConnection(parties);
    }

    @QueryMapping
    public WatchPartyConnection myHostedParties(@Argument Integer page,
                                                 @Argument Integer size,
                                                 @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<WatchParty> parties = watchPartyService.getPartiesHostedBy(principal.getId(), pageNum, pageSize);
        return toWatchPartyConnection(parties);
    }

    @QueryMapping
    public WatchPartyConnection myParties(@Argument Integer page,
                                           @Argument Integer size,
                                           @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        Page<WatchParty> parties = watchPartyService.getPartiesForUser(principal.getId(), pageNum, pageSize);
        return toWatchPartyConnection(parties);
    }

    @QueryMapping
    public List<WatchParty> myLiveParties(@AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.getLivePartiesForUser(principal.getId());
    }

    // ========== Mutations ==========

    @MutationMapping
    public WatchParty createWatchParty(@Argument CreateWatchPartyInput input,
                                        @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }

        Boolean isPublic = input.isPublic() != null ? input.isPublic() : false;

        return watchPartyService.createParty(
            principal.getId(),
            input.title(),
            input.description(),
            input.scheduledAt(),
            input.movieId(),
            input.maxParticipants(),
            isPublic
        );
    }

    @MutationMapping
    public WatchParty updateWatchParty(@Argument Long partyId,
                                        @Argument UpdateWatchPartyInput input,
                                        @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }

        return watchPartyService.updateParty(
            partyId,
            principal.getId(),
            input.title(),
            input.description(),
            input.scheduledAt(),
            input.maxParticipants(),
            input.isPublic()
        );
    }

    @MutationMapping
    public WatchParty cancelWatchParty(@Argument Long partyId,
                                        @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.cancelParty(partyId, principal.getId());
    }

    @MutationMapping
    public WatchParty startWatchParty(@Argument Long partyId,
                                       @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.startParty(partyId, principal.getId());
    }

    @MutationMapping
    public WatchParty endWatchParty(@Argument Long partyId,
                                     @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.endParty(partyId, principal.getId());
    }

    @MutationMapping
    public WatchPartyParticipant inviteToParty(@Argument Long partyId,
                                                @Argument Long userId,
                                                @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.inviteUser(partyId, principal.getId(), userId);
    }

    @MutationMapping
    public WatchPartyParticipant joinWatchParty(@Argument Long partyId,
                                                 @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.joinParty(partyId, principal.getId());
    }

    @MutationMapping
    public WatchPartyParticipant declinePartyInvitation(@Argument Long partyId,
                                                         @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.declineInvitation(partyId, principal.getId());
    }

    @MutationMapping
    public WatchPartyParticipant leaveWatchParty(@Argument Long partyId,
                                                  @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.leaveParty(partyId, principal.getId());
    }

    @MutationMapping
    public WatchPartyMovieSuggestion suggestMovie(@Argument Long partyId,
                                                   @Argument Long movieId,
                                                   @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.suggestMovie(partyId, principal.getId(), movieId);
    }

    @MutationMapping
    public WatchPartyMovieSuggestion voteForMovie(@Argument Long partyId,
                                                   @Argument Long movieId,
                                                   @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.voteForMovie(partyId, principal.getId(), movieId);
    }

    @MutationMapping
    public WatchParty setPartyMovie(@Argument Long partyId,
                                     @Argument Long movieId,
                                     @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentication required");
        }
        return watchPartyService.setPartyMovie(partyId, principal.getId(), movieId);
    }

    // ========== Field Resolvers for WatchParty ==========

    @SchemaMapping(typeName = "WatchParty", field = "participantCount")
    public int participantCount(WatchParty party) {
        return party.getParticipantCount();
    }

    @SchemaMapping(typeName = "WatchParty", field = "isFull")
    public boolean isFull(WatchParty party) {
        return party.isFull();
    }

    @SchemaMapping(typeName = "WatchParty", field = "participants")
    public List<WatchPartyParticipant> participants(WatchParty party) {
        return watchPartyService.getParticipants(party.getId());
    }

    @SchemaMapping(typeName = "WatchParty", field = "movieSuggestions")
    public List<WatchPartyMovieSuggestion> movieSuggestions(WatchParty party) {
        return watchPartyService.getMovieSuggestions(party.getId());
    }

    @SchemaMapping(typeName = "WatchParty", field = "isHost")
    public Boolean isHost(WatchParty party,
                          @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return party.getHost().getId().equals(principal.getId());
    }

    @SchemaMapping(typeName = "WatchParty", field = "isParticipant")
    public Boolean isParticipant(WatchParty party,
                                  @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return watchPartyService.isParticipant(party.getId(), principal.getId());
    }

    @SchemaMapping(typeName = "WatchParty", field = "myParticipation")
    public WatchPartyParticipant myParticipation(WatchParty party,
                                                  @AuthenticationPrincipal CineConnectUserPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return party.getParticipants().stream()
            .filter(p -> p.getUser().getId().equals(principal.getId()))
            .findFirst()
            .orElse(null);
    }

    // ========== Helper Methods ==========

    private WatchPartyConnection toWatchPartyConnection(Page<WatchParty> page) {
        PageInfo pageInfo = new PageInfo(
            page.hasNext(),
            page.hasPrevious(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );
        return new WatchPartyConnection(page.getContent(), pageInfo);
    }
}
