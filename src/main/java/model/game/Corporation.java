package model.game;

public enum Corporation {
    WORLDWIDE,
    SACKSON,
    FESTIVAL,
    IMPERIAL,
    CONTINENTAL,
    TOWER,
    AMERICAN;

    @Override
    public String toString() {
        switch(this) {
            case AMERICAN: return "Red";
            case CONTINENTAL: return "Purple";
            case FESTIVAL: return "Yellow";
            case IMPERIAL: return "Cyan";
            case SACKSON: return "Blue";
            case TOWER: return "Green";
            case WORLDWIDE: return "Orange";
            default: return "";
        }
    }

    public static Corporation getCorporationFromName(String c) {
        if (AMERICAN.toString().equals(c)) {
            return AMERICAN;
        }

        if (CONTINENTAL.toString().equals(c)) {
            return CONTINENTAL;
        }

        if (FESTIVAL.toString().equals(c)) {
            return FESTIVAL;
        }

        if (IMPERIAL.toString().equals(c)) {
            return IMPERIAL;
        }

        if (SACKSON.toString().equals(c)) {
            return SACKSON;
        }

        if (TOWER.toString().equals(c)) {
            return TOWER;
        }

        if (WORLDWIDE.toString().equals(c)) {
            return WORLDWIDE;
        }

        return null;
    }
}

