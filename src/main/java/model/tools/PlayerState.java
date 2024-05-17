package model.tools;

import java.util.HashMap;
import java.util.Map;

import model.game.Corporation;

public class PlayerState {
    private final String pseudo;
    private final int currentNet;
    private final int currentCash;
    private final Map<Corporation, Integer> currentStocks;

    public PlayerState(String pseudo, int currentNet, int currentCash, Map<Corporation, Integer> currentStocks) {
        this.pseudo = pseudo;
        this.currentNet = currentNet;
        this.currentCash = currentCash;
        this.currentStocks = new HashMap<>(currentStocks);
    }

    public int getCurrentCash() {
        return currentCash;
    }

    public int getCurrentNet() {
        return currentNet;
    }

    public Map<Corporation, Integer> getCurrentStocks() {
        return currentStocks;
    }

    public String getPseudo() {
        return pseudo;
    }

    @Override
    public String toString() {
        return "(" + pseudo + ", " + currentNet + ", " + currentCash + ", " + currentStocks + ")";
    }
}
