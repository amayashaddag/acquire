package model;

import java.util.HashMap;
/* @author Igor Ait Ali Braham
 * @version 1.0
 */

public class Player {
    private int cash;
    private HashMap<Corporation,Integer> earnedStocks;
    private Point[] deck;
     
    /**
     * 
     * getter for player's earnedStock
     * 
     * @return player's Stocks (HashMap<Corporation,Integer>)
     */
    public HashMap<Corporation,Integer> getEarnedStocks(){
        return this.earnedStocks;
    }
    /**
     * 
     * getter for player's deck
     * 
     * @return player's deck (Point[])
     */
    public Point[] getDeck(){
        return this.deck;
    }
    /**
     * change player's deck with the deck in argument
     * 
     * @param new deck of the player type: Point[]
     */
    public void setDeck(Point[] newDeck){
        this.deck = newDeck;
    }
    /**
     * Test if the player has enough cash
     * 
     * @param amount of money needed for next operation
     * @return boolean true if the player has enough cash else false
     */
    public boolean hasEnoughCash(int amount){
        return this.cash >= amount;
    }
    /**
     * add to player's money the amount in argument
     * 
     * @param amount of money to add to player's cash
     */
    public void addToCash(int amount){
        this.cash+=amount;
    }
    /**
     * withdraw player's money the amount in argument
     * 
     * @param amount of money to withdraw to player's cash
     */
    public void removeFromCash(int amount){
            this.cash-=amount;
    }
    /**
     * Test if the player has enough stocks for next operations
     * 
     * @param1 the corporation in which we want to check how much stocks we have
     * @param2 amount of stocks we want to test
     * @return boolean true if the player has enough stocks else false
     */
    public boolean hasEnoughStocks(Corporation corp,int amount){
        return earnedStocks.get(corp) >= amount;
    }
    /** add to player's corporation gave in argument a number of stocks gave in argument to 
     * 
     * @param1 corporation in which add stocks
     * @param2 number of stocks to add
     */
    public void addToEarnedStocks(Corporation corp,int amount){
        this.earnedStocks.put(corp, amount);
    }
    /**remove to player's corporation gave in argument a number of stocks gave in argument to 
     * 
     * @param1 corporation in which remove stocks
     * @param2 number of stocks to remove
     */
    public void removeFromEarnedStocks(Corporation corp,int amount){
        this.cash-=amount;
    }
    /**
     * get the Point of player's deck at index gave in argument
     * 
     * @param position of the point to get in tghe deck
     * @return position in the map of deck[index]
     */
    public Point getCell(int index){
        return this.deck[index];
    }
}
