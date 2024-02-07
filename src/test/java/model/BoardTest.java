package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTest {
    
    
    @Test
    public void calculateCorporationSizeTest() {
        Board testBoard = new Board();

        testBoard.getCell(0, 0).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(0, 1).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(0, 2).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(0, 3).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(0, 4).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(1, 0).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(1, 1).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(1, 2).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(1, 3).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(1, 4).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(2, 3).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(2, 4).setCorporation(Corporation.AMERICAN);
        testBoard.getCell(2, 5).setCorporation(Corporation.AMERICAN);

        testBoard.getCell(10, 0).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(10, 1).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(10, 2).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(10, 3).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(10, 4).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(11, 0).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(11, 1).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(11, 2).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(11, 3).setCorporation(Corporation.FESTIVAL);
        testBoard.getCell(11, 4).setCorporation(Corporation.FESTIVAL);

        testBoard.getCell(0, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(1, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(2, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(3, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(4, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(5, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(6, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(7, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(8, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(9, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(10, 8).setCorporation(Corporation.TOWER);
        testBoard.getCell(11, 8).setCorporation(Corporation.TOWER);

        assertEquals(testBoard.calculateCorporationSize(Corporation.AMERICAN, new Point(0, 0)), 13);
        assertEquals(testBoard.calculateCorporationSize(Corporation.AMERICAN, new Point(2, 8)), 0);
        assertEquals(testBoard.calculateCorporationSize(Corporation.IMPERIAL, new Point(1, 8)), 0);
        assertEquals(testBoard.calculateCorporationSize(null, new Point(1, 8)), 0);
        assertEquals(testBoard.calculateCorporationSize(Corporation.FESTIVAL, new Point(10, 0)), 10);
        assertEquals(testBoard.calculateCorporationSize(Corporation.TOWER, new Point(0, 8)), 12);
    }
}
