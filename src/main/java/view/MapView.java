package view;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
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
        this.at = new AffineTransform();
    }

    int width;
    int height;
    AffineTransform at;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;        

        g2d.setTransform(at);
        g2d.drawImage(background, 0, 0, width, height, this);

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
        double scale = e.getWheelRotation() < 0 ? 1.5 : 0.5;
        Point2D p = new Point2D.Float(e.getX(), e.getY());
        Point2D q = new Point2D.Float();
    
        try {
            AffineTransform inverseAt = at.createInverse();
            inverseAt.transform(p, q);
    
            at.translate(q.getX(), q.getY());
            at.scale(scale, scale);
            at.translate(-q.getX(), -q.getY());
        } catch (NoninvertibleTransformException excp) {
            GameFrame.showError(excp, () -> {});
        }

        if (at.getScaleX() < 1 || at.getScaleY() < 1) {
            at.setToIdentity();
        }
    
        repaint();
    }
}
