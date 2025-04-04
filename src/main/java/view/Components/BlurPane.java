package view.Components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.tools.AutoSetter;

/**
 * @author Arthur Deck
 */
@SuppressWarnings("serial")
@AutoSetter(typeParam = Form.class)
public class BlurPane extends JPanel {
    public BlurPane() {super();}

    public BlurPane(Form form) {
        super();
        init(form);
    }

    public void init(Form form) {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 190));
        this.form = form;
        this.frame = ((JFrame) SwingUtilities.getWindowAncestor(form));
        frame.setGlassPane(this);
    }

    Form form;
    JFrame frame;

    public JFrame getJFrame() {
        return frame;
    }

    @Override
    public final void paint(Graphics g) {
        final Color old = g.getColor();
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(old);
        super.paint(g);
    }

    public void blur(boolean b) {
        this.setVisible(b);
    }

    public void blurWith(Component panel) {
        blur(true);
        removeAll();
        add(panel);
        repaint();
    }

    public boolean isBlur() {return this.isVisible();}
}
