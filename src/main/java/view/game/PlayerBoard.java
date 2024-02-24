package view.game;

import model.Player;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.miginfocom.swing.MigLayout;
import tools.AutoSetter;

import javax.swing.*;

/**
 * <p> A JPanel which contains all
 * the {@link view.game.PlayerBoard.PlayerItem}
 * for displaying the players's infos</p>
 *
 * @author Arthur Deck
 * @version 1
 */
@AutoSetter(typeParam = GameView.class)
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
                item.setBorder(new ColorableArcableFlatBorder(Color.GREEN));
            this.add(item, "w " + INITIAL_DIMENSION.width + ", h " + INITIAL_DIMENSION.height);
        }

        this.g.add(this, BorderLayout.NORTH);
    }

    private class PlayerItem extends GrowingJLabel {
        final Player player;
        int arc;
        ColorableArcableFlatBorder playingBorder;

        public PlayerItem(Player p) {
            super(PlayerBoard.this.mig, INITIAL_DIMENSION, ZOOM_DIMENSION);
            this.player = p;
            this.arc = 10;
            this.playingBorder = new ColorableArcableFlatBorder(Color.GREEN, this.arc);   // Couleur indiquant quel joueur est en traint de jouer
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.DARK_GRAY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), this.arc, this.arc);

            if (this.getSize().equals(this.initialDimension)) {
                // TODO : afficher pp joueur
                this.setText(player.getPseudo());
            } else if (this.getSize().equals(this.zoomingDimension)) {
                this.setText("""
                                <html>
                                  <table>
                                  <tr>
                                    <td>1,1</td>
                                    <td>1,2</td>
                                    <td>1,3</td>
                                  </tr>
                                  <tr>
                                    <td>2,1</td>
                                    <td>2,2</td>
                                    <td>2,3</td>
                                  </tr>
                                  <tr>
                                    <td>3,1</td>
                                    <td>3,2</td>
                                    <td>3,3</td>
                                  </tr>
                                </table>
                                </html>
                                """);
            } else {
                this.setText("loading...");
            }

            if (PlayerBoard.this.g.getPlayer().equals(this.player)) {
                this.setBorder(playingBorder);
                paintBorder(g2);
            } else if (this.getBorder() != null) {
                this.setBorder(null);
            }
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
