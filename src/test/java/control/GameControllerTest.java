package control;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Board;
import model.Corporation;
import model.Player;
import tools.Point;

public class GameControllerTest {

    List<Player> generateCurrentPlayers() {
        List<Player> currentPlayers = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            currentPlayers.add(new Player());
        }
        return currentPlayers;
    }

    // FIXME : same function that is used in BoardTest
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

    boolean CellsInsameDeckAreDiffrent(Point[] deck) {
        for (Point point : deck) {
            for (Point point2 : deck) {
                if (point != point2 && point.equals(point2))
                    return false;
            }
        }
        return true;
    }
    boolean twoDeckAreDiffernt(Point[] deck1, Point[] deck2) {
        for (Point point1 : deck1) {
            for (Point point2 : deck2) {
                if (point1.equals(point2))
                    return false;
            }
        }
        return true;
    }

    @Test
    void testGenerateDeck() { // Tests if the decks are generated
        GameController game = new GameController(boardExample(), null, generateCurrentPlayers());
        game.initPlayersDecks();
        boolean deckNull = false;
        for (Player player : game.getCurrentPlayers()) {
            Point[] arthurDeck = player.getDeck();
            deckNull = (arthurDeck == null) || (arthurDeck.length == 0);
        }
        assertFalse(deckNull);
    }

    @Test
    void testCellsDifferent() { // Tests if same cells in a single deck are different
        GameController game = new GameController(boardExample(), null, generateCurrentPlayers());
        game.initPlayersDecks();
        boolean cellsDifferent = true;
        for (Player player : game.getCurrentPlayers()) {
            cellsDifferent = CellsInsameDeckAreDiffrent(player.getDeck());
        }
        assertTrue(cellsDifferent);
    }

    @Test
    void testDecksAreDiffrent() { // Tests if all decks are different
        GameController game = new GameController(boardExample(), null, generateCurrentPlayers());
        game.initPlayersDecks();
        boolean decksAreDifferent = true;
        for (Player player1 : game.getCurrentPlayers()) {
            for (Player player2 : game.getCurrentPlayers()) {
                if (player1 != player2)
                    decksAreDifferent = twoDeckAreDiffernt(player1.getDeck(), player2.getDeck());
            }
        }
        assertTrue(decksAreDifferent);

    }
        
}
