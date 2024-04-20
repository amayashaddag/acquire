package control.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.game.Board;
import model.game.Cell;
import model.game.Corporation;
import model.game.Player;
import model.tools.Action;
import model.tools.MergingChoice;
import model.tools.Point;
import view.game.GameView;

/**
 * @author Amayas HADDAG
 * @author Lamine BETRAOUI
 * @version 1.0
 */
public class BotController implements Cloneable {
    private Board board;
    private List<Player> currentPlayers;
    private final int numberOfPlayers;
    private Player currentPlayer;

    private int playerTurnIndex;
    public final static int FOUNDING_STOCK_BONUS = 1;

    public BotController(Board board, List<Player> currentPlayers, Player currentPlayer, int numberOfPlayers,
            int playerTurnIndex) {
        this.board = board;
        this.currentPlayers = currentPlayers;
        this.numberOfPlayers = currentPlayers.size();
        this.playerTurnIndex = playerTurnIndex;
        this.currentPlayer = currentPlayer;
    }

    public BotController(GameController controller) throws CloneNotSupportedException {
        this.board = (Board) controller.getBoard().clone();
        this.currentPlayers = new LinkedList<>(controller.getCurrentPlayers());
        this.numberOfPlayers = controller.getNumberOfPlayers();
        this.playerTurnIndex = controller.getPlayerTurnIndex();
        this.currentPlayer = controller.getCurrentPlayer();
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

    private void buyStocks(Player player) {
        Map<Corporation, Integer> possibleBuyingStocks = board.possibleBuyingStocks();
        if (!possibleBuyingStocks.isEmpty()) {
            Map<Corporation, Integer> stocksToBuy = chooseStocksToBuy(possibleBuyingStocks, player);
            buyChosenStocks(stocksToBuy, totalAmount(stocksToBuy), player);
        }
    }

    private Map<Corporation, Integer> chooseStocksToBuy(Map<Corporation, Integer> possibleBuyingStocks, Player charo) {
        int cpt = 3;
        Random randChoice = new Random();
        Map<Corporation, Integer> stocksToBuy = new HashMap<>();
        int price = 0;
        while (cpt > 0) {
            int moulaDuMec = charo.getCash();
            if (moulaDuMec - price < minimalStockPrice(possibleBuyingStocks.keySet()))
                break;
            if (!randChoice.nextBoolean()) {
                break;
            }
            Corporation randomCorp = randomCorporation(possibleBuyingStocks.keySet());
            if (stocksToBuy.containsKey(randomCorp)) {
                int amountToBuy = stocksToBuy.get(randomCorp);
                stocksToBuy.put(randomCorp, amountToBuy + 1);
            } else {
                stocksToBuy.put(randomCorp, 1);
            }
            int amountToDecrement = possibleBuyingStocks.get(randomCorp);
            if (amountToDecrement - 1 <= 0) {
                possibleBuyingStocks.remove(randomCorp);
            } else {
                possibleBuyingStocks.put(randomCorp, amountToDecrement - 1);
            }
            price += board.getStockPrice(randomCorp);
            cpt--;
        }
        return stocksToBuy;
    }

    private int minimalStockPrice(Set<Corporation> stocks) {
        int minimalStockPrice = Integer.MAX_VALUE;
        for (Corporation corp : stocks) {
            int stockPrice = board.getStockPrice(corp);
            if (stockPrice < minimalStockPrice)
                minimalStockPrice = stockPrice;
        }
        return minimalStockPrice;
    }

    private Corporation randomCorporation(Set<Corporation> setCorporations) {
        Random randCorp = new Random();
        int corpInt = randCorp.nextInt(setCorporations.size());
        int cpt = 0;
        for (Corporation corp : setCorporations) {
            if (cpt == corpInt)
                return corp;
            cpt++;
        }
        return null;
    }

    private int totalAmount(Map<Corporation, Integer> stocksToBuy) {
        int sum = 0;
        for (Corporation corp : stocksToBuy.keySet()) {
            sum += board.getStockPrice(corp) * stocksToBuy.get(corp);
        }
        return sum;
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
     *          combination in order to display a notification that tells him to
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
    public void mergeCorporations(Set<Point> cellsToMerge, Action action, Player player, boolean random) {
        Set<Corporation> maxCorporations = filterMaximalSizeCorporations(cellsToMerge);
        Set<Corporation> adjacentCorporations = board.adjacentCorporations(action.getPoint());

        Cell currentCell = board.getCell(action.getPoint());
        Corporation chosenCellCorporation;

        if (maxCorporations.size() == 1) {
            Iterator<Corporation> iterator = maxCorporations.iterator();
            chosenCellCorporation = iterator.next();
        } else {
            chosenCellCorporation = randomCorporation(maxCorporations);
        }

        board.replaceCellCorporation(currentCell, chosenCellCorporation);
        for (Point adj : cellsToMerge) {
            Cell adjacentCell = board.getCell(adj);
            if (!adjacentCell.isOwned() || !(adjacentCell.getCorporation() == chosenCellCorporation)) {
                board.replaceCorporationFrom(chosenCellCorporation, adj);
            }
        }

        adjacentCorporations.remove(chosenCellCorporation);

        Map<Corporation, Integer> stocksToKeepSellOrTrade = stocksToKeepSellOrTrade(player, adjacentCorporations);
        if (!stocksToKeepSellOrTrade.isEmpty()) {
            if (random) {
                Map<Corporation, Integer> mapTrade = new HashMap<>();
                Map<Corporation, Integer> mapSell = new HashMap<>();

                chooseKST(stocksToKeepSellOrTrade, mapTrade, mapSell);
                sellStocks(mapSell, player);
                tradeStocks(mapTrade, player, chosenCellCorporation);
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

    private void chooseKST(Map<Corporation, Integer> stocks, Map<Corporation, Integer> trade,
            Map<Corporation, Integer> sell) {
        Random rand = new Random();
        while (!stocks.isEmpty()) {

            for (Corporation c : stocks.keySet()) {
                int amount = stocks.get(c);
                if (amount == 0) {
                    stocks.remove(c);
                }
            }

            boolean putInMap1 = rand.nextBoolean();
            Map<Corporation, Integer> choosenMap;
            if (putInMap1) {
                choosenMap = trade;
            } else {
                choosenMap = sell;
            }
            Corporation corp = randomCorporation(stocks.keySet());
            int maxAmount = stocks.get(corp);
            int randomAmount = rand.nextInt(maxAmount) + 1;
            if (randomAmount != 0) {
                if (choosenMap.containsKey(corp)) {
                    int addAmount = choosenMap.get(corp);
                    choosenMap.put(corp, addAmount + randomAmount);
                } else {
                    choosenMap.put(corp, randomAmount);
                }
                int oldAmount = stocks.get(corp);
                if (oldAmount - randomAmount <= 0) {
                    stocks.remove(corp);
                } else {
                    stocks.put(corp, oldAmount - randomAmount);
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
                if (amount > 0) {
                    stocks.put(c, amount);
                }
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
    public void placeCell(Action action, Player currentPlayer, boolean random) {
        Cell currentCell = board.getCell(action.getPoint().getX(), action.getPoint().getY());
        Corporation placedCorporation;

        currentCell.setAsOccupied();

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
            // This initialization should be replaced later with the choice of the player

            List<Corporation> unplacedCorporations = board.unplacedCorporations();

            placedCorporation = randomCorporation(new HashSet<>(unplacedCorporations));
            currentPlayer.addToEarnedStocks(placedCorporation, FOUNDING_STOCK_BONUS);

            board.replaceCellCorporation(currentCell, placedCorporation);

        } else {
            mergeCorporations(adjacentOwnedCells, action, currentPlayer, random);
            placedCorporation = currentCell.getCorporation();
        }

        for (Point adj : adjacentOccupiedCells) {
            Cell adjacentOccupiedCell = board.getCell(adj);
            board.replaceCellCorporation(adjacentOccupiedCell, placedCorporation);
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
    public synchronized void handlePlayerTurn(Action action, Player player, boolean random) {
        resetNets();
        placeCell(action, player, random);
        if (board.thereArePlacedCorporations()) {
            buyStocks(player);
        }
        adjustNets();

        board.updateDeadCells();

        for (Player p : currentPlayers) {
            board.updatePlayerDeck(p);
        }

        playerTurnIndex = (playerTurnIndex + 1) % numberOfPlayers;
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

            // TODO : Send notification for selling stocks
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

            // TODO : Add notification for trading stocks
        }
    }

    public void simulateGame() {
        while (!board.isGameOver()) {
            Player currentPlayer = currentPlayers.get(playerTurnIndex);

            Random r = new Random();
            List<Point> possibleCells = new LinkedList<>();
            Point[] deck = currentPlayer.getDeck();
            for (int i = 0; i < Board.DECK_SIZE; i++) {
                Point p = deck[i];
                if (p != null) {
                    possibleCells.add(p);
                }
            }

            if (!possibleCells.isEmpty()) {
                int randomIndex = r.nextInt(possibleCells.size());
                Point cellPosition = possibleCells.get(randomIndex);

                Action action = new Action(cellPosition, null, null);
                handlePlayerTurn(action, currentPlayer, true);
            }
        }
    }

    public boolean currentPlayerWon() {
        int maxMoney = 0;
        int currentPlayerIncome = currentPlayer.getNet();
        for (Player p : currentPlayers) {
            int playerIncome = p.getNet();
            if (playerIncome > maxMoney) {
                maxMoney = playerIncome;
            }
        }

        return currentPlayerIncome >= maxMoney;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BotController clonedController = (BotController) super.clone();
        clonedController.board = (Board) this.board.clone();
        clonedController.currentPlayers = new ArrayList<>();

        for (Player player : this.currentPlayers) {
            clonedController.currentPlayers.add((Player) player.clone());
        }

        clonedController.currentPlayer = (Player) this.currentPlayer.clone();
        return clonedController;
    }

    /**
     * This function generates all the possible actions that a played can play.
     * It is used in Monte-Carlo AI algorithm
     * 
     * @return a list of all the possible actions.
     */
    public List<Action> getPossibleActions() {
        List<Action> possibleActions = new LinkedList<>();

        for (Point p : currentPlayer.getDeck()) {
            Map<Corporation, Integer> possibleBuyingStocks = board.possibleBuyingStocks();

            List<Map<Corporation, Integer>> combinationsOfBuyingStocks = generateCombinations(possibleBuyingStocks,
                    Board.MAXIMUM_AMOUNT_OF_BUYING_STOCKS);

            for (MergingChoice choice : MergingChoice.values()) {
                for (Map<Corporation, Integer> comb : combinationsOfBuyingStocks) {
                    Action possibleAction = new Action(p, comb, choice);

                    possibleActions.add(possibleAction);
                }
            }

            /*
             * This part includes the cases of not buying stocks for each choice of merging
             * choices
             */

            for (MergingChoice choice : MergingChoice.values()) {
                Action emptyStockAction = new Action(p, new HashMap<>(), choice);

                possibleActions.add(emptyStockAction);
            }

        }

        return possibleActions;
    }

    /**
     * This method is a "helper" for {@link #getPossibleActions()} method.
     * It generates all the possible combinations for a given map under a given
     * threshold
     * 
     * @param inputMap  the map from which it will generate the combinations
     * @param threshold the condition (in this case it represent the maximum of
     *                  possible buying stocks)
     * @return all the possible combinations of values of Map<Corporation, Integer>
     *         such as
     *         the sum of the values are less or equal to threshold
     */
    private List<Map<Corporation, Integer>> generateCombinations(Map<Corporation, Integer> inputMap,
            int threshold) {
        List<Map<Corporation, Integer>> combinations = new LinkedList<>();
        generateCombinationsHelper(inputMap, threshold, new HashMap<>(), combinations);
        return combinations;
    }

    /**
     * Just a "helper" method for {@link #generateCombinations(Map, int)}
     */
    private void generateCombinationsHelper(Map<Corporation, Integer> inputMap, int threshold,
            Map<Corporation, Integer> current, List<Map<Corporation, Integer>> combinations) {
        int sum = current.values().stream().mapToInt(Integer::intValue).sum();
        if (sum > threshold) {
            return;
        }

        if (!current.isEmpty()) {
            combinations.add(new HashMap<>(current));
        }

        for (Map.Entry<Corporation, Integer> entry : inputMap.entrySet()) {
            Corporation key = entry.getKey();
            int value = entry.getValue();

            if (!current.containsKey(key)) {
                current.put(key, value);
                generateCombinationsHelper(inputMap, threshold, current, combinations);
                current.remove(key);
            }
        }
    }
}
