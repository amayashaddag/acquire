package view;

import net.miginfocom.swing.MigLayout;
import tools.Point;
import view.game.ColorableFlatBorder;
import view.game.GameView;
import view.game.GrowingJLabel;
import view.game.PlayerBoard;
import view.game.annotations.AutoSetter;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.ui.*;

import java.awt.event.*;

import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import com.formdev.flatlaf.util.DerivedColor;
import com.formdev.flatlaf.util.UIScale;

import model.Player;

import java.util.Map;

import javax.swing.plaf.basic.BasicBorders;

@AutoSetter(paramType = GameView.class)
public class Debug {

    public static void main(String[] args) {
        // FlatDarculaLaf.setup();

        JFrame g = new JFrame();
        g.setTitle("Acquire");
        g.setSize(1000, 600);
        g.setLocationRelativeTo(null);
        g.setDefaultCloseOperation(3);

        JPanel jp = new JPanel();
        MigLayout mig = new MigLayout("al center, filly", "10[]10");
        jp.setLayout(mig);

        Player p = new Player();
        jp.add(new Debug.Caca(p, mig), "w 100, h 100");

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(jp, BorderLayout.NORTH);

        g.add(root);
        g.setVisible(true);

        // GameFrame f = new GameFrame();
        // f.setVisible(true);
    }

    public static class Caca extends GrowingJLabel {
        final Player player;

        public Caca(Player p, MigLayout mig) {
            super(mig, new Dimension(100, 100), new Dimension(300, 120));
            this.player = p;
            this.setText("Arthur");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            if (this.getSize().equals(this.initialDimension)) {
                // TODO : on affiche la pp du joueur
            } else if (this.getSize().equals(this.zoomingDimension)) {
                // TODO : afficher info joueur
            } else {
                // TODO : growing barre
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
