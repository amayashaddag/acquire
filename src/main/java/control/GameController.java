package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Timer;
import java.util.Map;

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
    private final Timer gameTimer;

    public final static int FPS = 60;
    public final static int GAME_DELAY = 1000 / FPS;
    public final static int FOUNDING_STOCK_BONUS = 1;

    public GameController(List<Player> currentPlayers, Player currentPlayer) {
        this.board = new Board();
        this.currentPlayers = currentPlayers;
        this.numberOfPlayers = currentPlayers.size();
        this.playerTurnIndex = 0;
        initPlayersDecks();
        this.gameTimer = new Timer(GAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (gameOver) {
                    stopGame();
                    // TODO : Handle end-game procedure
                    return;
                }
                Player currentPlayer = currentPlayers.get(playerTurnIndex);

                playerTurnIndex++;
                if (playerTurnIndex >= numberOfPlayers) {
                    playerTurnIndex = 0;
                }

                playTurn(currentPlayer);
                updatePlayerDeck(currentPlayer);
                board.updateDeadCells();

                if (board.isGameOver()) {
                    gameOver = true;
                }

            }
        });
        this.gameView = new GameView(this, currentPlayer);
    }

    public void handleCellPlacing(Point cellPosition, Player player) {
        placeCell(cellPosition, player);  // FIXME  : exception levé par cette fonction lorsque player.deck est vide
        updatePlayerDeck(player);
        board.updateDeadCells();
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
        Player currentPlayer = currentPlayers.get(playerTurnIndex);
        return currentPlayer;
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

    // TODO : Correct this function
    public void updatePlayerDeck(Player player) {
        Point[] playerDeck = player.getDeck();

        for (int i = 0; i < Board.DECK_SIZE; i++) {
            Point cellPosition = playerDeck[i];

            if (cellPosition == null || !board.canPlaceIn(cellPosition)) {
                Point randomChosenCell = board.getFromRemainingCells();
                playerDeck[i] = randomChosenCell;
            }
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
        gameView.showSuccessNotification(GameNotifications.successfullyBoughtStocks(amount, corporation));
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
        gameView.showSuccessNotification(GameNotifications.successfullySoldStocks(amount, corporation, amountToEarn));
    }


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
    public void mergeCorporations(List<Point> cellsToMerge, Point cellPosition) {
        List<Point> maxCorporations = filterMaximalSizeCorporations(cellsToMerge);
        Point chosenCellPosition;
        Cell chosenCell, currentCell = board.getCell(cellPosition);
        Corporation chosenCellCorporation;

        if (maxCorporations.size() == 1) {
            chosenCellPosition = maxCorporations.get(0);
        } else {
            // TODO : call the game view to request player to choose one major holder

            /* Temporary initialization */
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


    public void placeCell(Point cellPosition, Player currentPlayer) {
        Cell currentCell = board.getCell(cellPosition.getX(), cellPosition.getY());
        currentCell.setAsOccupied();
        gameView.showSuccessNotification(
                GameNotifications.cellPlacingNotification(currentPlayer.getPseudo(), cellPosition)
        );

        List<Point> adjacentOwnedCells = board.adjacentOwnedCells(cellPosition);
        if (adjacentOwnedCells.isEmpty()) {
            
            List<Point> adjacentOccupiedCells = board.adjacentOccupiedCells(cellPosition);
            if (adjacentOccupiedCells.isEmpty()) {
                return;
            }

            // TODO : Ask player to chose a corporation between remaining ones
            List<Corporation> unplacedCorporations = board.unplacedCorporations();
            Corporation chosenCorporation = unplacedCorporations.get(0);

            board.replaceCellCorporation(currentCell, chosenCorporation);
            for (Point adj : adjacentOccupiedCells) {
                Cell adjacentCell = board.getCell(adj);
                board.replaceCellCorporation(adjacentCell, chosenCorporation);
            }

            board.removeFromRemainingStocks(chosenCorporation, FOUNDING_STOCK_BONUS);
            currentPlayer.addToEarnedStocks(chosenCorporation, FOUNDING_STOCK_BONUS);
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

    // TODO : Implement graphical version
    public void playTurn(Player player) {
        // TODO : Handle waiting for an answer from the GUI

        /* Temporary initialization */
        int chosenIndexFromPlayerDeck = 0;
        Point cellPosition = player.getCell(chosenIndexFromPlayerDeck);

        placeCell(cellPosition, player);

        // TODO : Handle buying stocks if possible
        /* This map contains couples (corporation, amount) that represent the maximum amount
         * that the player can buy from that corporation (if the corporation is not figured in the Map, this
         * means that the player can't buy from it). The maximum amount is 3 according to game rules
         */
        Map<Corporation, Integer> possibleBuyingStocks = board.possibleBuyingStocks();

        // TODO : selling and trading will be handled later

    }

    public void startGame() {
        gameTimer.start();
    }

    public void stopGame() {
        gameTimer.stop();
    }

}
