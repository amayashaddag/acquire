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

    // TODO : Javadoc
    public synchronized void handleCellPlacing(Point cellPosition, Player player) {
        placeCell(cellPosition, player);
        buyStocks(player);

        Cell currentCell = board.getCell(cellPosition);
        if (currentCell.isOwned()) {
            Corporation placedCorporation = board.getCell(cellPosition).getCorporation();
            adjustRelatedNet(placedCorporation, true);
        }

        board.updateDeadCells();
        board.updatePlayerDeck(player);
        playerTurnIndex = (playerTurnIndex + 1) % numberOfPlayers;

        Player nextPlayer = currentPlayers.get(playerTurnIndex);
        gameView.showInfoNotification(GameNotifications.playerTurnNotification(nextPlayer.getPseudo()));
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


    public void buyStocks(Player player) {
        // TODO : Implement this function
    }


    // TODO : Javadoc
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


    // TODO : Javadoc
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

    // TODO : Javadoc
    public void placeCell(Point cellPosition, Player currentPlayer) {
        Cell currentCell = board.getCell(cellPosition.getX(), cellPosition.getY());
        Corporation placedCorporation;

        currentCell.setAsOccupied();
        gameView.showSuccessNotification(
                GameNotifications.cellPlacingNotification(currentPlayer.getPseudo())
        );

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
            placedCorporation = gameView.getCorporationChoice(unplacedCorporations);
            currentPlayer.addToEarnedStocks(placedCorporation, FOUNDING_STOCK_BONUS);

            board.replaceCellCorporation(currentCell, placedCorporation);
            for (Point adjacent : adjacentOccupiedCells) {
                Cell adjacentOccupiedCell = board.getCell(adjacent);
                board.replaceCellCorporation(adjacentOccupiedCell, placedCorporation);
            }

            gameView.showInfoNotification(
                    GameNotifications.corporationFoundingNotification(
                            currentPlayer.getPseudo(),
                            placedCorporation
                    )
            );
        } else {
            mergeCorporations(adjacentOwnedCells, cellPosition, currentPlayer);
            placedCorporation = currentCell.getCorporation();

            for (Point adj : adjacentOccupiedCells) {
                Cell adjacentOccupiedCell = board.getCell(adj);
                board.replaceCellCorporation(adjacentOccupiedCell, placedCorporation);
            }
        }

        adjustRelatedNet(placedCorporation, false);
    }

    /**
     * Returns the owners of a given corporation
     */
    private List<Player> getOwners(Corporation c) {
        List<Player> owners = new LinkedList<>();

        for (Player p : currentPlayers) {
            if (p.ownsStocksFromCorporation(c)) {
                owners.add(p);
            }
        }

        return owners;
    }

    /**
     * Calculates a list of major owners of a given corporation.
     * This function supposes that there is a given list of players referring to
     * all the owners of the given company.
     * 
     * @param owners All the owners of the given company
     * @param c Given company
     * @return Major owners of a given company
     * @see #getOwners(Corporation) 
     */
    private List<Player> getMajorOwners(List<Player> owners, Corporation c) {
        List<Player> majorOwners = new LinkedList<>();

        int maxStocksOwnedByPlayer = -1;
        for (Player p : currentPlayers) {
            int currentNumberOfStocks = p.getStocks(c);
            
            if (maxStocksOwnedByPlayer < currentNumberOfStocks) {
                maxStocksOwnedByPlayer = currentNumberOfStocks;
            }
        }
        
        for (Player p : currentPlayers) {
            int currentNumberOfStocks = p.getStocks(c);
            
            if (currentNumberOfStocks == maxStocksOwnedByPlayer) {
                majorOwners.add(p);
            }
        }
        
        return majorOwners;
    }

    // TODO : Javadoc
    private int getBonusFreeNet(Player p, Corporation c) {
        int ownedStocks = p.getStocks(c);
        int unityStockPrice = board.getStockPrice(c);

        return ownedStocks * unityStockPrice;
    }

    // TODO : Javadoc
    // TODO : Test this function
    private void adjustRelatedNet(Corporation c, boolean addNet) {
        List<Player> allOwners = getOwners(c);
        List<Player> majorOwners = getMajorOwners(allOwners, c);
        List<Player> minorOwners = new LinkedList<>(allOwners);
        minorOwners.removeAll(majorOwners);

        int numberOfMajorOwners = majorOwners.size();
        int numberOfMinorOwners = minorOwners.size();
        int personalMajoritySharehold;
        if (numberOfMajorOwners != 0) {
             personalMajoritySharehold = board.getMajoritySharehold(c) / numberOfMajorOwners;
        } else {
            personalMajoritySharehold = 0;
        }

        int personalMinoritySharehold;
        if (numberOfMinorOwners != 0) {
             personalMinoritySharehold = board.getMinoritySharehold(c) / numberOfMinorOwners;
        } else {
            personalMinoritySharehold = 0;
        }

        for (Player p : allOwners) {
            int bonusFreeNet = getBonusFreeNet(p, c);

            if (addNet) {
                p.addToNet(bonusFreeNet);
            } else {
                p.removeFromNet(bonusFreeNet);
            }

            int sharehold;

            if (majorOwners.contains(p)) {
                sharehold = personalMajoritySharehold;
            } else {
                sharehold = personalMinoritySharehold;
            }

            if (addNet) {
                p.addToNet(sharehold);
            } else {
                p.removeFromNet(sharehold);
            }
        }
    }


}
