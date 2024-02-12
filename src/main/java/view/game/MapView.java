package view.game;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.Image;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Color;

import javaswingdev.GradientDropdownMenu;
import javaswingdev.MenuEvent;
import view.Form;
import view.GameFrame;


/**
 * The panel which has the map
 * 
 * @author Arthur Deck
 * @version 0.1
 * @see view.Form
 */
public class MapView extends Form implements MouseWheelListener, MouseListener, MouseMotionListener {
    
    final int SIZE = 12;
    final Image background;

    public MapView() {
        super(); // FIXME : changer celà lorsqu'on aura le controleur
        background = null;
    }

    /**
     * @param map : the current's game map
     */
    protected MapView(Object[][] map, int width, int height) {
        super();

        ImageIcon ico = new ImageIcon("src/main/"+ GameString.RESSOURCES_PATH + GameString.IMAGES_PATH + "background.jpg");
        background = ico.getImage();

        setSize(width, height);
        this.at = new AffineTransform();
    }

    AffineTransform at;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;        
        
        g2d.setTransform(at);
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
  
        int cellWidth = getWidth() / SIZE;
        int cellHeight = getHeight() / SIZE;

        ImageIcon grassIco = new ImageIcon("src/main/"+GameString.RESSOURCES_PATH + GameString.IMAGES_PATH + "grass.png");
        Image grassImg = grassIco.getImage();

        int x = 0;
        int y = 0;
        for (int row = 0; row < SIZE; row++) {
            x = -row * cellWidth/2 + getWidth()/2 - cellWidth; 
            y = row * cellHeight/2; // FIXME 20 for menu bar
            for (int col = 0; col < SIZE; col++) { 
                x += cellWidth/2; 
                y += cellHeight/2;
                g2d.drawImage(grassImg, x, y, cellWidth, cellHeight, this);
            }
        }

        g2d.dispose();
    }


    /*------------------ Form ------------------- */

    private GradientDropdownMenu getGameMenu() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        GradientDropdownMenu menu = new GradientDropdownMenu();
        menu.addItem("Information", "Player Information","Global statement");
        menu.addItem("Action", "act1", "act2", "...");
        menu.addItem("Chat", "...");
        menu.addItem("Option", "Reinitialize map view","Full Screen", "Normal Screen", "Exit");
        menu.addEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex, boolean menuItem) {
                if (menuItem) {
                    if (menu.getMenuNameAt(index, subIndex) == "Exit") {
                        System.exit(0);
                    } else if (menu.getMenuNameAt(index, subIndex) == "Reinitialize map view") {
                        // FIXME : implement in a general method
                    } else if (menu.getMenuNameAt(index, subIndex) == "Full Screen") {
                        // FIXME : undecorated !!!
                    } else if (menu.getMenuNameAt(index, subIndex) == "Normal Screen") {
                        device.setFullScreenWindow(null);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "not availible yet");
                    }
                }
            }
        });

        return menu;
    }

    /**
     * {@link view.Form#setOn()}
     */
    public void setOn(GameFrame g) {
        MapView map = new MapView(null, g.getWidth(), g.getHeight());   // FIXME map != null
        g.addMouseWheelListener(map);
        g.addMouseListener(map);
        g.addMouseMotionListener(map);
        g.getContentPane().add(map); // TODO : en attente du controleur pour [][]

        GradientDropdownMenu menu = getGameMenu(); // FIXME : pour test
        menu.applay(g);
    }

    
    /* -------------- MouseListener --------------- */
    private Point2D lastClickedPos = new Point2D.Float();
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point2D p = new Point2D.Float(e.getX(), e.getY()); 
            try {
                AffineTransform inverseAt = at.createInverse();
                inverseAt.transform(p, lastClickedPos);
            } catch (NoninvertibleTransformException excp) {
                GameFrame.showError(excp, () -> {});
            }
        }
    }
    public void mouseReleased(MouseEvent e) {
        lastClickedPos = new Point2D.Float();
        if (e.getButton() == MouseEvent.BUTTON1) 
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    /* -------------- MouseMotionListener --------------- */
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
            try {
                Point2D p = new Point2D.Double(e.getX(), e.getY());
                Point2D q = new Point2D.Double();
                
                AffineTransform inverseAt = at.createInverse();
                inverseAt.transform(p, q);

                at.translate(q.getX() - lastClickedPos.getX(), q.getY() - lastClickedPos.getY()); 
                wrapTranslation(); 
            } catch (NoninvertibleTransformException excp) {
                GameFrame.showError(excp, () -> {});
            }
            
            repaint();
        }
    }

    private void wrapTranslation() {
        double matrix[] = new double[6];
        at.getMatrix(matrix);

        if (at.getTranslateX() > 0) matrix[4] = 0;
        if (at.getTranslateY() > 50) matrix[5] = 0;    // FIXME : 50 hauteur menu
        // FIXME : dépacement droite bas

        at = new AffineTransform(matrix);
    }

    /* -------------- MouseWheelListener --------------- */
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

        if (at.getScaleX() < 1 || at.getScaleY() < 1) { // FIXME : min dezoom
            at.setToScale(1, 1);
        }
    
        repaint();
    }
}
