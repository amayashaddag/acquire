package game;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * @author Arthur Deck
 * @version 0.1
 */
public class GameRessources {
    public static final String MAIN_PATH = "src/main/";
    public static final String RESSOURCES_PATH = "ressources/";
    public static final String IMAGES_PATH ="images/game/";

    public class Assets {
        static {
            ImageIcon ico = new ImageIcon(MAIN_PATH + RESSOURCES_PATH + IMAGES_PATH + "background.jpg");
            BACKGROUND = ico.getImage();

            ico = new ImageIcon(MAIN_PATH+RESSOURCES_PATH + IMAGES_PATH + "grass.png");
            GRASS = ico.getImage();
        }

        public static final Image BACKGROUND;
        public static final Image GRASS;
    }
}
