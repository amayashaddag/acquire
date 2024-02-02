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
        this.grid = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                this.grid[i][j] = new Cell();
            }
        }
        this.remainingStocks = initialStocks();
        this.remainingCells = initialCells();
        this.random = new Random();
    }

    /**
     * This function is only used to intialize the variable remainingStocks in constructor
     * @return returns initial stocks associated to each corporation
     */
    private Map<Corporation, Integer> initialStocks() {
        Map<Corporation, Integer> startingStocks = new HashMap<>();
        for (Corporation corporation : Corporation.allCorporations()) {
            startingStocks.put(corporation, INITIAL_STOCKS_PER_COMPANY);
        }
        return startingStocks;
    }


    /**
     * This function is only used to initialize the cells set in the constructor
     * @return returns all the cells of the board in a list
     */
    private List<Point> initialCells() {
        List<Point> cells = new LinkedList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                Point cell = new Point(i, j);
                cells.add(cell);
            }
        }
        return cells;
    }

    /**
     * 
     * @param amount is the amount of cells we want to check
     * @return whether the number of remaining cells is greater or equals the amount given
     */
    public boolean enoughRemainingCells(int amount) {
        return amount <= this.remainingCells.size();
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
     * 
     * @param corporation
     * @return whether the given corporation is safe or not
     */
    public boolean corporationIsSafe(Corporation corporation) {
        return getCorporationSize(corporation) >= SAFETY_SIZE;
    }

    /**
     * 
     * @param cell 
     * @return returns all the adjacent cells to cell
     */
    private List<Point> adjacentCells(Point cell) {
        List<Point> adjacentCells = new LinkedList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    Point adjacent = new Point(i + cell.getX(), j + cell.getY());
                    if (adjacent.isInBounds(BOARD_WIDTH, BOARD_HEIGHT)) {
                        adjacentCells.add(adjacent);
                    }
                }
            }
        }

        return adjacentCells;
    }

}