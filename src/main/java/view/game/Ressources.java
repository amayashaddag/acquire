package view.game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
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
     * Use with the following name
     * convention : same name as the image file
     * but in UPPER CASE, and replace '-' by '_'.
     *
     * ! Specials characters are not allowed (except
     * '-' and '_')!
     */
    public class Assets {
        static {
            File dir = new File(MAIN_PATH + RESSOURCES_PATH + IMAGES_PATH);

            Class<?> clazz = Assets.class;
            Field[] fields = clazz.getFields();

            for (Field field : fields) {
                if (field.getType() == Image.class && Modifier.isStatic(field.getModifiers())) {
                    String nameVar = field.getName().toLowerCase();
                    String nameImg = nameVar.replace('_', '-');

                    try {
                        File[] filesFounded = dir.listFiles((dir1, name) -> name.startsWith(nameImg+"."));

                        if (filesFounded == null || filesFounded.length == 0)
                            throw new IllegalArgumentException("The file " + nameImg + " doesn't exists.");

                        Image img = null;
                        for (File f: filesFounded) {
                            try {
                                img = ImageIO.read(f);
                                break;
                            } catch (IOException e) {
                                // Continue, we are looking for the first Load who match with nameImg
                            }
                        }

                        field.set(null, img);

                        if (filesFounded.length > 1)
                            throw new IllegalArgumentException("More than one file correspond to " + nameImg + ".");

                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        System.err.println("An error ocured during the setting of " + field.getName());
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
        public static Image BLUE_TOWER_CELL;
        public static Image CYAN_TOWER_CELL;
        public static Image GREEN_TOWER_CELL;
        public static Image ORANGE_TOWER_CELL;
        public static Image PURPLE_TOWER_CELL;
        public static Image RED_TOWER_CELL;
        public static Image YELLOW_TOWER_CELL;
    }
}
