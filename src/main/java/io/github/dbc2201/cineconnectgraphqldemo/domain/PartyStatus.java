package io.github.dbc2201.cineconnectgraphqldemo.domain;

/**
 * Status of a watch party.
 */
public enum PartyStatus {
    /**
     * Party is scheduled for a future time.
     */
    SCHEDULED,

    /**
     * Party is currently live and in progress.
     */
    LIVE,

    /**
     * Party has ended normally.
     */
    ENDED,

    /**
     * Party was cancelled by the host.
     */
    CANCELLED
}
