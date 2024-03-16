package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.game.Board;
import model.game.Corporation;
import org.junit.jupiter.api.Test;

import model.tools.Point;

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

        testBoard.replaceCellCorporation(testBoard.getCell(8, 0), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(8, 1), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(8, 2), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(8, 3), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(8, 4), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 0), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 1), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 2), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 3), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 4), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 5), Corporation.FESTIVAL);
        testBoard.replaceCellCorporation(testBoard.getCell(7, 6), Corporation.FESTIVAL);

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
        Board c = new Board();

        assertEquals(b.getCorporationSize(Corporation.AMERICAN), 12);
        assertEquals(b.getCorporationSize(Corporation.FESTIVAL), 12);
        assertEquals(b.getCorporationSize(Corporation.IMPERIAL), 3);
        assertEquals(b.getCorporationSize(Corporation.CONTINENTAL), 2);
        assertEquals(b.getCorporationSize(Corporation.TOWER), 0);

        for (Corporation corporation : Corporation.values()) {
            assertEquals(c.getCorporationSize(corporation), 0);
        }
    }

    @Test
    public void replaceCellCorporationTest() {
        Board b = new Board();

        b.replaceCellCorporation(b.getCell(0, 0), Corporation.FESTIVAL);
        assertEquals(b.getCorporationSize(Corporation.FESTIVAL), 1);
    }

    @Test
    public void removeFromRemainingStocksTest() {
        Board b = new Board();

        b.removeFromRemainingStocks(Corporation.AMERICAN, 25);
        assertTrue(b.enoughRemainingStocks(Corporation.AMERICAN, 0));
    }

    @Test
    public void corporationIsSafeTest() {
        Board b = boardExample();

        assertTrue(b.corporationIsSafe(Corporation.AMERICAN));
        assertTrue(b.corporationIsSafe(Corporation.FESTIVAL));
        assertFalse(b.corporationIsSafe(Corporation.IMPERIAL));
        assertFalse(b.corporationIsSafe(Corporation.TOWER));
    }

    @Test
    public void adjacentCellsTest() {
        Board b = new Board();

        Set<Point> adjacentCells1 = b.adjacentCells(new Point(0, 0), (cell) -> true);
        List<Point> veritableAdjacentCells1 = new LinkedList<>();
        veritableAdjacentCells1.add(new Point(0, 1));
        veritableAdjacentCells1.add(new Point(1, 0));

        Set<Point> adjacentCells2 = b.adjacentCells(new Point(2, 2), (cell) -> true);
        Set<Point> veritableAdjacentCells2 = new HashSet<>();
        veritableAdjacentCells2.add(new Point(2, 1));
        veritableAdjacentCells2.add(new Point(1, 2));
        veritableAdjacentCells2.add(new Point(2, 3));
        veritableAdjacentCells2.add(new Point(3, 2));

        // TODO : Sets equality should be tested here
    }

    @Test
    public void replaceCorporationFromTest() {
        Board b = boardExample();

        assertEquals(b.getCorporationSize(Corporation.TOWER), 0);
        b.replaceCorporationFrom(Corporation.TOWER, new Point(0, 0));

        assertEquals(b.getCorporationSize(Corporation.AMERICAN), 0);
        assertEquals(b.getCorporationSize(Corporation.TOWER), 12);
        int calculatedCorporationSize = b.foldingDFS(Corporation.TOWER, new Point(0, 0),
                new LinkedList<>(), Integer::sum, 1);
        assertEquals(calculatedCorporationSize, 12);
    }

    @Test
    public void canPlaceInTest() {
        Board board = new Board();

        board.replaceCellCorporation(board.getCell(0, 0), Corporation.AMERICAN);
        board.replaceCellCorporation(board.getCell(0, 1), Corporation.AMERICAN);

        board.replaceCellCorporation(board.getCell(0, 3), Corporation.CONTINENTAL);
        board.replaceCellCorporation(board.getCell(0, 4), Corporation.CONTINENTAL);

        board.replaceCellCorporation(board.getCell(0, 6), Corporation.FESTIVAL);
        board.replaceCellCorporation(board.getCell(0, 7), Corporation.FESTIVAL);

        board.replaceCellCorporation(board.getCell(2, 0), Corporation.IMPERIAL);
        board.replaceCellCorporation(board.getCell(2, 1), Corporation.IMPERIAL);

        board.replaceCellCorporation(board.getCell(2, 3), Corporation.SACKSON);
        board.replaceCellCorporation(board.getCell(2, 4), Corporation.SACKSON);

        board.replaceCellCorporation(board.getCell(2, 6), Corporation.WORLDWIDE);
        board.replaceCellCorporation(board.getCell(2, 7), Corporation.WORLDWIDE);

        board.replaceCellCorporation(board.getCell(4, 6), Corporation.TOWER);
        board.replaceCellCorporation(board.getCell(4, 7), Corporation.TOWER);

        board.getCell(6, 0).setAsOccupied();
        board.getCell(6, 1).setAsOccupied();

        board.getCell(6, 3).setAsOccupied();
        board.getCell(6, 4).setAsOccupied();

        assertFalse(board.canPlaceIn(new Point(6, 2)));
        assertTrue(board.canPlaceIn(new Point(0, 2)));
        assertTrue(board.canPlaceIn(new Point(8, 8)));
    }

}
