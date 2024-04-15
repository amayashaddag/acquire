package model.game;

public class Cell {

    private CellState cellState;
    private Corporation currentCorporation;

    private enum CellState {
        EMPTY,
        OCCUPIED,
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

    public boolean isOccupied() {
        return cellState == CellState.OCCUPIED;
    }

    public Corporation getCorporation() {
        return currentCorporation;
    }

    public void setCorporation(Corporation corporation) {
        this.cellState = CellState.OWNED;
        this.currentCorporation = corporation;
    }

    public void setAsOccupied() {
        this.cellState = CellState.OCCUPIED;
    }

    public void setAsDead() {
        this.cellState = CellState.DEAD;
    }

    public String toString() {
        switch (cellState) {
            case EMPTY: return ".";
            case DEAD : return "*";
            case OCCUPIED : return "+";
            case OWNED : {
                switch (currentCorporation) {
                    case AMERICAN: return "A";
                    case CONTINENTAL: return "C";
                    case FESTIVAL: return "F";
                    case IMPERIAL: return "I";
                    case SACKSON: return "S";
                    case TOWER: return "T";
                    case WORLDWIDE: return "W";
                    default: return "";
                }
            }
            default : return "";
        }
    }
     @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    } 
}
