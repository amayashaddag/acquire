package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class BoardTest {
    @Test
    public void testMethod() {
        assertTrue(true);
    }

    public Board boardExample() {
        Board testBoard = new Board();

        testBoard.replaceCellCorporation(testBoard.getCell(0, 0), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(0, 1), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(0, 2), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(0, 3), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(0, 4), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(1, 0), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(1, 1), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(1, 2), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(1, 3), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(1, 4), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(2, 4), Corporation.AMERICAN);
        testBoard.replaceCellCorporation(testBoard.getCell(2, 5), Corporation.AMERICAN);

        testBoard.replaceCellCorporation(testBoard.getCell(10, 0), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(10, 1), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(10, 2), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(10, 3), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(10, 4), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 0), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 1), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 2), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 3), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 4), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 5), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(11, 6), Corporation.FESTIVAL);

        testBoard.replaceCellCorporation(testBoard.getCell(3, 6), Corporation.IMPERIAL);
        testBoard.replaceCellCorporation(testBoard.getCell(4, 6), Corporation.IMPERIAL);
        testBoard.replaceCellCorporation(testBoard.getCell(5, 6), Corporation.IMPERIAL);

        testBoard.replaceCellCorporation(testBoard.getCell(2, 7), Corporation.CONTINENTAL);
        testBoard.replaceCellCorporation(testBoard.getCell(2, 8), Corporation.CONTINENTAL);
        
        return testBoard;
    }

    @Test
    public void getCorporationSizeTest() {
        Board b = boardExample();

        assertEquals(b.getCorporationSize(Corporation.AMERICAN), 12);
        assertEquals(b.getCorporationSize(Corporation.FESTIVAL), 12);
    }


}
