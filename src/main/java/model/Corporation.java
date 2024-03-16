package model;

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
}

