package view.game;

import model.Corporation;
import tools.Point;

public class GameNotifications {
    /* Error notifications */
    public final static String NOT_ENOUGH_REMAINING_STOCKS_IN_BANK = "There is not enough remaining stocks.";
    public final static String NOT_ENOUGH_CASH = "You don't have enough cash to buy this.";
    public final static String NOT_ENOUGH_STOCKS_PLAYER = "You don't have enough stocks.";

    /* Success notifications */
    public static String successfullyBoughtStocks(int amount, Corporation corporation) {
        return "You successfully bought " + amount + " stocks of the company " + corporation + ".";
    }

    public static String successfullySoldStocks(int amount, Corporation corporation, int moneyGained) {
        return "You successfully sold " + amount + " stocks of the company " + corporation
                + " for " + moneyGained + "$.";
    }

    public static String playerTurnNotifying(String pseudo) {
        return pseudo + " is about to play.";
    }

    public static String cellPlacingNotification(String pseudo, Point cellPosition) {
        return pseudo + " placed a cell at " + cellPosition + ".";
    }

}
