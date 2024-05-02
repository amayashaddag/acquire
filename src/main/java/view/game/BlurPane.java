package view.game;

import model.tools.AutoSetter;
import view.window.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingUtilities;
import java.awt.Component;

/**
 * @author Arthur Deck
 */
@SuppressWarnings("serial")
@AutoSetter(typeParam = GameView.class)
public class BlurPane extends JPanel {
    public BlurPane(Form form) {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 190));
        ((GameFrame) SwingUtilities.getWindowAncestor(form)).setGlassPane(this);
        this.form = form;
        setLayout(new MigLayout("al center, filly"));
    }

    final Form form;

    @Override
    public final void paint(Graphics g) {
        final Color old = g.getColor();
        g.setColor(getBackground());
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(old);
        super.paint(g);
    }

    public void blur(boolean b) {
        ((GameFrame) SwingUtilities.getWindowAncestor(form)).getGlassPane().setVisible(b);
    }

    public void blurWith(Component panel) {
        blur(true);
        removeAll();
        add(panel);
        repaint();
    }
}
