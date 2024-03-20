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
            case AMERICAN: return "American";
            case CONTINENTAL: return "Continental";
            case FESTIVAL: return "Festival";
            case IMPERIAL: return "Imperial";
            case SACKSON: return "Sackson";
            case TOWER: return "Tower";
            case WORLDWIDE: return "Worldwide";
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

