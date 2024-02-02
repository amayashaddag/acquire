package model;

import java.util.HashMap;

public class Player {
    private int cash;
    private HashMap<Corporation,Integer> earnedStocks;
    private Point[] deck;

    public HashMap<Corporation,Integer> getEarnedStocks(){
        return this.earnedStocks;
    }
    public Point[] getDeck(){
        return this.deck;
    }
    public void setDeck(Point[] newDeck){
        this.deck = newDeck;
    }
    public boolean hasEnoughCash(int amount){
        return this.cash >= amount;
    }
    public void addToCash(int amount){
        this.cash+=amount;
    }
    public void removeFromCash(int amount){
            this.cash-=amount;
    }
    public boolean hasEnoughStocks(Corporation corp,int amount){
        return earnedStocks.get(corp) >= amount;
    }
    public void addToEarnedStocks(Corporation corp,int amount){
        this.earnedStocks.put(corp, amount);
    }
    public void removeToEarnedStocks(Corporation corp,int amount){
        this.cash-=amount;
    }
    public Point getCell(int index){
        return this.deck[index];
    }
}
