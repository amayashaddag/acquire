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
}

