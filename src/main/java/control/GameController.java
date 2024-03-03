package control;

import java.util.*;

import model.Board;
import model.Cell;
import model.Corporation;
import model.Player;
import tools.Point;
import view.game.GameNotifications;
import view.game.GameView;

public class GameController {
    private final Board board;
    private final GameView gameView;
    private final List<Player> currentPlayers;
    private int playerTurnIndex;
    private final int numberOfPlayers;
    private boolean gameOver;

    
    public final static int FOUNDING_STOCK_BONUS = 1;

    public GameController(List<Player> currentPlayers, Player currentPlayer) {
        this.board = new Board();
        this.currentPlayers = currentPlayers;
        this.numberOfPlayers = currentPlayers.size();
        this.playerTurnIndex = 0;
        initPlayersDecks();
        this.gameView = new GameView(this, currentPlayer);
    }

    public synchronized void handleCellPlacing(Point cellPosition, Player player) {
        placeCell(cellPosition, player);  // FIXME  : Fix NullPointerException in this function
        board.updateDeadCells();
        board.updatePlayerDeck(player);
        playerTurnIndex = (playerTurnIndex + 1) % numberOfPlayers;
    }

    public GameView getGameView() {
        return gameView;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player getCurrentPlayer() {
        return currentPlayers.get(playerTurnIndex);
    }

    public int getPlayerTurnIndex() {
        return playerTurnIndex;
    }

    public List<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     * This function is used at the beginning of the game (where it is already
     * supposed that there is enough board cells for everyone) to initialize players
     * decks
     */
    public void initPlayersDecks() {
        for (Player player : currentPlayers) {
            Point[] deck = new Point[Board.DECK_SIZE];
            for (int i = 0; i < Board.DECK_SIZE; i++) {
                Point randomChosenCell = board.getFromRemainingCells();
                deck[i] = randomChosenCell;
            }

            player.setDeck(deck);
        }
    }


    public void buyStocks(Player player, Corporation corporation, int amount) {
        if (!board.enoughRemainingStocks(corporation, amount)) {
            gameView.showErrorNotification(GameNotifications.NOT_ENOUGH_REMAINING_STOCKS_IN_BANK);
            return;
        }
        int unityStockPrice = board.getStockPrice(corporation);
        int amountToPay = unityStockPrice * amount;

        if (!player.hasEnoughCash(amountToPay)) {
            gameView.showErrorNotification(GameNotifications.NOT_ENOUGH_CASH);
            return;
        }

        board.removeFromRemainingStocks(corporation, amount);
        player.addToEarnedStocks(corporation, amount);
        player.removeFromCash(amountToPay);
        gameView.showSuccessNotification(GameNotifications.boughtStocksNotification(amount, corporation));
    }


    public void sellStocks(Player player, Corporation corporation, int amount) {
        if (!player.hasEnoughStocks(corporation, amount)) {
            gameView.showErrorNotification(GameNotifications.NOT_ENOUGH_STOCKS_PLAYER);
            return;
        }

        int unityStockPrice = board.getStockPrice(corporation);
        int amountToEarn = unityStockPrice * amount;

        board.addToRemainingStocks(corporation, amountToEarn);
        player.removeFromEarnedStocks(corporation, amountToEarn);
        player.addToCash(amountToEarn);
        gameView.showSuccessNotification(GameNotifications.soldStocksNotification(amount, corporation, amountToEarn));
    }


    private List<Point> filterMaximalSizeCorporations(Set<Point> adjacentOwnedCells) {
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
     * This function is called when a merge is possible and processes it.
     * It checks whether there is a unique maximum size company and merges all the
     * adjacent
     * companies to it, if there is not, it asks the player to choose one maximum
     * size company
     * 
     * @param cellsToMerge represents the set of adjacent cells to merge
     * @param cellPosition represents the position of the cell where the merge
     *                     started
     * @see #placeCell(Point, Player)
     */
    public void mergeCorporations(Set<Point> cellsToMerge, Point cellPosition, Player player) {
        List<Point> maxCorporations = filterMaximalSizeCorporations(cellsToMerge);
        Set<Corporation> adjacentCorporations = board.adjacentCorporations(cellPosition);

        Point chosenCellPosition;
        Cell chosenCell, currentCell = board.getCell(cellPosition);
        Corporation chosenCellCorporation;

        if (maxCorporations.size() == 1) {
            chosenCellPosition = maxCorporations.get(0);
        } else {
            // TODO : call the game view to request player to choose one major holder

            /* Temporary initialization */
            // TODO : Should pass maxCorporations as a parameter to gameView
            chosenCellPosition = maxCorporations.get(0);
        }

        chosenCell = board.getCell(chosenCellPosition);
        chosenCellCorporation = chosenCell.getCorporation();

        board.replaceCellCorporation(currentCell, chosenCellCorporation);
        for (Point adj : cellsToMerge) {
            if (!adj.equals(chosenCellPosition)) {
                Cell adjacentCell = board.getCell(adj);
                Corporation adjacentCorporation = adjacentCell.getCorporation();

                board.replaceCorporationFrom(chosenCellCorporation, adj);
                // TODO : Do not add the case where the adjacentCorporation equals to chosenCorporation
                if (chosenCellCorporation != adjacentCorporation) {
                    addSharingBonus(adjacentCorporation);
                }
            }
        }

        if (adjacentCorporations.size() > 1) {
            gameView.showInfoNotification(
                    GameNotifications.corporationMergingNotification(
                            player.getPseudo(),
                            chosenCellCorporation
                    )
            );
        }
    }

    public void placeCell(Point cellPosition, Player currentPlayer) {
        Cell currentCell = board.getCell(cellPosition.getX(), cellPosition.getY());
        currentCell.setAsOccupied();
        gameView.showSuccessNotification(GameNotifications.cellPlacingNotification(currentPlayer.getPseudo()));

        Set<Point> adjacentOwnedCells = board.adjacentOwnedCells(cellPosition);
        Set<Point> adjacentOccupiedCells = board.adjacentOccupiedCells(cellPosition);
        Set<Corporation> adjacentCorporations = board.adjacentCorporations(cellPosition);

        if (adjacentCorporations.isEmpty()) {
            if (adjacentOccupiedCells.isEmpty()) {
                return;
            }

            // This part of the function supposes that a cell can be place in the given position
            // Therefore, unplacedCorporations is supposed to never be empty
            // This initialization should be replaced later with the choice of the player

            List<Corporation> unplacedCorporations = board.unplacedCorporations();
            Corporation chosenCorporationToPlace = gameView.getCorporationChoice(unplacedCorporations);
            currentPlayer.addToEarnedStocks(chosenCorporationToPlace, FOUNDING_STOCK_BONUS);

            board.replaceCellCorporation(currentCell, chosenCorporationToPlace);
            for (Point adjacent : adjacentOccupiedCells) {
                Cell adjacentOccupiedCell = board.getCell(adjacent);
                board.replaceCellCorporation(adjacentOccupiedCell, chosenCorporationToPlace);
            }

            gameView.showInfoNotification(
                    GameNotifications.corporationFoundingNotification(
                            currentPlayer.getPseudo(),
                            chosenCorporationToPlace
                    )
            );

            return;
        }

        mergeCorporations(adjacentOwnedCells, cellPosition, currentPlayer);

        Corporation mergedCorporation = currentCell.getCorporation();

        for (Point adj : adjacentOccupiedCells) {
            Cell adjacentOccupiedCell = board.getCell(adj);
            board.replaceCellCorporation(adjacentOccupiedCell, mergedCorporation);
        }
    }

    /**
     * @return List of the players owning at least one stock from the given corporation
     */
    public List<Player> allOwners(Corporation corporation) {
        List<Player> playersOwning = new LinkedList<>();

        for (Player player : currentPlayers) {
            if (player.ownsStocksFromCorporation(corporation)) {
                playersOwning.add(player);
            }
        }

        return playersOwning;
    }

    /**
     * Calculates the list of the major owners of a given company.
     * It takes the corporation owners list calculated from {@link #allOwners(Corporation)}
     * and removes the major owners from it to return it in a separated list.
     */
    public List<Player> majorOwners(List<Player> allOwners, Corporation c) {
        List<Player> majorOwners = new LinkedList<>();
        int majorOwnerAmountOfStocks = -1;

        for (Player owner : allOwners) {
            int currentOwnerStocks = owner.getStocks(c);

            if (majorOwnerAmountOfStocks == -1) {
                majorOwnerAmountOfStocks = currentOwnerStocks;
            } else if (currentOwnerStocks > majorOwnerAmountOfStocks) {
                majorOwnerAmountOfStocks = currentOwnerStocks;
            }
        }

        for (Player owner : allOwners) {
            int currentOwnerStocks = owner.getStocks(c);

            if (currentOwnerStocks >= majorOwnerAmountOfStocks) {
                majorOwners.add(owner);
            }
        }

        allOwners.removeAll(majorOwners);

        return majorOwners;

    }

    /**
     * This function takes all the owners of shares of the given function and adds them
     * the owning bonus.
     * This function is called when a corporation will be merged to another.
     */
    public void addSharingBonus(Corporation c) {
        List<Player> owners = allOwners(c);
        List<Player> majorOwners = majorOwners(owners, c);

        int minoritySharehold = board.getMinoritySharehold(c);
        int majoritySharehold = board.getMajoritySharehold(c);

        for (Player p : owners) {
            p.addToNet(minoritySharehold);
        }

        for (Player p : majorOwners) {
            p.addToNet(majoritySharehold);
        }
    }



}
