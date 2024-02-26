package view.game;

import java.awt.Image;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.swing.ImageIcon;

/**
 * All images used for game are ir
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class Ressources {
    public static final String MAIN_PATH = "src/main/";
    public static final String RESSOURCES_PATH = "ressources/";
    public static final String IMAGES_PATH ="images/game/";

    /**
     * If you want to add an Image,
     * it must be a png and you must define
     * a public static Image with the following
     * name convention : same name as the png file
     * but in UPPER CASE, and replace '-' by '_'.
     */
    public class Assets {
        /**
         * Please do not modify !
         */
        static {
            Class<?> clazz = Assets.class;
            Field[] fields = clazz.getFields();

            for (Field field : fields) {
                if (field.getType() == Image.class && Modifier.isStatic(field.getModifiers())) {
                    String nameVar = field.getName().toLowerCase();
                    String nameImg = nameVar.replace('_', '-');
                    ImageIcon ico = new ImageIcon(MAIN_PATH + RESSOURCES_PATH + IMAGES_PATH + nameImg + ".png");
                    try {
                        field.set(null, ico.getImage());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static Image BACKGROUND;
        public static Image GRASS;
        public static Image BOARD_CELL;
        public static Image EMPTY_CELL;
        public static Image OCCUPIED_CELL;
        public static Image SELECTED_CELL;
    }
}
