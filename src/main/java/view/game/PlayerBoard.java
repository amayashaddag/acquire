package view.game;

import model.Corporation;
import model.Player;

import java.awt.*;
import java.util.HashMap;

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
    private final Dimension ZOOM_DIMENSION = new Dimension(140, 100);
    private final MigLayout mig;

    public PlayerBoard(GameView g) {
        this.g = g;
        mig = new MigLayout("al center, filly", "10[]10");
        this.setLayout(mig);
        this.setOpaque(false);

        for (Player p : g.getController().getCurrentPlayers()) {
            PlayerItem item = new PlayerItem(p);
            if (p.equals(g.getController().getCurrentPlayer())) {
                item = new PlayerItem(p, INITIAL_DIMENSION, new Dimension(200, 120));
                item.setBorder(new ColorableArcableFlatBorder(Color.GREEN));
            }
            else if (p.equals(g.getPlayer()))
                item.setBorder(new ColorableArcableFlatBorder(Color.BLUE));
            this.add(item, "w " + INITIAL_DIMENSION.width + ", h " + INITIAL_DIMENSION.height);
        }

        this.g.add(this, BorderLayout.NORTH);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Component c : getComponents())
            c.setEnabled(enabled);
    }

    private class PlayerItem extends GrowingJLabel {
        final Player player;
        int arc;
        final ColorableArcableFlatBorder playingBorder;    // The player who is actually his turn
        final ColorableArcableFlatBorder currentPlayerBorder;   // The player who is behind his computer

        public PlayerItem(Player p, Dimension initial, Dimension zoom) {
            super(PlayerBoard.this.mig, initial, zoom);
            this.player = p;
            this.arc = 10;
            this.playingBorder = new ColorableArcableFlatBorder(Color.RED, this.arc);
            this.currentPlayerBorder = new ColorableArcableFlatBorder(Color.GREEN, this.arc);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
        }

        public PlayerItem(Player p) {
            this(p, INITIAL_DIMENSION, ZOOM_DIMENSION);
        }

        private String getPlayersActionsHTMLString(Player p) {
            String res = "";
            HashMap<Corporation, Integer> earnedStocks = p.getEarnedStocks();

            for (HashMap.Entry<Corporation, Integer> entry : earnedStocks.entrySet()) {
                if (entry.getValue() > 0) {
                    int rgb = Ressources.Assets.getCorpColor(entry.getKey()).getRGB();
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;
                    res += "<td style='color: rgb(" + red + " " + green + " " + blue + "); display: block; margin: 0 auto;'> " +
                            + entry.getValue() + "</td>";
                }
            }
            return res;
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
                if (player.equals(PlayerBoard.this.g.getPlayer()))
                    this.setText("""
                                    <html>
                                      <table>
                                      <tr>
                                        <td><b>Name</td>
                                        <td>"""+player.getPseudo()+"""
                                      </td>
                                      </tr>
                                      <tr>
                                        <td><b>Cash</td>
                                        <td>"""+player.getCash()+"""
                                      $</td>
                                      </tr>
                                      <tr>
                                         <td><b>Net</td>
                                         <td>"""+player.getNet()+"""
                                      $</td>
                                      </tr>
                                      <tr>
                                        <center>
                                          """+getPlayersActionsHTMLString(player)+"""
                                        </center>
                                      </tr>
                                    </table>
                                    </html>
                                    """);
                else
                    this.setText("""
                                    <html>
                                      <table>
                                      <tr>
                                        <td><b>Name</td>
                                        <td>"""+player.getPseudo()+"""
                                      </td>
                                      </tr>
                                      </tr>
                                      <tr>
                                         <td><b>Net</td>
                                         <td>"""+player.getNet()+"""
                                      $</td>
                                      </tr>
                                    </table>
                                    </html>
                                    """);
            } else {
                this.setText("loading...");
            }

            if (PlayerBoard.this.g.getPlayer().equals(this.player)) {
                this.setBorder(currentPlayerBorder);
                paintBorder(g2);
            } else if (PlayerBoard.this.g.getController().getCurrentPlayer().equals(player)) {
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
