package game;

import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.Color;
import java.awt.Component;

/**
 * A colored border which keep the FlatLaf style
 * 
 * @author Arthur Deck
 * @version 1
 * @see com.formdev.flatlaf.ui.FlatBorder
 */
public class ColorableFlatBorder extends FlatBorder {
    Color color; 

    public ColorableFlatBorder(Color c) {
        super();
        this.color = c;
    }

    @Override
    protected Color getOutlineColor(Component c) {
        return this.color;
    }
}
