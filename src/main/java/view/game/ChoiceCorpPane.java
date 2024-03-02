package view.game;

import javaswingdev.pggb.PanelGlowingGradient;
import model.Corporation;
import net.miginfocom.layout.Grid;
import net.miginfocom.swing.MigLayout;
import view.Debug;

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
        setLayout(new MigLayout("al center, filly", "10[]10"));

        for (Corporation corp : corps)
            add(new GlowingItem(corp), "w 300, h 400");

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    private void close() {
        Container parent = getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }

    private class GlowingItem extends PanelGlowingGradient {
        private final Corporation corp;
        private final Image img;
        public GlowingItem(Corporation corp) {
            super();
            this.corp = corp;
            this.img = Ressources.Assets.getCorpImage(this.corp);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    monitor.set(corp);
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

            BufferedImage bfi = Ressources.imageToBufferedImage(img);
            int color = bfi.getRGB(img.getWidth(null)/2,img.getHeight(null)* 3/4);
            setGradientColor1(new Color(color, true).darker());
            setGradientColor2(new Color(color, true).brighter());
            setBackground(new Color(color,true).darker());
            setBackgroundLight(new Color(color,true).brighter());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int bordShadSize = getShadowSize() + getBorderSize() + 5; // 5 for spacing
            g.drawImage(img, bordShadSize, bordShadSize,  getWidth() - 2*bordShadSize, getHeight() - 2*bordShadSize, null);
        }
    }
}
