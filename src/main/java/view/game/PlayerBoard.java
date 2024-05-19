package view.game;

import model.game.Corporation;
import model.game.Player;

import java.awt.*;
import java.util.HashMap;

import net.miginfocom.swing.MigLayout;
import model.tools.AutoSetter;
import view.Components.ColorableArcableFlatBorder;
import view.Components.GrowingJLabel;
import view.assets.GameResources;

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
    private final Dimension INITIAL_DIMENSION = new Dimension(100, 80);
    private final Dimension ZOOM_DIMENSION = new Dimension(180, 100);
    private final MigLayout mig;

    public PlayerBoard(GameView g) {
        this.g = g;
        mig = new MigLayout("al center, filly", "10[]10");
        this.setLayout(mig);
        this.setOpaque(false);

        for (Player p : g.getController().getCurrentPlayers()) {
            PlayerItem item = new PlayerItem(p);
            if (p.equals(g.getPlayer())) {
                item.setZoomingDimension(new Dimension(200,120));
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
        int maxCharPsd;
        final ColorableArcableFlatBorder playingBorder;    // The player who is actually his turn
        final ColorableArcableFlatBorder currentPlayerBorder;   // The player who is behind his computer

        public PlayerItem(Player p, Dimension initial, Dimension zoom) {
            super(PlayerBoard.this.mig, initial, zoom);
            this.player = p;
            this.arc = 10;
            this.maxCharPsd = (5*(int)initial.getWidth())/100;
            this.playingBorder = new ColorableArcableFlatBorder(GameResources.getColor("red").darker(), this.arc);
            this.currentPlayerBorder = new ColorableArcableFlatBorder(GameResources.getColor("green").brighter(), this.arc);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
        }

        public PlayerItem(Player p) {
            this(p, INITIAL_DIMENSION, ZOOM_DIMENSION);
        }

        @Deprecated
        @SuppressWarnings("unused")
        private String getPlayersActionsHTMLString(Player p) {
            String res = "";
            HashMap<Corporation, Integer> earnedStocks = p.getEarnedStocks();

            for (HashMap.Entry<Corporation, Integer> entry : earnedStocks.entrySet()) {
                if (entry.getValue() > 0) {
                    int rgb = GameResources.getCorpColor(entry.getKey()).getRGB();
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
                String psd = player.getPseudo();
                if(psd.length() > maxCharPsd)
                    psd = psd.substring(0, maxCharPsd) +"...";

                this.setText(psd);
            } else if (this.getSize().equals(this.zoomingDimension)) {
                String psd = player.getPseudo();
                int maxCharPsdZoom = maxCharPsd + 5;
                if(psd.length() > maxCharPsdZoom)
                    psd = psd.substring(0, maxCharPsdZoom) +"...";

                if (!PlayerBoard.this.g.getPlayer().equals(this.player))
                    this.setText("""
                                        <html>
                                        <table>
                                        <tr>
                                            <td><b>Name</td>
                                            <td>"""+psd+"""
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
                else
                    this.setText("""
                        <html>
                        <table>
                        <tr>
                            <td><b>Name</td>
                            <td>"""+psd+"""
                        </td>
                        </tr>
                        </tr>
                        <tr>
                            <td><b>Net</td>
                            <td>"""+player.getNet()+"""
                        $</td>
                        </tr>
                        <tr>
                            <td><b>Cash</td>
                            <td>"""+player.getCash()+"""
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
