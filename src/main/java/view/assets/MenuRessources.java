package view.assets;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

/**
 * All ressouces used for Menu
 *
 * @author Arthur Deck
 * @version 0.1
 */
public class MenuRessources {
    public static final String MAIN_PATH = "src/main/";
    public static final String RESSOURCES_PATH = "ressources/";
    public static final String IMAGES_PATH = "images/menu/";

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
    }
}
