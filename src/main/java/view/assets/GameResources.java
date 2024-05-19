package view.assets;

import model.game.Corporation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;

/**
 * All ressouces used for in game and some utils methods.
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class GameResources {
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
     * @apiNote all about game's images (ressources/utils)
     */
    public static class GImage {
        static {
            File dir = new File(MAIN_PATH + RESSOURCES_PATH + IMAGES_PATH);

            Class<?> clazz = GImage.class;
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
                                // Continue, we are looking for the first image witch match with nameImg
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
                case IMPERIAL -> GameResources.GImage.CYAN_TOWER_CELL;
                case FESTIVAL -> GameResources.GImage.YELLOW_TOWER_CELL;
                case AMERICAN -> GameResources.GImage.RED_TOWER_CELL;
                case SACKSON -> GameResources.GImage.BLUE_TOWER_CELL;
                case TOWER -> GameResources.GImage.GREEN_TOWER_CELL;
                case WORLDWIDE -> GameResources.GImage.ORANGE_TOWER_CELL;
                case CONTINENTAL -> GameResources.GImage.PURPLE_TOWER_CELL;
            };
        }

        public static void saveImage(BufferedImage image, String filename) {
            try {
                File file = new File(filename + ".png");
                ImageIO.write(image, "png", file);
                System.out.println("Image saved as " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static BufferedImage applyGaussianBlur(BufferedImage srcImage, int radius, int iterations) {
            int size = radius * 2 + 1;
            float[] data = new float[size * size];
            float totalWeight = 0.0f;
            int center = size / 2;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int dx = i - center;
                    int dy = j - center;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    float gaussian = (float) Math.exp(-(distance * distance) / (2 * radius * radius));
                    data[i * size + j] = gaussian;
                    totalWeight += gaussian;
                }
            }
    
            for (int i = 0; i < data.length; i++) {
                data[i] /= totalWeight;
            }
    
            Kernel kernel = new Kernel(size, size, data);
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage result = srcImage;
            for (int i = 0; i < iterations; i++) {
                result = op.filter(result, null);
            }
            return result;
        }    
    }

    public static Color getColor(String s) {
        return MenuResources.getColor(s);
    }

    public static Color getCorpColor(Corporation c) {
        Image img = GImage.getCorpImage(c);
        BufferedImage bfi = GameResources.imageToBufferedImage(img);
        int clr = bfi.getRGB(img.getWidth(null)/2,img.getHeight(null)* 3/4);
        return new Color(clr, true);
    }
}
