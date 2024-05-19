package view.Components;

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
public class ColorableArcableFlatBorder extends FlatBorder {
    Color color; 
    int arc;

    public ColorableArcableFlatBorder() {
        this(null, 0);
    }

    public ColorableArcableFlatBorder(Color c, int arc) {
        super();
        this.arc = arc;
        this.color = c;
    }

    public ColorableArcableFlatBorder(Color c) {
        super();
        this.color = c;
    }

    public ColorableArcableFlatBorder(int arc) {
        super();
        this.arc = arc;
    }

    public Color getColor() {
        return color;
    }

    public int getArc() {
        return arc;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setArc(int arc) {
        this.arc = arc;
    }

    @Override
    protected Color getOutlineColor(Component c) {
        return this.color;
    }

    @Override
    protected int getArc(Component c) {
        return arc;
    }
}
