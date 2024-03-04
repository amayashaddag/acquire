package model;

public class ReferenceChart {
    public static final int FIRST_CATEGORY_STARTING_STOCK_PRICE = 200;
    public static final int SECOND_CATEGORY_STARTING_STOCK_PRICE = 300;
    public static final int THIRD_CATEGORY_STARTING_STOCK_PRICE = 400;
    public static final int STOCK_PRICE_INCREASING_STEP = 100;

    public static final int FIRST_CATEGORY_STARTING_MAJORITY_SHAREHOLD = 2000;
    public static final int SECOND_CATEGORY_STARTING_MAJORITY_SHAREHOLD = 3000;
    public static final int THIRD_CATEGORY_STARTING_MAJORITY_SHAREHOLD = 4000;
    public static final int MAJORITY_SHAREHOLD_INCREASING_STEP = 1000;

    public static final int FIRST_CATEGORY_STARTING_MINORITY_SHAREHOLD = 1000;
    public static final int SECOND_CATEGORY_STARTING_MINORITY_SHAREHOLD = 1500;
    public static final int THIRD_CATEGORY_STARTING_MINORITY_SHAREHOLD = 2000;
    public static final int MINORITY_SHAREHOLD_INCREASING_STEP = 500;

    private static final Corporation[] firstCategoryCorporations = {
        Corporation.WORLDWIDE, 
        Corporation.SACKSON
    };
    private static final Corporation[] secondCategoryCorporations = {
        Corporation.FESTIVAL,
        Corporation.IMPERIAL,
        Corporation.AMERICAN
    };

    @FunctionalInterface
    private static interface SizePredicate {
        boolean test(int size);
    }

    /* This array represents the set of predicates that a corporation size 
     * should satisfy to get information about its stock price and shareholds
     */
    private static final SizePredicate[] predicates = {
        (size) -> size == 2,
        (size) -> size == 3,
        (size) -> size == 4,
        (size) -> size == 5,
        (size) -> size >= 6 && size <= 10,
        (size) -> size >= 11 && size <= 20,
        (size) -> size >= 21 && size <= 30,
        (size) -> size >= 31 && size <= 40,
        (size) -> size >= 41
    };

    /**
     * This function is used to calculate the stock price and the shareholds of given companies
     * 
     * @param size represents the given size in board of a corporation
     * @return the ranking of the size of the corporation in the reference chart
     * as an int from 0 to 8
     * @see #getStockPrice(Corporation, int)
     * @see #getMajoritySharehold(Corporation, int)
     * @see #getMinoritySharehold(Corporation, int)
     */
    private static int getCorporationSizeRanking(int size) {
        for (int i = 0; i < predicates.length; i++) {
            SizePredicate predicate = predicates[i];
            if (predicate.test(size)) {
                return i;
            }
        }
        return 0;
    }

    private static boolean contains(Corporation[] corporations, Corporation corporation) {
        for (Corporation c : corporations) {
            if (c == corporation) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the purchasing stock price of the current corporation given according
     * to its size on the board
     */
    public static int getStockPrice(Corporation corporation, int size) {
        int rankingSize = getCorporationSizeRanking(size), startingPrice;
        
        if (contains(firstCategoryCorporations, corporation)) {
            startingPrice = FIRST_CATEGORY_STARTING_STOCK_PRICE;
        } else if (contains(secondCategoryCorporations, corporation)) {
            startingPrice = SECOND_CATEGORY_STARTING_STOCK_PRICE;
        } else {
            startingPrice = THIRD_CATEGORY_STARTING_STOCK_PRICE;
        }

        return startingPrice + STOCK_PRICE_INCREASING_STEP * rankingSize;
    }

    /**
     * @return returns the majority sharehold of a given corporation according to its size
     * on the board
     */
    public static int getMajoritySharehold(Corporation corporation, int size) {
        int rankingSize = getCorporationSizeRanking(size), startingPrice;

        if (contains(firstCategoryCorporations, corporation)) {
            startingPrice = FIRST_CATEGORY_STARTING_MAJORITY_SHAREHOLD;
        } else if (contains(secondCategoryCorporations, corporation)) {
            startingPrice = SECOND_CATEGORY_STARTING_MAJORITY_SHAREHOLD;
        } else {
            startingPrice = THIRD_CATEGORY_STARTING_MAJORITY_SHAREHOLD;
        }

        return startingPrice + MAJORITY_SHAREHOLD_INCREASING_STEP * rankingSize;
    }

    /**
     * @return returns the minority sharehold of a given corporation according to its size
     */
    public static int getMinoritySharehold(Corporation corporation, int size) {
        int rankingSize = getCorporationSizeRanking(size), startingPrice;

        if (contains(firstCategoryCorporations, corporation)) {
            startingPrice = FIRST_CATEGORY_STARTING_MINORITY_SHAREHOLD;
        } else if (contains(secondCategoryCorporations, corporation)) {
            startingPrice = SECOND_CATEGORY_STARTING_MINORITY_SHAREHOLD;
        } else {
            startingPrice = THIRD_CATEGORY_STARTING_MINORITY_SHAREHOLD;
        }

        return startingPrice + MINORITY_SHAREHOLD_INCREASING_STEP * rankingSize;
    }
}
