package model;

public class Cell {

    private CellState cellState;
    private Corporation currentCorporation;

    public enum CellState {
        EMPTY,
        OWNED,
        DEAD
    }

    public Cell() {
        this.cellState = CellState.EMPTY;
        this.currentCorporation = null;
    }

    public boolean isEmpty() {
        return cellState == CellState.EMPTY;
    }

    public boolean isDead() {
        return cellState == CellState.DEAD;
    }

    public boolean isOwned() {
        return cellState == CellState.OWNED;
    }

    public Corporation getCorporation() {
        return currentCorporation;
    }

    public void setCorporation(Corporation corporation) {
        this.cellState = CellState.OWNED;
        this.currentCorporation = corporation;
    }
}
