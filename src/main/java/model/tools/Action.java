package model.tools;

import java.util.Map;
import model.game.Corporation;


public class Action {
    private Point point;
    private Map<Corporation, Integer> boughtStocks;
    private Map<Corporation, Integer> soldStocks;

    public Action(Point point, Map<Corporation, Integer> boughtStocks, Map<Corporation, Integer> soldStocks) {
        this.point = point;
        this.boughtStocks = boughtStocks;
        this.soldStocks = soldStocks;
    }

    public Point getPoint() {
        return point;
    }

    public Map<Corporation, Integer> getBoughtStocks() {
        return boughtStocks;
    }

    public Map<Corporation, Integer> getSoldStocks() {
        return soldStocks;
    }
}
