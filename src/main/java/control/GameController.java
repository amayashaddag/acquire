package control;

import java.util.List;

import model.Board;
import model.Cell;
import model.Corporation;
import model.Player;
import tools.Pair;
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
     * 
     * @param cellPosition 
     * @return this function returns, according to a given cell position, whether the action of putting
     * a cell in that position will give rise to a hotel chain merge or not, by verifying all
     * the conditions that a merge should satisfy
     * 
     */
    public Pair<Point> possibleMerge(Point cellPosition) {
        List<Point> adjacentCells = board.adjacentCells(cellPosition);
        int size = adjacentCells.size();

        for (int i = 0; i < size; i++) {
            Point adjacentCellPosition = adjacentCells.get(i);
            Cell adjacentCell = board.getCell(adjacentCellPosition);

            if (adjacentCell.isOwned()) {
                continue;
            }

            Corporation adjacentCellCorporation = adjacentCell.getCorporation();
            if (board.corporationIsSafe(adjacentCellCorporation)) {
                continue;
            }

            for (int j = i + 1; j < size; j++) {
                Point otherAdjacentCellPosition = adjacentCells.get(j);
                Cell otherAdjacentCell = board.getCell(otherAdjacentCellPosition);

                if (otherAdjacentCell.isOwned()) {
                    continue;
                }

                Corporation otherCellCorporation = otherAdjacentCell.getCorporation();
                if (board.corporationIsSafe(otherCellCorporation)
                        || adjacentCellCorporation == otherCellCorporation) {
                    continue;
                }

                return new Pair<>(adjacentCellPosition, otherAdjacentCellPosition);

            }
        }

        return new Pair<>(null, null);
    }


    public void mergeCorporations(Point firsPosition, Point secondPosition) {

    }

    public void placeCell(Point cellPosition) {
        Cell cell = board.getCell(cellPosition.getX(), cellPosition.getY());
    }
}
