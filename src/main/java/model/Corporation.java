package model;
import java.util.EnumSet;
import java.util.Set;

public enum Corporation {
    WORLDWIDE,
    SACKSON,
    FESTIVAL,
    IMPERIAL,
    CONTINENTAL,
    TOWER,
    AMERICAN;

    /**
     * 
     * @return a set containing all corporationss
     */
    public static Set<Corporation> allCorporations() {
        return EnumSet.allOf(Corporation.class);
    }

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

