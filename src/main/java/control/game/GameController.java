package control.game;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Timer;

import control.database.GameDatabaseConnection;
import control.menu.MenuController;
import model.game.Board;
import model.game.Cell;
import model.game.Corporation;
import model.game.Player;
import model.tools.Action;
import model.tools.Couple;
import model.tools.Point;
import view.game.GameNotifications;
import view.game.GameView;
import view.window.GameFrame;

/**
 * @author Amayas HADDAG
 * @version 1.0
 */
public class GameController {
    private final Board board;
    private final GameView gameView;
    private final List<Player> currentPlayers;
    private final int numberOfPlayers;
    private final String gameId;
    private final boolean onlineMode;
    private final Timer onlineObserver;
    private final Timer chatObserver;
    private final Timer botTurnTimer;
    private final Timer refresher;
    private final Map<Point, Corporation> newPlacedCells;
    private final Executor executor;

    private long lastNotificationTime;
    private long lastKeepSellTradeStockTime;
    private long lastChatMessageTime;
    private int playerTurnIndex;
    private boolean gameEnded;

    private Map<Corporation, Integer> registredStocksToKeepSellTrade;
    private Corporation registredMajorCorporation;

    public final static int FOUNDING_STOCK_BONUS = 1;
    public final static int ONLINE_OBSERVER_DELAY = 2000;
    public final static int BOT_TURN_OBSERVER_DELAY = 20;
    public final static int BOT_TURN_DELAY = 500;
    public final static int GAME_IN_PROGRESS_STATE = 1, GAME_NOT_STARTED_STATE = 0;
    public final static int NUM_SIMULATIONS = 3;

    public GameController(List<Player> currentPlayers, Player currentPlayer, String gameId, boolean online) {
        this.board = new Board();
        this.currentPlayers = currentPlayers;
        this.numberOfPlayers = currentPlayers.size();
        this.playerTurnIndex = 0;
        this.gameId = gameId;
        this.newPlacedCells = new HashMap<>();

        initPlayersDecks();

        this.gameView = new GameView(this, currentPlayer);
        this.onlineMode = online;
        this.executor = Executors.newSingleThreadExecutor();

        this.onlineObserver = !online ? null : new Timer(ONLINE_OBSERVER_DELAY, (ActionListener) -> {
            try {
                boolean result = updateGameState();

                if (result) {
                    return;
                }

                updateCurrentPlayer();
                updateNewPlacedCells();
            } catch (Exception e) {
                errorInterrupt(e);
            }

            executor.execute(() -> {
                try {
                    updateStocks();
                    updateCashNet();
                    updateLastNotification();
                    updateKeepSellOrTradeStocks();

                    board.updatePlayerDeck(currentPlayer);
                    gameView.updatePlayerDeck();
                } catch (Exception e) {
                    errorInterrupt(e);
                }
            });
        });

        this.botTurnTimer = online ? null : new Timer(BOT_TURN_OBSERVER_DELAY, (ActionEvent) -> {
            if (gameEnded) {
                endGame();
            } else {
                executor.execute(() -> {
                    try {
                        Thread.sleep(BOT_TURN_DELAY);

                        Player p = getCurrentPlayer();

                        if (!p.isBot()) {
                            return;
                        }

                        if (p.isEmptyDeck() || board.isGameOver()) {
                            gameEnded = true;
                            return;
                        }

                        BotController botController = new BotController(this);
                        MonteCarloAlgorithm monteCarlo = new MonteCarloAlgorithm(botController, NUM_SIMULATIONS);
                        Action nextAction = monteCarlo.runMonteCarlo();

                        handleCellPlacing(nextAction, p);

                        GameFrame parent = GameFrame.currentFrame;
                        parent.requestFocus();

                        gameView.repaint();
                    } catch (InterruptedException e) {

                    } catch (Exception e) {
                        errorInterrupt(e);
                        e.printStackTrace();
                    }
                });
            }
        });

        this.chatObserver = !online ? null : new Timer(ONLINE_OBSERVER_DELAY, (ActionEvent) -> {
            executor.execute(() -> {
                updateChat();
            });
        });

        this.refresher = new Timer(BOT_TURN_OBSERVER_DELAY, (ActionEvent) -> {
            gameView.repaint();
        });

        if (online) {
            onlineObserver.start();
            chatObserver.start();
        } else {
            botTurnTimer.start();
        }

        refresher.start();
    }

