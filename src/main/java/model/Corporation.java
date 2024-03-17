package model;

import java.util.ArrayList;
import java.util.List;

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

