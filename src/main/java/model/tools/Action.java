package model.tools;

import java.util.Map;
import model.game.Corporation;

public class Action {
    private final Point point;
    private final MergingChoice mergingChoice;
    private final Map<Corporation, Integer> boughtStocks;
    
    public Action(
            Point point,
            Map<Corporation, Integer> boughtStocks,
            MergingChoice mergingChoice) {
        this.point = point;
        this.boughtStocks = boughtStocks;
        this.mergingChoice = mergingChoice;
    }

    public Point getPoint() {
        return point;
    }

    public Map<Corporation, Integer> getBoughtStocks() {
        return boughtStocks;
    }

    public MergingChoice getMergingChoice() {
        return mergingChoice;
    }

    @Override
    public String toString() {
        return "(" + point + ", " + mergingChoice + boughtStocks + ")";
    }
}
