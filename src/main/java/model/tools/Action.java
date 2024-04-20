package model.tools;

import java.util.Map;
import model.game.Corporation;

public class Action {
    private final Point point;
    private final Corporation createdCorporation;
    private final MergingChoice mergingChoice;
    private final Map<Corporation, Integer> boughtStocks;
    
    public Action(
            Point point,
            Corporation createdCorporation,
            Map<Corporation, Integer> boughtStocks,
            MergingChoice mergingChoice) {
        this.point = point;
        this.createdCorporation = createdCorporation;
        this.boughtStocks = boughtStocks;
        this.mergingChoice = mergingChoice;
    }

    public Point getPoint() {
        return point;
    }

    public Corporation getCreatedCorporation() {
        return createdCorporation;
    }

    public Map<Corporation, Integer> getBoughtStocks() {
        return boughtStocks;
    }

    public MergingChoice getMergingChoice() {
        return mergingChoice;
    }

    @Override
    public String toString() {
        return "(" + point + ", " + mergingChoice + ", " + boughtStocks + ")";
    }
}
