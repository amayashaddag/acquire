package view.game;

import javaswingdev.pggb.PanelGlowingGradient;
import model.Corporation;
import net.miginfocom.layout.Grid;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class ChoiceCorpPane extends JPanel {
    public ChoiceCorpPane(List<Corporation> corps) {
        setLayout(new GridLayout(1, corps.size()));

        for (Corporation corp : corps)
            add(new GlowingItem(corp));

        revalidate();
        repaint();
    }

    private void close() {
        Container parent = getParent();
        parent.remove(this);
        parent.revalidate();
        parent.repaint();
    }

    public Corporation ask(JPanel jp) {
        jp.add(this);
        // retourner le choix
    }

    private class GlowingItem extends PanelGlowingGradient {
        private final Corporation corp;
        public GlowingItem(Corporation corp) {
            super();
            this.corp = corp;

            setSize(100, 100);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
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
