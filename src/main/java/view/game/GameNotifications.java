package view.game;

import model.game.Corporation;

public class GameNotifications {
    /* Error notifications */
    public final static String NOT_ENOUGH_REMAINING_STOCKS_IN_BANK = "There is not enough remaining stocks.";
    public final static String NOT_ENOUGH_CASH = "You don't have enough cash to buy this.";
    public final static String NOT_ENOUGH_STOCKS_PLAYER = "You don't have enough stocks.";

    public final static String CANNOT_BUY_MORE_THAN_THREE = "You can't by more than 3 actions.";

    /* Success notifications */
    public static String boughtStocksNotification(int amount, Corporation corporation) {
        return "You successfully bought " + amount + " stocks of the company " + corporation + ".";
    }

    public static String soldStocksNotification(int amount, Corporation corporation, int moneyGained) {
        return "You successfully sold " + amount + " stocks of the company " + corporation
                + " for " + moneyGained + "$.";
    }

    public static String tradedStocksNotification(int amountGiven, Corporation corporation, int amountEarned, Corporation major) {
        return "You successfully traded " + amountGiven + " stocks of the company " + corporation
                + " for " + amountEarned + " stocks of " + major + ".";
    }

    public static String playerTurnNotification(String pseudo) {
        return pseudo + " is about to play.";
    }

    public static String cellPlacingNotification(String pseudo) {
        return pseudo + " placed a cell.";
    }

    public static String corporationMergingNotification(String pseudo, Corporation corporation) {
        return pseudo + " merged corporations into " + corporation + ".";
    }

    public static String corporationFoundingNotification(String pseudo, Corporation corporation) {
        return pseudo + " founded the corporation " + corporation + ".";
    }

}
