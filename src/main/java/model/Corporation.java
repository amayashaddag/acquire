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

    // Fonction pour initialiser toutes les corporations
    public static Set<Corporation> setAllCorporations() {
        return EnumSet.allOf(Corporation.class);
    }
}

