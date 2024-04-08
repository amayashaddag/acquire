package model.tools;

/**
 * A record which keep the
 * information needed for join
 * the PreGame, when the player is
 * the Menu.
 *
 * @author Arthur Deck
 * @version 1
 */
public record PreGameAnalytics(
        String hostName,
        String gameUID,
        int currentNumberOfPlayer,
        int maxNumberOfPlayer) {
}
