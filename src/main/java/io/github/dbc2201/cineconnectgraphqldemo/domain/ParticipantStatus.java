package io.github.dbc2201.cineconnectgraphqldemo.domain;

/**
 * Status of a participant in a watch party.
 */
public enum ParticipantStatus {
    /**
     * User has been invited but hasn't responded yet.
     */
    INVITED,

    /**
     * User has accepted the invitation and joined the party.
     */
    JOINED,

    /**
     * User has declined the invitation.
     */
    DECLINED,

    /**
     * User joined but later left the party.
     */
    LEFT
}
