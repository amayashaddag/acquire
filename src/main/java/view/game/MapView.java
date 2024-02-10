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

        ImageIcon ico = new ImageIcon("src/main/ressources/background.jpg");
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

        GradientDropdownMenu menu = getGameMenu();
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
    public void mouseMoved(MouseEvent e) {
        
        //System.out.println("dim : " + getWidth() * at.getScaleX()); // FIXME : debug !
    }
    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
            try {
                Point2D p = new Point2D.Double(e.getX(), e.getY());
                Point2D q = new Point2D.Double();
                
                AffineTransform inverseAt = at.createInverse();
                inverseAt.transform(p, q);

                at.translate(q.getX() - lastClickedPos.getX(), q.getY() - lastClickedPos.getY()); 
                //System.out.println(at.getTranslateX() + " " + at.getTranslateY() ); // FIXME : debug !
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
        // if (at.getTranslateX() < getWidth() * at.getScaleX()) matrix[4] = getWidth()+ getWidth()/at.getScaleX();
        // if (at.getTranslateY() < getHeight() * at.getScaleY()) matrix[5] = 0;

        System.out.println(matrix[4] + "  " + matrix[5]);
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

        if (at.getScaleX() < 1 || at.getScaleY() < 1) {
            at.setToScale(1, 1);
        }
    
        repaint();
    }
}
