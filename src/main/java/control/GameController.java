package control;

import java.util.LinkedList;
import java.util.List;

import model.Board;
import model.Cell;
import model.Corporation;
import model.Player;
import tools.Point;
import view.GameView;

public class GameController {
    private Board board;
    private GameView gameView;
    private List<Player> currentPlayers;

    private int playerTurnIndex;
    private boolean gameOver;

    public GameController(Board board, GameView gameView, List<Player> currentPlayers) {
        this.board = board;
        this.gameView = gameView;
        this.currentPlayers = currentPlayers;
    }

    public GameView getGameView() {
        return gameView;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPlayerTurnIndex() {
        return playerTurnIndex;
    }

    /**
     * This function is used at the beginning of the game (where it is already
     * supposed that there is enough board cells for everyone) to initialize players
     * decks
     */
    public void initPlayersDecks() {
        for (Player player : currentPlayers) {
            Point[] deck = board.generatePlayerDeck();
            player.setDeck(deck);
        }
    }

    /**
     * Processes the buying stocks action by testing the possibility then removing
     * from player's cash
     * 
     * @param player
     * @param corporation the corporation the player wants to buy stocks from
     * @param amount      the number of stocks the player wants to buy
     */
    public void buyStocks(Player player, Corporation corporation, int amount) {
        if (!board.enoughRemainingStocks(corporation, amount)) {
            // TODO : display message telling that there is not enough remaining stocks
            return;
        }
        int unityStockPrice = board.getStockPrice(corporation);
        int amountToPay = unityStockPrice * amount;

        if (!player.hasEnoughCash(amountToPay)) {
            // TODO : display message you don't have enough cash
            return;
        }

        board.removeFromRemainingStocks(corporation, amount);
        player.addToEarnedStocks(corporation, amount);
        player.removeFromCash(amountToPay);
    }

    /**
     * This functions sells the player's holden stocks to the bank
     * 
     * @param player
     * @param corporation
     * @param amount
     */
    public void sellStocks(Player player, Corporation corporation, int amount) {
        if (!player.hasEnoughStocks(corporation, amount)) {
            // TODO : print a don't have enough stocks message
            return;
        }

        int unityStockPrice = board.getStockPrice(corporation);
        int amountToEarn = unityStockPrice * amount;

        board.addToRemainingStocks(corporation, amountToEarn);
        player.removeFromEarnedStocks(corporation, amountToEarn);
        player.addToCash(amountToEarn);
    }

    /**
     * Returns the adjacent owned cell positions to a given cell position in order
     * to
     * determine the action to perform on the cell
     * 
     * @param cellPosition
     * @return
     */
    public List<Point> adjacentOwnedCells(Point cellPosition) {
        List<Point> adjacentCells = board.adjacentCells(cellPosition);
        List<Point> cellsToRemove = new LinkedList<>();

        for (Point p : adjacentCells) {
            Cell adjacentCell = board.getCell(p);
            if (!adjacentCell.isOwned()) {
                cellsToRemove.add(p);
            }
        }
        adjacentCells.removeAll(cellsToRemove);
        return adjacentCells;
    }

    /**
     * This function is a removing sort algorithm which removes all the positions of cells that 
     * belong to non-maximal-size corporations between them
     * 
     * @param adjacentOwnedCells
     * @return
     */
    private List<Point> filterMaximalSizeCorporations(List<Point> adjacentOwnedCells) {
        int maxCorporationSize = 0;
        List<Point> maxCorporations = new LinkedList<>();

        for (Point p : adjacentOwnedCells) {
            Cell cell = board.getCell(p);
            Corporation corporation = cell.getCorporation();
            int corporationSize = board.getCorporationSize(corporation);

            if (corporationSize > maxCorporationSize) {
                maxCorporationSize = corporationSize;
            }
        }

        for (Point p : adjacentOwnedCells) {
            Cell cell = board.getCell(p);
            Corporation corporation = cell.getCorporation();
            int corporationSize = board.getCorporationSize(corporation);

            if (corporationSize == maxCorporationSize) {
                maxCorporations.add(p);
            }
        }

        return maxCorporations;
    }

    /**
     * This function is called when a merge is possible and processes it
     * It checks whether there is a unique maximum size company and merges all the
     * adjacent
     * companies to it, if there is not, it asks the player to choose one maximum
     * size company
     * 
     * @param cellsToMerge represents the set of adjacent cells to merge
     * @param cellPosition represents the position of the cell where the merge
     *                     started
     * @see #placeCell(Point)
     */
    public void mergeCorporations(List<Point> cellsToMerge, Point cellPosition) {
        List<Point> maxCorporations = filterMaximalSizeCorporations(cellsToMerge);
        Point chosenCellPosition;
        Cell chosenCell, currentCell = board.getCell(cellPosition);
        Corporation chosenCellCorporation;

        if (maxCorporations.size() == 1) {
            chosenCellPosition = maxCorporations.get(0);

        } else {
            // TODO : call the game view to request player to choose one major holder

            /* Temporary intialization */
            chosenCellPosition = maxCorporations.get(0);
        }

        chosenCell = board.getCell(chosenCellPosition);
        chosenCellCorporation = chosenCell.getCorporation();

        board.replaceCellCorporation(currentCell, chosenCellCorporation);
        for (Point adj : cellsToMerge) {
            if (!adj.equals(chosenCellPosition)) {
                board.replaceCorporationFrom(chosenCellCorporation, adj);
            }
        }
    }

    /**
     * This function places a cell in a given position and does the task of merging
     * or extending any possible hotel chain
     * 
     * @param cellPosition
     */
    public void placeCell(Point cellPosition) {
        Cell currentCell = board.getCell(cellPosition.getX(), cellPosition.getY());
        currentCell.setAsOccupied();

        List<Point> adjacentOwnedCells = adjacentOwnedCells(cellPosition);
        if (adjacentOwnedCells.size() == 0) {
            return;
        }

        if (adjacentOwnedCells.size() == 1) {
            Point adjacentPosition = adjacentOwnedCells.get(0);
            Cell adjacentCell = board.getCell(adjacentPosition);
            Corporation adjacentCorporation = adjacentCell.getCorporation();

            board.replaceCellCorporation(currentCell, adjacentCorporation);
            return;
        }

        mergeCorporations(adjacentOwnedCells, cellPosition);
    }
}
