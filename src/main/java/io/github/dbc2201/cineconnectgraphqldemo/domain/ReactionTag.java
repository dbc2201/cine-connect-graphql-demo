package io.github.dbc2201.cineconnectgraphqldemo.domain;

/**
 * Reaction tags that users can add to their reviews.
 * These provide quick, standardized feedback beyond numeric ratings.
 */
public enum ReactionTag {
    MIND_BENDING("Mind Bending", "Complex, thought-provoking"),
    PLOT_TWIST("Plot Twist!", "Unexpected turns"),
    UNDERRATED_GEM("Underrated Gem", "Deserves more attention"),
    COMFORT_WATCH("Comfort Watch", "Cozy, feel-good vibes"),
    REWATCHABLE("Rewatchable", "Worth seeing again"),
    NOT_WORTH_HYPE("Not Worth Hype", "Overhyped"),
    TEARJERKER("Tearjerker", "Emotionally powerful"),
    CROWD_PLEASER("Crowd Pleaser", "Great for groups"),
    SLOW_BURN("Slow Burn", "Takes time to develop"),
    VISUAL_MASTERPIECE("Visual Masterpiece", "Stunning cinematography"),
    GREAT_SOUNDTRACK("Great Soundtrack", "Memorable music"),
    STRONG_PERFORMANCE("Strong Performance", "Outstanding acting");

    private final String displayName;
    private final String description;

    ReactionTag(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
