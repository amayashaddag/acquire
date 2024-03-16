package model;

import static org.junit.jupiter.api.Assertions.*;

import model.game.Corporation;
import model.game.ReferenceChart;
import org.junit.jupiter.api.Test;

public class ReferenceChartTest {
    @Test
    public void getStockPriceTest() {
        assertEquals(ReferenceChart.getStockPrice(Corporation.WORLDWIDE, 2), 200);
        assertEquals(ReferenceChart.getStockPrice(Corporation.SACKSON, 3), 300);
        assertEquals(ReferenceChart.getStockPrice(Corporation.SACKSON, 42), 1000);

        assertEquals(ReferenceChart.getStockPrice(Corporation.FESTIVAL, 2), 300);
        assertEquals(ReferenceChart.getStockPrice(Corporation.IMPERIAL, 3), 400);
        assertEquals(ReferenceChart.getStockPrice(Corporation.AMERICAN, 42), 1100);

        assertEquals(ReferenceChart.getStockPrice(Corporation.CONTINENTAL, 2), 400);
        assertEquals(ReferenceChart.getStockPrice(Corporation.TOWER, 3), 500);
        assertEquals(ReferenceChart.getStockPrice(Corporation.TOWER, 42), 1200);
    }

    @Test
    public void getMajorityShareholdTest() {
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.WORLDWIDE, 2), 2000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.SACKSON, 3), 3000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.SACKSON, 42), 10000);

        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.FESTIVAL, 2), 3000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.IMPERIAL, 3), 4000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.AMERICAN, 42), 11000);

        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.CONTINENTAL, 2), 4000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.TOWER, 3), 5000);
        assertEquals(ReferenceChart.getMajoritySharehold(Corporation.TOWER, 42), 12000);
    }

    @Test
    public void getMinorityShareholdTest() {
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.WORLDWIDE, 2), 1000);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.SACKSON, 3), 1500);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.SACKSON, 42), 5000);

        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.FESTIVAL, 2), 1500);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.IMPERIAL, 3), 2000);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.AMERICAN, 42), 5500);

        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.CONTINENTAL, 2), 2000);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.TOWER, 3), 2500);
        assertEquals(ReferenceChart.getMinoritySharehold(Corporation.TOWER, 42), 6000);
    }
}
