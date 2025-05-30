package model.game;

import java.util.HashMap;
/** @author Igor Ait Ali Braham
 * @version 1.0
 */

import model.tools.Point;

public class Player {

    public static enum PlayerType {
        HUMAN,
        BOT
    }

    private int cash;
    private int net;
    private HashMap<Corporation, Integer> earnedStocks;
    private volatile Point[] deck;
    private final PlayerType playerType;
    private final String pseudo;
    private final String uid;

    public static final int INITIAL_CASH = 6000;
    public static final int INITIAL_NET = INITIAL_CASH;
    private static int botNumber = 0;
    private final static String botPseudoPrefix = "Bot";

    private Player(PlayerType playerType, String pseudo, String uid) {
        this.cash = this.net = INITIAL_CASH;
        this.earnedStocks = initEarnedStocks();
        this.deck = new Point[Board.DECK_SIZE];
        this.pseudo = pseudo;
        this.playerType = playerType;
        this.uid = uid;
    }

    public HashMap<Corporation, Integer> initEarnedStocks() {
        HashMap<Corporation, Integer> initialEarnedStocks = new HashMap<>();

        for (Corporation c : Corporation.values()) {
            initialEarnedStocks.put(c, 0);
        }

        return initialEarnedStocks;
    }

    public String getPseudo() {
        return pseudo;
    }

    /**
     * getter for player's earnedStock
     *
     * @return player's Stocks (HashMap<Corporation,Integer>)
     */
    public HashMap<Corporation, Integer> getEarnedStocks() {
        return this.earnedStocks;
    }

    /**
     * getter for player's deck
     *
     * @return player's deck (Point[])
     */
    public Point[] getDeck() {
        return this.deck;
    }

    public int getNet() {
        return net;
    }

    public String getUID() {
        return uid;
    }

    public void setNet(int net) {
        this.net = net;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    /**
     * change player's deck with the deck in argument
     *
     * @param newDeck of the player type: Point[]
     */
    public void setDeck(Point[] newDeck) {
        this.deck = newDeck;
    }

    /**
     * Test if the player has enough cash
     *
     * @param amount of money needed for next operation
     * @return boolean true if the player has enough cash else false
     */
    public boolean hasEnoughCash(int amount) {
        return this.cash >= amount;
    }

    /**
     * add to player's money the amount in argument
     *
     * @param amount of money to add to player's cash
     */
    public void addToCash(int amount) {
        this.cash += amount;
    }

    public void setStocks(Corporation c, int amount) {
        earnedStocks.put(c, amount);
    }

    public void addToNet(int amount) {
        this.net += amount;
    }

    public void removeFromNet(int amount) {
        this.net -= amount;
    }

    /**
     * withdraw player's money the amount in argument
     *
     * @param amount of money to withdraw to player's cash
     */
    public void removeFromCash(int amount) {
        this.cash -= amount;
    }

    /**
     * Test if the player has enough stocks for next operations
     *
     * @return boolean true if the player has enough stocks else false
     * @param1 the corporation in which we want to check how much stocks we have
     * @param2 amount of stocks we want to test
     */
    public boolean hasEnoughStocks(Corporation corp, int amount) {
        return earnedStocks.get(corp) >= amount;
    }

    /**
     * add to player's corporation gave in argument a number of stocks gave in
     * argument to
     *
     * @param1 corporation in which add stocks
     * @param2 number of stocks to add
     */
    public void addToEarnedStocks(Corporation corp, int amount) {
        int oldAmountOfEarnedStocks = earnedStocks.get(corp);
        this.earnedStocks.put(corp, amount + oldAmountOfEarnedStocks);
    }

    /**
     * remove to player's corporation gave in argument a number of stocks gave in
     * argument to
     *
     * @param1 corporation in which remove stocks
     * @param2 number of stocks to remove
     */
    public void removeFromEarnedStocks(Corporation corp, int amount) {
        int oldAmountOfEarnedStocks = earnedStocks.get(corp);
        this.earnedStocks.put(corp, oldAmountOfEarnedStocks - amount);
    }

    /**
     * get the Point of player's deck at index gave in argument
     *
     * @param index of the point to get in the deck
     * @return position in the map of deck[index]
     */
    public Point getCell(int index) {
        Point cellPosition = this.deck[index];
        this.deck[index] = null;

        return cellPosition;
    }

    public boolean isHuman() {
        return playerType == PlayerType.HUMAN;
    }

    public boolean isBot() {
        return playerType == PlayerType.BOT;
    }

    public static Player createHumanPlayer(String pseudo, String uid) {
        return new Player(PlayerType.HUMAN, pseudo, uid);
    }

    public static Player createBotPlayer() {
        String uid = botPseudoPrefix + (botNumber++);
        return new Player(PlayerType.BOT, uid, uid);
    }

    public int getCash() {
        return cash;
    }

    public boolean ownsStocksFromCorporation(Corporation c) {
        return hasEnoughStocks(c, 1);
    }

    public int getStocks(Corporation c) {
        return earnedStocks.get(c);
    }

    public boolean isEmptyDeck() {
        for (Point p : deck) {
            if (p != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Player clonedPlayer = new Player(this.playerType, this.pseudo, null);
        clonedPlayer.cash = this.cash;
        clonedPlayer.net = this.net;
        clonedPlayer.earnedStocks = new HashMap<>(this.earnedStocks);

        clonedPlayer.deck = new Point[this.deck.length];
        for (int i = 0; i < this.deck.length; i++) {
            if (this.deck[i] != null) {
                clonedPlayer.deck[i] = new Point(this.deck[i].getX(), this.deck[i].getY());
            }
        }

        return clonedPlayer;
    }

}
