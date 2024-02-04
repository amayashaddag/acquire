package view;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;


/**
 * The panel which has the map
 */
public class MapView extends JPanel implements MouseWheelListener {
    
    final int SIZE = 12;
    final Image background;

    /**
     * 
     * @param map : the current's game map
     */
    protected MapView(Object[][] map, int width, int height) {
        super();

        ImageIcon ico = new ImageIcon("src/main/ressources/background.jpg");
        background = ico.getImage();

        this.width = width;
        this.height = height;
    }

    int width;
    int height;
    float zoom = 1;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(background, 0, 0, width, height, this);

        // AffineTransform at = new AffineTransform(1, 0.5, 1, -0.5, 1, 1); // FIXME : changement de repère
        // try {g2d.setTransform(at.createInverse());} catch(Exception e) {}

        AffineTransform at = new AffineTransform();
        at.translate(width / 10., height / 10.);
        at.scale(0.8 * zoom, 0.8 * zoom);
        g2d.setTransform(at);

        // int cellHeight = height / SIZE;  Pour le cadrillage de la map mais on attend la controleur
        // int cellWidth = width / SIZE;

        // g2d.setColor(Color.BLACK);

        // for (int row = 0; row < SIZE; row++) {
        //     int y = row * cellHeight;
        //     for (int col = 0; col < SIZE; col++) {
        //         int x = col * cellWidth;
        //         g2d.drawRect(x, y, cellWidth, cellHeight);
        //     }
        // }

        g2d.dispose();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom = Math.min(1/2, zoom + e.getWheelRotation());
        repaint();
    }
}
