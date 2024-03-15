package view.game;

import model.Corporation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;

/**
 * All images used for game are ir
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class Ressources {
    public static final String MAIN_PATH = "src/main/";
    public static final String RESSOURCES_PATH = "ressources/";
    public static final String IMAGES_PATH = "images/game/";

    public static BufferedImage imageToBufferedImage(Image im) {
        BufferedImage bi = new BufferedImage
                (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = bi.createGraphics();
        g2D.drawImage(im, 0, 0, null);
        g2D.dispose();
        return bi;
    }

    /**
     * Use with the following name
     * convention : same name as the image file
     * but in UPPER CASE, and replace '-' by '_'.
     * <p>
     * ! Specials characters are not allowed (except
     * '-' and '_')!
     */
    public class Assets {
        static {
            File dir = new File(MAIN_PATH + RESSOURCES_PATH + IMAGES_PATH);

            Class<?> clazz = Assets.class;
            Field[] fields = clazz.getFields();

            for (Field field : fields) {
                if (field.getType() == Image.class) {
                    String nameVar = field.getName().toLowerCase();
                    String nameImg = nameVar.replace('_', '-');

                    try {
                        File[] filesFounded = dir.listFiles((dir1, name) -> name.startsWith(nameImg + "."));

                        if (filesFounded == null || filesFounded.length == 0)
                            throw new IllegalArgumentException("The file " + nameImg + " doesn't exists.");

                        Image img = null;
                        for (File f : filesFounded) {
                            try {
                                img = ImageIO.read(f);
                                break;
                            } catch (IOException e) {
                                // Continue, we are looking for the first AutoLoadProcessor who match with nameImg
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

        public static Image getCorpImage(Corporation c) {
            return switch (c) {
                case IMPERIAL -> Ressources.Assets.CYAN_TOWER_CELL;
                case FESTIVAL -> Ressources.Assets.YELLOW_TOWER_CELL;
                case AMERICAN -> Ressources.Assets.RED_TOWER_CELL;
                case SACKSON -> Ressources.Assets.BLUE_TOWER_CELL;
                case TOWER -> Ressources.Assets.GREEN_TOWER_CELL;
                case WORLDWIDE -> Ressources.Assets.ORANGE_TOWER_CELL;
                case CONTINENTAL -> Ressources.Assets.PURPLE_TOWER_CELL;
            };
        }

        public static Color getCorpColor(Corporation c) {
            Image img = getCorpImage(c);
            BufferedImage bfi = Ressources.imageToBufferedImage(img);
            int clr = bfi.getRGB(img.getWidth(null)/2,img.getHeight(null)* 3/4);
            return new Color(clr, true);
        }
    }
}
