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
    private final Dimension INITIAL_DIMENSION = new Dimension(80, 80);
    private final Dimension ZOOM_DIMENSION = new Dimension(200, 120);
    private final MigLayout mig;

    public PlayerBoard(GameView g) {
        this.g = g;
        mig = new MigLayout("al center, filly", "10[]10");
        this.setLayout(mig);
        this.setOpaque(false);

        for (Player p : g.getController().getCurrentPlayers()) {
            PlayerItem item = new PlayerItem(p);
            if (p.equals(g.getPlayer()))
                item.setBorder(new ColorableFlatBorder(new Color(104, 203, 44)));
            this.add(item, "w " + INITIAL_DIMENSION.width + ", h " + INITIAL_DIMENSION.height);
        }

        this.g.add(this, BorderLayout.NORTH);
    }

    private class PlayerItem extends GrowingJLabel {
        final Player player;
        int arc;

        public PlayerItem(Player p) {
            super(PlayerBoard.this.mig, INITIAL_DIMENSION, ZOOM_DIMENSION);
            this.player = p;
            this.arc = 10;
            this.setBorder(new ColorableFlatBorder(Color.GREEN, this.arc));   // Couleur quel joueur est en traint de jouer
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.DARK_GRAY);  // TODO : peut-être mettre une image de fond
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), this.arc, this.arc);

            if (this.getSize().equals(this.initialDimension)) {
                // TODO : afficher pp joueur
                this.setText(""+player.getPseudo());
            } else if (this.getSize().equals(this.zoomingDimension)) {
                // TODO : afficher info joueur
            } else {
                // TODO : growing barre
            }

            if (PlayerBoard.this.g.getPlayer().equals(this.player))
                paintBorder(g2);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
