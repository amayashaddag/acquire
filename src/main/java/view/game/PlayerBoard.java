package view.game;

import model.Player;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.miginfocom.swing.MigLayout;

public class PlayerBoard extends javax.swing.JPanel {
    private final GameView g;
    private final Dimension INITIAL_DIMENSION = new Dimension(100, 100);
    private final Dimension ZOOM_DIMENSION = new Dimension(300, 120);

    MigLayout mig;

    public PlayerBoard(GameView g) {
        mig = new MigLayout("al center, filly", "10[]10");
        this.g = g;

        // TODO : attendre le controleur
        for (Player p : new java.lang.Iterable<Player>() {
            public java.util.Iterator<Player> iterator() {
                return null;
            }
        }) {
            PlayerItem item = new PlayerItem(p);
            if (p.equals(g.getPlayer()))
                item.setBorder(new ColorableFlatBorder(new Color(104, 203, 44)));
            this.add(item, "w " + INITIAL_DIMENSION.width + ", h " + INITIAL_DIMENSION.height);
        }

        this.g.add(this, BorderLayout.NORTH);
    }

    private class PlayerItem extends GrowingJLabel {
        final Player player;

        public PlayerItem(Player p) {
            super(PlayerBoard.this.mig, INITIAL_DIMENSION, ZOOM_DIMENSION);
            this.player = p;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
