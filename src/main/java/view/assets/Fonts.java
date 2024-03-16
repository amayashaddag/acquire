package view.assets;

import java.awt.Font;
import java.io.File;

public class Fonts {
    public final static String FONTS_REPOSITORY = "src/main/ressources/fonts/";

    public final static Font TITLE_FONT;
    public final static Font REGULAR_PARAGRAPH_FONT;
    public final static Font BOLD_PARAGRAPH_FONT;

    public final static int PARAGRAPH_FONT_SIZE = 16;
    public final static int TITLE_FONT_SIZE = 32;

    public final static String REGULAR_FONT_PATH = FONTS_REPOSITORY + "regular.ttf";
    public final static String BOLD_FONT_PATH = FONTS_REPOSITORY + "bold.ttf";

    public final static String BASIC_FONT_IF_ERROR = "Arial";

    static {
        Font regularFont, boldFont;
        try {
            File boldFontFile = new File(BOLD_FONT_PATH);
            File regularFontFile = new File(REGULAR_FONT_PATH);
            boldFont = Font.createFont(Font.TRUETYPE_FONT, boldFontFile);
            regularFont = Font.createFont(Font.TRUETYPE_FONT, regularFontFile);
        } catch (Exception e) {
            boldFont = new Font(BASIC_FONT_IF_ERROR, Font.BOLD, TITLE_FONT_SIZE);
            regularFont = new Font(BASIC_FONT_IF_ERROR, Font.PLAIN, TITLE_FONT_SIZE);
        }

        TITLE_FONT = boldFont.deriveFont(Font.BOLD, TITLE_FONT_SIZE);
        REGULAR_PARAGRAPH_FONT = regularFont.deriveFont(Font.PLAIN, PARAGRAPH_FONT_SIZE);
        BOLD_PARAGRAPH_FONT = boldFont.deriveFont(Font.BOLD, PARAGRAPH_FONT_SIZE);
    }
}
