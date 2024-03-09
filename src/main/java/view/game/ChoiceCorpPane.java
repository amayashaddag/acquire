package view.game;

import javaswingdev.pggb.PanelGlowingGradient;
import model.Corporation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @see GameView#getCorporationChoice(List)
 * @author Arthur Deck
 * @version 1
 */
public class ChoiceCorpPane extends JComponent {
    private tools.Box<Corporation> monitor;
    public ChoiceCorpPane(List<Corporation> corps, tools.Box<Corporation> monitor) {
        this.monitor = monitor;

        setOpaque(false);
        setLayout(new MigLayout("al center, filly, ins 0, wrap 4"));

        for (Corporation corp : corps)
            add(new ChoiceGlowingItemCorp(corp), "w 16%, h 33%");
    }

    private void close() {
        Container parent = getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }

    private class ChoiceGlowingItemCorp extends GlowingItemCorp {
        public ChoiceGlowingItemCorp(Corporation corp) {
            super(corp);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    monitor.set(getCorp());
                    synchronized (monitor) {
                        monitor.notify();
                    }
                    close();
                }
                public void mousePressed(MouseEvent mouseEvent) {}
                public void mouseReleased(MouseEvent mouseEvent) {}
                public void mouseEntered(MouseEvent mouseEvent) {}
                public void mouseExited(MouseEvent mouseEvent) {}
            });
        }
    }
}
