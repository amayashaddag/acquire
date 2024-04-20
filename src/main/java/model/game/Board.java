package model.game;

import java.util.*;
import java.util.function.Function;
import java.util.function.BiFunction;

import model.tools.Point;

public class Board {
    public final static int BOARD_WIDTH = 9, BOARD_HEIGHT = 9;
    public final static int INITIAL_STOCKS_PER_COMPANY = 25;
    public final static int DECK_SIZE = 6;
    public final static int SAFETY_SIZE = 11;

    public final static int WINNING_CORPORATION_SIZE = 41;
    public final static int MAXIMUM_AMOUNT_OF_BUYING_STOCKS = 3;

    private  Cell[][] grid;
    private  Map<Corporation, Integer> corporationSizes;
    private  Map<Corporation, Integer> remainingStocks;
    private  List<Point> remainingCells;

    public Board() {
        this.grid = new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.grid[i][j] = new Cell();
            }
        }
        this.corporationSizes = initialSizes();
        this.remainingStocks = initialStocks();
        this.remainingCells = initialCells();
    }

    // TODO : To delete
    public Map<Corporation, Integer> getCorporationSizes() {
        return corporationSizes;
    }

    /**
     * This function is only used to initialize the variable remainingStocks in
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
        Collections.shuffle(cells);
        return cells;
    }

    private Map<Corporation, Integer> initialSizes() {
        Map<Corporation, Integer> initialSizes = new HashMap<>();
        Integer initialSize = 0;

        for (Corporation corporation : Corporation.values()) {
            initialSizes.put(corporation, initialSize);
        }

        return initialSizes;
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

    public Cell getCell(Point cellPosition) {
        return this.grid[cellPosition.getY()][cellPosition.getX()];
    }

    /**
     * Surcharged method of {@link #getCell(Point)}
     */
    public Cell getCell(int x, int y) {
        return this.grid[y][x];
    }

    // TODO : To remove after finishing tests
    public List<Point> getRemainingCells() {
        return remainingCells;
    }

    /**
     * 
     * @param corporation
     * @param amount      the amount of stocks of a corporation we want to check
     * @return returns whether the number of remaining stocks of a given corporation
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
        Point chosenCell = null;

        for (Point p : remainingCells) {
            if (canPlaceIn(p)) {
                chosenCell = p;
                break;
            }
        }

        if (chosenCell != null) {
            remainingCells.remove(chosenCell);
        }

        return chosenCell;
    }
    public Map<Corporation, Integer> getRemainingStocks() {
        return remainingStocks;
    }

    /**
     *
     * @param corporation the corporation we want to know the size of
     * @return given a corporation, it returns its size on the board
     */
    public int getCorporationSize(Corporation corporation) {
        return corporationSizes.get(corporation);
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
     */
    public void replaceCellCorporation(Cell cell, Corporation newCorporation) {
        if (cell.isOwned()) {
            Corporation oldCorporation = cell.getCorporation();
            int oldCorporationSize;

            oldCorporationSize = getCorporationSize(oldCorporation);
            setCorporationSize(oldCorporation, oldCorporationSize - 1);
        }
        int newCorporationSize = getCorporationSize(newCorporation);

        setCorporationSize(newCorporation, newCorporationSize + 1);
        cell.setCorporation(newCorporation);
    }

    /**
     * Removes a number of stocks from a given corporation
     * 
     * @param corporation
     */
    public void removeFromRemainingStocks(Corporation corporation, int amount) {
        int numberOfStocks = remainingStocks.get(corporation);
        numberOfStocks -= amount;
        remainingStocks.put(corporation, numberOfStocks);
    }

    /**
     * Adds a number of stocks to a given corporation
     * 
     * @param corporation
     * @param amount
     */
    public void addToRemainingStocks(Corporation corporation, int amount) {
        int numberOfStocks = remainingStocks.get(corporation);
        numberOfStocks += amount;
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
     * This function returns the list of the adjacent cells to a given cell
     * 
     * @param cell
     * @return returns all the adjacent cells to cell
     * @see #mappingDFS(Corporation, Point, List, Function)
     * @see #foldingDFS(Corporation, Point, List, BiFunction, Object)
     */
    public Set<Point> adjacentCells(Point cell, Function<Cell, Boolean> mapper) {
        Set<Point> adjacentCells = new HashSet<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 ^ j != 0) {
                    Point adjacent = new Point(j + cell.getX(), i + cell.getY());
                    if (adjacent.isInBounds(BOARD_WIDTH, BOARD_HEIGHT)) {
                        Cell adjacentCell = getCell(adjacent);
                        if (mapper.apply(adjacentCell)) {
                            adjacentCells.add(adjacent);
                        }
                    }
                }
            }
        }

        return adjacentCells;
    }

    /**
     * This function is a dfs graph algorithm that maps all the cells of the board
     * that belong to a given company with the given function
     * 
     * @param corporation  corporation to test if the cells belong to it
     * @param currentPoint the current point in the algorithm iteration
     * @param visitedCells contains the set of already visited cells (to not revisit
     *                     them)
     * @param op           the operation that maps the cells
     */
    public void mappingDFS(Corporation corporation, Point currentPoint, List<Point> visitedCells,
            Function<Point, Void> op) {
        visitedCells.add(currentPoint);
        op.apply(currentPoint);

        Set<Point> adjacentCells = adjacentCells(currentPoint, (cell) -> true);

        for (Point adj : adjacentCells) {
            Cell cell = getCell(adj);

            if ((cell.getCorporation() == corporation || cell.isOccupied())
                    && !(visitedCells.contains(adj))) {
                mappingDFS(corporation, adj, visitedCells, op);
            }
        }
    }

    /**
     * This function is a folding dfs graph algorithm that folds all the reachable
     * cells that belong
     * to a given corporation with a given function f and return the result
     * 
     * @param <U>          generic returning type parameter
     * @param corporation  corporation to test the cells on
     * @param currentPoint current point in each visiting iteration
     * @param visitedCells contains all the already-visited cells to not revisit
     *                     them
     * @param op           the operation to fold the cells on, if we consider this
     *                     function as :
     *                     f : U x U -> U, the ending of the algorithm returns
     *                     f(f(f(f(...,...),...),...),...)
     * @param initialValue the initial value of base case
     * @return a U type data that matches to the definition of the folding function
     * 
     */
    public <U> U foldingDFS(Corporation corporation, Point currentPoint, List<Point> visitedCells,
            BiFunction<U, U, U> op, U initialValue) {
        visitedCells.add(currentPoint);
        Set<Point> adjacentCells = adjacentCells(currentPoint, (cell) -> true);
        U value = initialValue;

        for (Point adj : adjacentCells) {
            Cell cell = getCell(adj);
            if (cell.getCorporation() == corporation && !(visitedCells.contains(adj))) {
                value = op.apply(value, foldingDFS(corporation, adj, visitedCells, op, initialValue));
            }
        }

        return value;
    }

    /**
     * This function sets all the reachable cells that belong to the same
     * corporation starting
     * from a given point to a given corporation
     * 
     * @param corporation   the given corporation to transform cells into
     * @param startingPoint the point where the setting will start from
     * @see #mappingDFS(Corporation, Point, List, Function)
     */
    public Map<Point, Corporation> replaceCorporationFrom(Corporation corporation, Point startingPoint) {
        Map<Point, Corporation> replacedCells = new HashMap<>();
        Cell currentCell = getCell(startingPoint);

        if (!currentCell.isOwned()) {
            return replacedCells;
        }

        Corporation currentCorporation = currentCell.getCorporation();

        List<Point> visitedCells = new LinkedList<>();
        mappingDFS(currentCorporation, startingPoint, visitedCells, (Point p) -> {
            Cell cell = getCell(p);
            replaceCellCorporation(cell, corporation);
            replacedCells.put(p, corporation);
            return null;
        });

        return replacedCells;
    }

    /**
     *
     * @return returns current stock price of given corporation
     */
    public int getStockPrice(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        return ReferenceChart.getStockPrice(corporation, corporationSize);
    }

    /**
     * @return returns majority sharehold for given corporation
     */
    public int getMajoritySharehold(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        return ReferenceChart.getMajoritySharehold(corporation, corporationSize);
    }

    /**
     *
     * @return returns minority sharehold for given corporation
     */
    public int getMinoritySharehold(Corporation corporation) {
        int corporationSize = getCorporationSize(corporation);
        return ReferenceChart.getMinoritySharehold(corporation, corporationSize);
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board.append(this.grid[i][j].toString()).append(" ");
            }
            board.append("\n");
        }

        return board.toString();
    }

    /**
     * Returns the adjacent owned cell positions to a given cell position in order
     * to
     * determine the action to perform on the cell
     */
    public Set<Point> adjacentOwnedCells(Point cellPosition) {
        return adjacentCells(cellPosition, Cell::isOwned);
    }

    public Set<Point> adjacentOccupiedCells(Point cellPosition) {
        return adjacentCells(cellPosition, Cell::isOccupied);
    }

    public Set<Point> adjacentEmptyCells(Point cellPosition) {
        return adjacentCells(cellPosition, Cell::isEmpty);
    }

    public Set<Point> adjacentSafeCells(Point cellPosition) {
        return adjacentCells(cellPosition, (cell) -> {
            if (!cell.isOwned()) {
                return false;
            }

            Corporation corporation = cell.getCorporation();
            return corporationIsSafe(corporation);
        });
    }

    public Set<Corporation> adjacentCorporations(Point cellPosition) {
        Set<Corporation> adjacentCorporations = new HashSet<>();
        Set<Point> adjacentOwnedCells = adjacentOwnedCells(cellPosition);

        for (Point adj : adjacentOwnedCells) {
            Cell adjacentOwnedCell = getCell(adj);
            Corporation adjacentCorporation = adjacentOwnedCell.getCorporation();

            if (!adjacentCorporations.contains(adjacentCorporation)) {
                adjacentCorporations.add(adjacentCorporation);
            }
        }

        return adjacentCorporations;
    }

    /**
     * This function updates the cells adjacent to a given cell to detect the dead ones
     */
    public void updateDeadCells() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Point cellPosition = new Point(j, i);
                Cell cell = getCell(cellPosition);

                if (cell.isEmpty()) {
                    Set<Point> adjacentSafeCells = adjacentSafeCells(cellPosition);
                    Set<Corporation> adjacentSafeCorporations = new HashSet<>();
                    int numberOfAdjacentSafeCorporations = 0;

                    for (Point adjacent : adjacentSafeCells) {
                        Cell adjacentSafeCell = getCell(adjacent);
                        Corporation adjacentSafeCorporation = adjacentSafeCell.getCorporation();

                        if (!adjacentSafeCorporations.contains(adjacentSafeCorporation)) {
                            adjacentSafeCorporations.add(adjacentSafeCorporation);
                            numberOfAdjacentSafeCorporations++;
                        }
                    }

                    if (numberOfAdjacentSafeCorporations > 1) {
                        cell.setAsDead();
                        remainingCells.remove(cellPosition);
                    }
                }
            }
        }
    }

    /**
     * @return Returns whether a player can place a cell in the given position or not
     */
    public boolean canPlaceIn(Point cellPosition) {
        Cell cell = getCell(cellPosition);

        if (!cell.isEmpty()) {
            return false;
        }

        List<Corporation> unplacedCorporations = unplacedCorporations();
        Set<Point> adjacentOccupiedCells = adjacentOccupiedCells(cellPosition);

        return adjacentOccupiedCells.isEmpty() || !unplacedCorporations.isEmpty();
    }

    public List<Corporation> unplacedCorporations() {
        List<Corporation> unplacedCorporations = new LinkedList<>();

        for (Corporation c : corporationSizes.keySet()) {
            if (corporationSizes.get(c) <= 0) {
                unplacedCorporations.add(c);
            }
        }

        return unplacedCorporations;
    }

    public boolean isGameOver() {
        boolean allCorporationsAreSafe = true;

        for (int corporationSize : corporationSizes.values()) {
            if (corporationSize < SAFETY_SIZE) {
                allCorporationsAreSafe = false;
            }

            if (corporationSize >= WINNING_CORPORATION_SIZE) {
                return true;
            }
        }

        return allCorporationsAreSafe;
    }

    public Map<Corporation, Integer> possibleBuyingStocks() {
        Map<Corporation, Integer> possibleBuyingStocks = new HashMap<>();

        for (Corporation corporation : corporationSizes.keySet()) {
            int corporationSize = corporationSizes.get(corporation);
            int remainingStocks = this.remainingStocks.get(corporation);

            if (corporationSize != 0) {
                int possibleBuyingAmount = Math.min(remainingStocks, MAXIMUM_AMOUNT_OF_BUYING_STOCKS);
                possibleBuyingStocks.put(corporation, possibleBuyingAmount);
            }
        }

        return possibleBuyingStocks;
    }

    public void updatePlayerDeck(Player player) {
        Point[] deck = player.getDeck();

        for (int i = 0; i < DECK_SIZE; i++) {
            Point oldCellPosition = deck[i];

            if (oldCellPosition != null && canPlaceIn(oldCellPosition)) {
                continue;
            }

            Point newCellPosition = null;
            for (Point possibleCellPosition : remainingCells) {
                if (canPlaceIn(possibleCellPosition)) {
                    newCellPosition = possibleCellPosition;
                    break;
                }
            }

            if (oldCellPosition != null) {
                Cell oldCell = getCell(oldCellPosition);

                if (oldCell.isEmpty()) {
                    remainingCells.add(oldCellPosition);
                }
            }

            if (newCellPosition != null) {
                remainingCells.remove(newCellPosition);
            }
            deck[i] = newCellPosition;
        }
    }

    public boolean thereArePlacedCorporations() {
        for (Corporation c : Corporation.values()) {
            if (getCorporationSize(c) > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Updates the board according to the given map in argument.
     * Used in online mode.
     * 
     * @param newPlacedCells a map of (Point, Corporation) that specifies the new placed
     * cells by other players.
     * @apiNote Corporation == null refeers to the cell being in OCCUPIED state.
     */
    public void updateNewPlacedCells(Map<Point, Corporation> newPlacedCells) {
        for (Point p : newPlacedCells.keySet()) {
            Corporation c = newPlacedCells.get(p);
            Cell cell = getCell(p);

            if (c == null) {
                cell.setAsOccupied();
            } else {
                replaceCellCorporation(cell, c);
            }
        }
    }

    @Override 
    public Object clone() throws CloneNotSupportedException {

        Cell[][] clonedGrid = new Cell[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                clonedGrid[i][j] = (Cell) this.grid[i][j].clone();
            }
        }
        
        Map<Corporation, Integer> clonedCorporationSizes = new HashMap<>(this.corporationSizes);
        Map<Corporation, Integer> clonedRemainingStocks = new HashMap<>(this.remainingStocks);
        List<Point> clonedRemainingCells = new ArrayList<>(this.remainingCells);

        
        Board clonedBoard = new Board();
        clonedBoard.grid = clonedGrid;
        clonedBoard.corporationSizes = clonedCorporationSizes;
        clonedBoard.remainingStocks = clonedRemainingStocks;
        clonedBoard.remainingCells = clonedRemainingCells;

        return clonedBoard;
    }

}