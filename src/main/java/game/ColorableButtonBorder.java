package game;

import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.Color;
import java.awt.Component;

public class ColorableButtonBorder extends FlatBorder {
    Color color;

    public ColorableButtonBorder(Color c) {
        super();
        this.color = c;
    }

    @Override
    protected Color getOutlineColor(Component c) {
        return this.color;
    }
}