    private boolean updateGameState() throws Exception {
        boolean gameEnded = GameDatabaseConnection.isGameEnded(gameId);

        if (gameEnded) {
            endGame();
            return true;
        }

        return false;
    }

    private void updateNewPlacedCells() throws Exception {
        Map<Point, Corporation> newPlacedCells = GameDatabaseConnection.getNewPlacedCells(gameId, board);

        if (!newPlacedCells.isEmpty()) {
            board.updateNewPlacedCells(newPlacedCells);
        }
    }

    private void updateStocks() throws Exception {
        for (Player p : currentPlayers) {
            GameDatabaseConnection.updateStocks(p, gameId);
        }

        board.updateStocks(currentPlayers);
    }

    private void updateChat() {
        try {
            Player p = gameView.getPlayer();
            List<Couple<Couple<String, String>, Long>> newChats = GameDatabaseConnection.getNewChats(gameId, p.getUID(),
                    lastChatMessageTime);

            if (newChats.isEmpty()) {
                return;
            }

            lastChatMessageTime = newChats.get(newChats.size() - 1).getValue();

            for (Couple<Couple<String, String>, Long> c : newChats) {
                Player sender = null;
                String senderId = c.getKey().getKey();
                String message = c.getKey().getValue();

                boolean notified = message.contains("@" + gameView.getPlayer().getPseudo())
                        || message.contains("@everyone");

                for (Player player : currentPlayers) {
                    if (player.getUID().equals(senderId)) {
                        sender = player;
                    }
                }

                if (sender == null) {
                    continue;
                }

                gameView.recieveChat(sender, message, notified);
            }
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    private void updateCashNet() throws Exception {
        Map<String, int[]> playersCashNet = GameDatabaseConnection.getPlayersCashNet(gameId);

        for (String uid : playersCashNet.keySet()) {
            int[] income = playersCashNet.get(uid);
            int cash = income[0], net = income[1];

            Player p = getPlayerFromUid(uid);
            p.setCash(cash);
            p.setNet(net);
        }
    }

    private Player getPlayerFromUid(String uid) {
        for (Player p : currentPlayers) {
            if (p.getUID().equals(uid)) {
                return p;
            }
        }

        return null;
    }

    public void incPlayerTurnIndex() {
        playerTurnIndex++;
    }

    private void setCurrentPlayer() throws Exception {
        Player currentPlayer = currentPlayers.get(playerTurnIndex);

        GameDatabaseConnection.setCurrentPlayer(gameId, currentPlayer.getUID());
    }

    private void setCashNet() throws Exception {
        for (Player p : currentPlayers) {
            GameDatabaseConnection.setCash(p.getCash(), p, gameId);
            GameDatabaseConnection.setNet(p.getNet(), p, gameId);
        }
    }

    private void setNewPlacedCells() throws Exception {
        GameDatabaseConnection.setNewPlacedCells(newPlacedCells, gameId);
        newPlacedCells.clear();
    }

    private void setNewEarnedStocks() throws Exception {
        for (Player p : currentPlayers) {
            GameDatabaseConnection.setStocks(p, gameId);
        }
    }

    private void updateCurrentPlayer() throws Exception {
        String uid = GameDatabaseConnection.getCurrentPlayer(gameId);

        for (int i = 0; i < currentPlayers.size(); i++) {
            Player p = currentPlayers.get(i);

            if (p.getUID().equals(uid)) {
                playerTurnIndex = i;
                return;
            }
        }
    }

    private void updateLastNotification() throws Exception {
        Couple<String, Long> notification = GameDatabaseConnection.getLastNotification(gameId);

        if (notification == null) {
            return;
        }

        if (lastNotificationTime != notification.getValue()) {
            gameView.showInfoNotification(notification.getKey());
            lastNotificationTime = notification.getValue();
        }
    }

    private void updateKeepSellOrTradeStocks() {
        try {
            Map<Corporation, Long> stocks = GameDatabaseConnection.getKeepSellOrTradeStocks(gameId,
                    lastKeepSellTradeStockTime);

            if (stocks.isEmpty()) {
                return;
            }

            long time = stocks.entrySet().iterator().next().getValue();
            Corporation major = GameDatabaseConnection.getMajorCorporation(gameId, time);
            Set<Corporation> adjacentCorporations = stocks.keySet();
            Map<Corporation, Integer> stocksToKeepSellOrTrade = stocksToKeepSellOrTrade(gameView.getPlayer(),
                    adjacentCorporations);

            if (stocksToKeepSellOrTrade.isEmpty()) {
                return;
            }

            registredStocksToKeepSellTrade = stocksToKeepSellOrTrade;
            registredMajorCorporation = major;
            lastKeepSellTradeStockTime = time;

        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    private void errorInterrupt(Exception e) {
        GameFrame.showError(e, () -> {
            endGame();
        });
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

    public boolean isOnlineMode() {
        return onlineMode;
    }

    /**
     * This function is used at the beginning of the game (where it is already
     * supposed that there is enough board cells for everyone) to initialize players
     * decks.
     */
    private void initPlayersDecks() {
        for (Player player : currentPlayers) {
            Point[] deck = new Point[Board.DECK_SIZE];
            for (int i = 0; i < Board.DECK_SIZE; i++) {
                Point randomChosenCell = board.getFromRemainingCells();
                deck[i] = randomChosenCell;
            }

            player.setDeck(deck);
        }
    }

    private void buyStocks(Player player) {
        Map<Corporation, Integer> possibleBuyingStocks = board.possibleBuyingStocks();
        if (!possibleBuyingStocks.isEmpty()) {
            gameView.chooseStocksToBuy(possibleBuyingStocks);
        }
    }

    /**
     * @param chosenStocksToBuy Combination of corporations and number of stocks the
     *                          player wants to buy.
     * @return Price of the chosen combination of corporations and number of stocks
     * @apiNote This function should be used in {@link GameView} class to calculate
     *          the price of the
     *          combination of number of stocks and corporations the player wants to
     *          buy.
     *          Example : chosenStocksToBuy = {Tower = 2, American = 1}, in this
     *          case, the player chose to buy
     *          two stocks of Tower corporation and one of American one.
     *          The purpose of this function is to verify whether the player has
     *          enough cash to buy the wanted
     *          combination in order to display a that tells him to
     *          reconsider his choice because of
     *          lack of cash.
     */
    public int calculateStocksPrice(Map<Corporation, Integer> chosenStocksToBuy) {
        int totalValueToBuy = 0;
        for (Corporation c : chosenStocksToBuy.keySet()) {
            int stockPrice = board.getStockPrice(c);
            int amount = chosenStocksToBuy.get(c);

            totalValueToBuy += stockPrice * amount;
        }

        return totalValueToBuy;
    }

    /**
     * This function takes the final choice that the player wants to buy from
     * remaining stocks
     * and handles the buying process.
     * It supposes that the player has enough stocks to buy the stocks in
     * {@link GameView} class.
     * 
     * @param chosenStocks Stocks that the player chose th buy.
     * @param totalPrice   Total price of the chosen stocks.
     * @apiNote This function should be used in {@link GameView} class after making
     *          sure that
     *          the player has enough cash to buy the chosen stocks.
     */
    public void buyChosenStocks(Map<Corporation, Integer> chosenStocks, int totalPrice, Player player) {
        player.removeFromCash(totalPrice);

        for (Corporation c : chosenStocks.keySet()) {
            int amount = chosenStocks.get(c);

            player.addToEarnedStocks(c, amount);
            board.removeFromRemainingStocks(c, amount);
        }
    }

    /**
     * This function is used in merging corporations process, it filters all the
     * maximal sizes of corporations.
     * 
     * @param adjacentOwnedCells The owned cells that we should filter corporations
     *                           for
     */
    private Set<Corporation> filterMaximalSizeCorporations(Set<Point> adjacentOwnedCells) {
        int maxCorporationSize = 0;
        Set<Corporation> maxCorporations = new HashSet<>();

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
                maxCorporations.add(corporation);
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
    public void mergeCorporations(Set<Point> cellsToMerge, Action action, Player player) {
        Set<Corporation> maxCorporations = filterMaximalSizeCorporations(cellsToMerge);
        Set<Corporation> adjacentCorporations = board.adjacentCorporations(action.getPoint());

        Cell currentCell = board.getCell(action.getPoint());
        Corporation chosenCellCorporation;

        if (maxCorporations.size() == 1) {
            Iterator<Corporation> iterator = maxCorporations.iterator();
            chosenCellCorporation = iterator.next();
        } else {

            if (player.isHuman()) {
                chosenCellCorporation = gameView.getCorporationChoice(maxCorporations.stream().toList());
            } else {
                chosenCellCorporation = maxCorporations.iterator().next();
            }
        }

        board.replaceCellCorporation(currentCell, chosenCellCorporation);
        if (onlineMode) {
            newPlacedCells.put(action.getPoint(), chosenCellCorporation);
        }
        for (Point adj : cellsToMerge) {
            Cell adjacentCell = board.getCell(adj);
            if (!adjacentCell.isOwned() || !(adjacentCell.getCorporation() == chosenCellCorporation)) {
                Map<Point, Corporation> replaced = board.replaceCorporationFrom(chosenCellCorporation, adj);
                newPlacedCells.putAll(replaced);

                if (onlineMode) {
                    newPlacedCells.put(adj, chosenCellCorporation);
                }
            }
        }

        if (adjacentCorporations.size() > 1) {
            gameView.showInfoNotification(
                    GameNotifications.corporationMergingNotification(
                            player.getPseudo(),
                            chosenCellCorporation));
        }

        adjacentCorporations.remove(chosenCellCorporation);

        Map<Corporation, Integer> stocksToKeepSellOrTrade = stocksToKeepSellOrTrade(player, adjacentCorporations);

        if (onlineMode) {
            try {
                lastKeepSellTradeStockTime = Instant.now().toEpochMilli();
                GameDatabaseConnection.setKeepSellOrTradeStocks(adjacentCorporations, gameId,
                        lastKeepSellTradeStockTime);
                GameDatabaseConnection.setMajorCorporation(gameId, chosenCellCorporation, lastKeepSellTradeStockTime);
            } catch (Exception e) {
                errorInterrupt(e);
            }
        }

        if (!stocksToKeepSellOrTrade.isEmpty()) {
            if (player.isHuman()) {
                gameView.chooseSellingKeepingOrTradingStocks(stocksToKeepSellOrTrade, chosenCellCorporation);
            } else {
                switch (action.getMergingChoice()) {
                    case SELL:
                        sellStocks(stocksToKeepSellOrTrade, player);
                        break;
                    case TRADE:
                        tradeStocks(stocksToKeepSellOrTrade, player, chosenCellCorporation);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private Map<Corporation, Integer> stocksToKeepSellOrTrade(Player player, Set<Corporation> acquiredCorporations) {
        Map<Corporation, Integer> stocks = new HashMap<>();
        Map<Corporation, Integer> playerStocks = player.getEarnedStocks();

        for (Corporation c : playerStocks.keySet()) {
            if (acquiredCorporations.contains(c)) {
                int amount = playerStocks.get(c);
                stocks.put(c, amount);
            }
        }

        return stocks;
    }

    /**
     * This function handles cell placing in board according to a given position for
     * a given player.
     * It handles also the choice of the founding corporation.
     * If it is possible, it also handles the major holder while merging
     * corporations.
     * 
     * @param cellPosition  represents where to place a new cell.
     * @param currentPlayer represents the player that is about to place the cell.
     */
    public void placeCell(Action action, Player currentPlayer) {
        Cell currentCell = board.getCell(action.getPoint().getX(), action.getPoint().getY());
        Corporation placedCorporation;

        currentCell.setAsOccupied();

        if (onlineMode) {
            newPlacedCells.put(action.getPoint(), null);
        }

        if (currentPlayer.equals(gameView.getPlayer())) {
            gameView.showSuccessNotification(
                    GameNotifications.cellPlacingNotification(currentPlayer.getPseudo()));
        }

        Set<Point> adjacentOwnedCells = board.adjacentOwnedCells(action.getPoint());
        Set<Point> adjacentOccupiedCells = board.adjacentOccupiedCells(action.getPoint());
        Set<Corporation> adjacentCorporations = board.adjacentCorporations(action.getPoint());

        if (adjacentCorporations.isEmpty()) {
            if (adjacentOccupiedCells.isEmpty()) {
                return;
            }

            // This part of the function supposes that a cell can be place in the given
            // position
            // Therefore, unplacedCorporations is supposed to never be empty

            List<Corporation> unplacedCorporations = board.unplacedCorporations();

            if (currentPlayer.isHuman()) {
                placedCorporation = gameView.getCorporationChoice(unplacedCorporations);
            } else {
                placedCorporation = action.getCreatedCorporation();
            }

            currentPlayer.addToEarnedStocks(placedCorporation, FOUNDING_STOCK_BONUS);

            board.replaceCellCorporation(currentCell, placedCorporation);

            if (onlineMode) {
                newPlacedCells.put(action.getPoint(), placedCorporation);
            }

            gameView.showInfoNotification(
                    GameNotifications.corporationFoundingNotification(
                            currentPlayer.getPseudo(),
                            placedCorporation));
        } else {
            mergeCorporations(adjacentOwnedCells, action, currentPlayer);
            placedCorporation = currentCell.getCorporation();
        }

        for (Point adj : adjacentOccupiedCells) {
            Cell adjacentOccupiedCell = board.getCell(adj);
            board.replaceCellCorporation(adjacentOccupiedCell, placedCorporation);

            if (onlineMode) {
                newPlacedCells.put(adj, placedCorporation);
            }
        }
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
     * @param c      Given company
     * @return Major owners of a given company
     * @see #getOwners(Corporation)
     */
    private List<Player> getMajorOwners(List<Player> owners, Corporation c) {
        List<Player> majorOwners = new LinkedList<>();

        int maxStocksOwnedByPlayer = -1;
        for (Player p : owners) {
            int currentNumberOfStocks = p.getStocks(c);

            if (maxStocksOwnedByPlayer < currentNumberOfStocks) {
                maxStocksOwnedByPlayer = currentNumberOfStocks;
            }
        }

        for (Player p : owners) {
            int currentNumberOfStocks = p.getStocks(c);

            if (currentNumberOfStocks == maxStocksOwnedByPlayer) {
                majorOwners.add(p);
            }
        }

        return majorOwners;
    }

    /**
     * @return Net value of the owned shares of a corporation for a given player
     */
    private int getBonusFreeNet(Player p, Corporation c) {
        int ownedStocks = p.getStocks(c);
        int unityStockPrice = board.getStockPrice(c);

        return ownedStocks * unityStockPrice;
    }

    /**
     * This functions adjusts the net of all the players according to current board
     * state.
     */
    private void adjustNets() {
        for (Corporation c : Corporation.values()) {
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
                p.addToNet(getBonusFreeNet(p, c));

                int sharehold;

                if (majorOwners.contains(p)) {
                    sharehold = personalMajoritySharehold;
                } else {
                    sharehold = personalMinoritySharehold;
                }

                p.addToNet(sharehold);
            }
        }
    }

    /**
     * Resets the players net to the basic value in order to recalculate the new
     * value of players net.
     */
    private void resetNets() {
        for (Player p : currentPlayers) {
            p.setNet(Player.INITIAL_CASH);
        }
    }

    /**
     * This function is the function that handles all the process of placing a cell
     * in board.
     * It adjusts all the associated parameters.
     * 
     * @param cellPosition represents where to place a new cell.
     * @param player       represents the player that is about to place a new cell.
     */
    public synchronized void handleCellPlacing(Action action, Player player) {

        if (onlineMode) {
            onlineObserver.stop();

            if (registredStocksToKeepSellTrade != null) {
                gameView.chooseSellingKeepingOrTradingStocks(registredStocksToKeepSellTrade, registredMajorCorporation);
                registredStocksToKeepSellTrade = null;
                registredMajorCorporation = null;
            }
        }

        resetNets();
        placeCell(action, player);

        if (board.thereArePlacedCorporations()) {
            if (player.isHuman()) {
                buyStocks(player);
            } else {
                Map<Corporation, Integer> chosenStocksToBuy = action.getBoughtStocks();

                if (!chosenStocksToBuy.isEmpty()) {
                    int totalPrice = calculateStocksPrice(chosenStocksToBuy);
                    buyChosenStocks(chosenStocksToBuy, totalPrice, player);
                }
            }
        }
        adjustNets();

        board.updateDeadCells();

        if (onlineMode) {
            board.updatePlayerDeck(player);
        } else {
            for (Player p : currentPlayers) {
                board.updatePlayerDeck(p);
            }
        }

        if (board.isGameOver()) {
            gameEnded = true;

            if (onlineMode) {
                endGame();
            }

            return;
        }

        playerTurnIndex = (playerTurnIndex + 1) % numberOfPlayers;
        Player nextPlayer = currentPlayers.get(playerTurnIndex);

        gameView.repaint();

        if (onlineMode) {
            try {
                String notification = GameNotifications.playerTurnNotification(nextPlayer.getPseudo());
                GameDatabaseConnection.setLastNotification(gameId, notification);
            } catch (Exception e) {
                errorInterrupt(e);
            }
        }

        if (onlineMode) {
            try {
                setNewEarnedStocks();
                setNewPlacedCells();
                setCashNet();
                setCurrentPlayer();
            } catch (Exception e) {
                errorInterrupt(e);
            }

            onlineObserver.start();
        }
    }

    /**
     * This function handles the stocks selling, it is called from {@link GameView}
     * class
     * after the played chose the corporations he wants to sell to apply the
     * process.
     *
     * @param stocks Represents the map that associates each corporation to the
     *               number of stocks
     *               the player wants to sell.
     */
    public void sellStocks(Map<Corporation, Integer> stocks, Player player) {
        for (Corporation c : stocks.keySet()) {
            int amount = stocks.get(c);
            int totalPriceForCorporation = board.getStockPrice(c) * amount;

            board.addToRemainingStocks(c, amount);
            player.removeFromEarnedStocks(c, amount);
            player.addToCash(totalPriceForCorporation);

            if (player.equals(gameView.getPlayer())) {
                gameView.showSuccessNotification(
                        GameNotifications.soldStocksNotification(amount, c, totalPriceForCorporation));
            }
        }
    }

    /**
     * This functions handles the trading of stocks process after a major
     * corporation acquired player's
     * owned corporation stocks. It is called from {@link GameView} class after the
     * player chose the amount
     * of stocks they want to trade.
     */
    public void tradeStocks(Map<Corporation, Integer> stocks, Player player, Corporation major) {
        for (Corporation c : stocks.keySet()) {
            int amountToGive = stocks.get(c);
            int amountToEarn = amountToGive == 1 ? 1 : amountToGive / 2;

            board.addToRemainingStocks(c, amountToGive);
            board.removeFromRemainingStocks(major, amountToEarn);
            player.removeFromEarnedStocks(c, amountToGive);
            player.addToEarnedStocks(major, amountToEarn);

            if (player.equals(gameView.getPlayer())) {
                gameView.showSuccessNotification(
                        GameNotifications.tradedStocksNotification(amountToGive, c, amountToEarn, major));
            }
        }
    }

    private void endGame() {
        if (onlineMode) {
            try {
                if (gameId != null) {
                    GameDatabaseConnection.removeGame(gameId);
                } else {
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                errorInterrupt(e);
            }
        }

        exitGame();
    }

    public void sendChat(String chat, Player p) {
        try {
            long currentTime = Instant.now().toEpochMilli();
            GameDatabaseConnection.sendChat(chat, p.getUID(), p.getPseudo(), gameId, currentTime);
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    /**
     * @return a list of (String, Integer) which represent the pseudo and total cash
     *         of each player.
     */
    public List<Couple<String, Integer>> getCurrentCashes() {
        List<Couple<String, Integer>> currentCashes = new LinkedList<>();

        for (Player p : currentPlayers) {
            currentCashes.add(new Couple<String, Integer>(p.getPseudo(), p.getCash()));
        }

        return currentCashes;
    }

    /**
     * @return a list of (String, Integer) which represent the pseudo and total net
     *         of each player.
     */
    public List<Couple<String, Integer>> getCurrentNets() {
        List<Couple<String, Integer>> currentNets = new LinkedList<>();

        for (Player p : currentPlayers) {
            currentNets.add(new Couple<String, Integer>(p.getPseudo(), p.getNet()));
        }

        return currentNets;
    }

    /**
     * @return total cash in game.
     */
    public int getTotalCash() {
        int totalCash = 0;

        for (Player p : currentPlayers) {
            totalCash += p.getCash();
        }

        return totalCash;
    }

    /**
     * @return total net in game.
     */
    public int getTotalNet() {
        int totalNet = 0;

        for (Player p : currentPlayers) {
            totalNet += p.getCash();
        }

        return totalNet;
    }

    private void stopObservers() {
        if (onlineMode) {
            chatObserver.stop();
            onlineObserver.stop();
        } else {
            gameEnded = true;
            botTurnTimer.stop();

            ((ExecutorService) executor).shutdownNow();
        }

        refresher.stop();
    }

    public void exitGame() {
        if (onlineMode) {
            try {
                Player p = gameView.getPlayer();
                GameDatabaseConnection.removePlayer(p.getUID());
            } catch (Exception e) {
                errorInterrupt(e);
            }
        }

        stopObservers();

        GameFrame.clearCurrentFrame();

        MenuController menuController = new MenuController();
        menuController.start();
    }

    public boolean isBanChat(Player p) {
        return false;
        // A implementer
    }

    public void repportPlayer(Player agresor, Player victim) {
        // Signaler un joueur pour msg offensant
        // Lorsque plus d'1/3 de la game signal le joueur on le ban chat
        // Que penses-tu de l'idée ?
    }
}
