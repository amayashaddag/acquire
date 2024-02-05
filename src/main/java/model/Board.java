package model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class Board {
    public final static int BOARD_WIDTH = 12, BOARD_HEIGHT = 9;
    public final static int INITIAL_STOCKS_PER_COMPANY = 25;
    public final static int DECK_SIZE = 6;
    public final static int SAFETY_SIZE = 11;

    private Cell[][] grid;
    private Map<Corporation, Integer> corporationSizes;
    private Map<Corporation, Integer> remainingStocks;
    private List<Point> remainingCells;

    private Random random;

    public Board() {
        this.grid = new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.grid[i][j] = new Cell();
            }
        }
        this.remainingStocks = initialStocks();
        this.remainingCells = initialCells();
        this.random = new Random();
    }

    /**
     * This function is only used to intialize the variable remainingStocks in
     * constructor
     * 
     * @return returns initial stocks associated to each corporation
     * @see Board
     */
    private Map<Corporation, Integer> initialStocks() {
        Map<Corporation, Integer> startingStocks = new HashMap<>();
        for (Corporation corporation : Corporation.values()) {
            startingStocks.put(corporation, INITIAL_STOCKS_PER_COMPANY);
        }
        return startingStocks;
    }


    /**
     * This function is only used to initialize the cells set in the constructor
     * 
     * @return returns all the cells of the board in a list
     * @see Board
     */
    private List<Point> initialCells() {
        List<Point> cells = new LinkedList<>();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Point cell = new Point(j, i);
                cells.add(cell);
            }
        }
        return cells;
    }

    /**
     * 
     * @param amount is the amount of cells we want to check
     * @return whether the number of remaining cells is greater or equals the amount
     *         given
     */
    public boolean enoughRemainingCells(int amount) {
        return amount <= this.remainingCells.size();
    }

    /**
     * 
     * @param x
     * @param y
     * @return the cell object at position (x, y)
     */
    public Cell getCell(int x, int y) {
        return this.grid[y][x];
    }

    /**
     * 
     * @param corporation
     * @param amount      the amount of stocks of a corporation we want to check
     * @return returns wether the number of remaining stocks of a given corporation
     *         is sufficient according to the given amount
     */
    public boolean enoughRemainingStocks(Corporation corporation, int amount) {
        int amountOfCorporationStocks = remainingStocks.get(corporation);
        return amountOfCorporationStocks >= amount;
    }

    /**
     * 
     * @return a random cell from the remaining cells list
     */
    public Point getFromRemainingCells() {
        int index = this.random.nextInt(remainingCells.size());
        Point cell = remainingCells.remove(index);
        return cell;
    }

    /**
     *
     * @param corporation the corporation we want to know the size of
     * @return given a corporation, it returns its size on the board
     */
    public int getCorporationSize(Corporation corporation) {
        int size = corporationSizes.get(corporation);
        return size;
    }

    /**
     * Sets the corporation size to the given amount
     * 
     * @param corporation
     * @param size
     */
    public void setCorporationSize(Corporation corporation, int size) {
        corporationSizes.put(corporation, size);
    }

    /**
     * Does the job of adding one cell of a corporation to the board
     * 
     * @param corporation 
     * @param position describes the position of the corporation on the board
     */
    public void addCorporationCell(Corporation corporation, Point position) {
        Cell cell = this.grid[position.getY()][position.getX()];
        int currentCorporationSize = getCorporationSize(corporation);

        cell.setCorporation(corporation);
        setCorporationSize(corporation, currentCorporationSize + 1);
    }

    /**
     * Removes one stock from a given corporation
     * 
     * @param corporation
     */
    public void removeFromRemainingStocks(Corporation corporation) {
        int numberOfStocks = remainingStocks.get(corporation);
        numberOfStocks--;
        remainingStocks.put(corporation, numberOfStocks);
    }

    /**
     * 
     * @param corporation
     * @return whether the given corporation is safe or not
     */
    public boolean corporationIsSafe(Corporation corporation) {
        return getCorporationSize(corporation) >= SAFETY_SIZE;
    }

    /**
     * This function is only used in the function that calculates the size of
     * a corporation
     * 
     * @param cell
     * @return returns all the adjacent cells to cell
     * @see #auxCalculateCorporationSize(Corporation, Point, List)
     */
    private List<Point> adjacentCells(Point cell) {
        List<Point> adjacentCells = new LinkedList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    Point adjacent = new Point(j + cell.getX(), i + cell.getY());
                    if (adjacent.isInBounds(BOARD_WIDTH, BOARD_HEIGHT)) {
                        adjacentCells.add(adjacent);
                    }
                }
            }
        }

        return adjacentCells;
    }

    /**
     * This is an auxiliary function used to calculate the size of a corporation on board
     * The algorithm used in this function is based on DFS graph algorithm (Depth First Search)
     * 
     * @param corporation represents the given corporation we want to calculate size for
     * @param currentPoint represents the point we arrived to during the depth search
     * @param visitedCells stocks all the visited cells in previous iterations
     * @return the size of a corporation from a point and its adjacents
     * @see #calculateCorporationSize(Corporation, Point)
     */
    private int auxCalculateCorporationSize(Corporation corporation, Point currentPoint, List<Point> visitedCells) {
        visitedCells.add(currentPoint);
        List<Point> adjacentCells = adjacentCells(currentPoint);
        int numberOfCells = 1;

        for (Point adj : adjacentCells) {
            if (grid[adj.getY()][adj.getX()].getCorporation() == corporation && (!visitedCells.contains(adj))) {
                numberOfCells += auxCalculateCorporationSize(corporation, adj, visitedCells);
            }
        }

        return numberOfCells;
    }


    /**
     * This is the main function that calculates the size of a corporation on board
     * The purpose of this function is for example, to update the stocken information
     * related to the size of the given corporation
     * 
     * @param corporation
     * @param startingPoint represents the starting point from where the corporation size will
     * be calculated
     * @return the size of a corporation on board according to the board
     */
    public int calculateCorporationSize(Corporation corporation, Point startingPoint) {
        Cell startingCell = this.grid[startingPoint.getY()][startingPoint.getX()];
        if (startingCell.getCorporation() != corporation) {
            return 0;
        }
        List<Point> visitedCells = new LinkedList<>();
        return auxCalculateCorporationSize(corporation, startingPoint, visitedCells);
    }

    /**
     * 
     * @param corporation
     * @return returns current stock price of given corporation
     */
    public int getStockPrice(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        int stockPrice = ReferenceChart.getStockPrice(corporation, corporationSize);
        return stockPrice;
    }

    /**
     * 
     * @param corporation
     * @return returns majority sharehold for given corporation
     */
    public int getMajoritySharehold(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        int stockPrice = ReferenceChart.getMajoritySharehold(corporation, corporationSize);
        return stockPrice;
    }

    /**
     * 
     * @param corporation
     * @return returns minority sharehold for given corporation
     */
    public int getMinoritySharehold(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        int stockPrice = ReferenceChart.getMinoritySharehold(corporation, corporationSize);
        return stockPrice;
    }

    @Override
    public String toString() {
        String board = "";

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board += this.grid[i][j].toString() + " ";
            }
            board += "\n";
        }
        
        return board;
    }

}