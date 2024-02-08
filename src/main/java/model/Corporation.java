package model;

public enum Corporation {
    WORLDWIDE,
    SACKSON,
    FESTIVAL,
    IMPERIAL,
    CONTINENTAL,
    TOWER,
    AMERICAN,
    NONE;

    @Override
    public String toString() {
        switch(this) {
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
}

