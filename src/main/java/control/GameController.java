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
     * Returns the adjacent owned cell positions to a given cell position in order to
     * determine the action to perform on the cell
     * 
     * @param cellPosition
     * @return
     */
    public List<Point> adjacentOwnedCells(Point cellPosition) {
        List<Point> adjacentCells = board.adjacentCells(cellPosition);

        for (Point p : adjacentCells) {
            Cell adjacentCell = board.getCell(p);
            if (!adjacentCell.isOwned()) {
                adjacentCells.remove(p);
            }
        }

        return adjacentCells;
    }


    private List<Point> stalinSort(List<Point> adjacentOwnedCells) {
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
     * 
     * @param cellsToMerge
     * @param cellPosition
     */
    public void mergeCorporations(List<Point> cellsToMerge, Point cellPosition) {
        List<Point> maxCorporations = stalinSort(cellsToMerge);
        Cell currentCell = board.getCell(cellPosition);

        if (maxCorporations.size() == 1) {
            Point maxCorporationPosition = maxCorporations.get(0);
            Cell maxCell = board.getCell(maxCorporationPosition);
            Corporation maxCorporation = maxCell.getCorporation();

            currentCell.setCorporation(maxCorporation);
            // TODO : Transformer toutes les autres owned cells en maxCorporation
            return;
        }

        // TODO : appeler l'interface pour récupérer la corporation choisie
        /* Temporary intialization */
        Corporation chosenCorporation = Corporation.NONE; 
        currentCell.setCorporation(chosenCorporation);
        // TODO : transformer les autres owned cells en chosenCorporation
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
            currentCell.setCorporation(adjacentCorporation);
            return;
        }

        mergeCorporations(adjacentOwnedCells, cellPosition);
    }
}

