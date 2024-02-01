package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.GameController;

public class GameView extends JFrame {

    public GameView(GameController controller) {
        this.controller = controller;
        this.map = new MapView();

        setTitle("Acquire");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(map);
    }

    GameController controller;
    MapView map;

    final int SIZE = 12;

    class MapView extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(45));
            at.scale(1,0.5);
            g2d.setTransform(at);

            System.out.println("Rotationnnnnnnnnnnnnn");

            int cellWidth = getWidth() / SIZE;
            int cellHeight = getHeight() / SIZE;

            for (int row = 0; row < SIZE; row++) {
                int y = row * cellHeight;
                for (int col = 0; col < SIZE; col++) {
                    int x = col * cellWidth;
                    g.drawRect(x, y, cellWidth, cellHeight);
                }
            }

            g2d.dispose();
        }
    }
}
