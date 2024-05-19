package view.game;

import model.tools.AutoSetter;
import view.window.GameFrame;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * A special MouseListener directly thinked for GameView
 * 
 * <p>It implement the zoom and drag through an Affine Transform
 * wich is use on {@link view.game.GameView#paintComponent}</p>
 * 
 * @author Arthur Deck
 * @version 1
 * @see java.awt.event.MouseListener 
 * @see java.awt.event.MouseMotionListener
 * @see java.awt.event.MouseWheelListener
 */
@AutoSetter(typeParam = GameView.class)
public class MouseManager implements MouseListener, MouseWheelListener, MouseMotionListener {
    private final double MINIMAL_ZOOM = 0.75;
    private boolean enabled = true;

    public MouseManager(GameView g) {
        this.lastClickedPos = new Point2D.Float();
        this.at = g.at;
        this.g = g;
        g.addMouseListener(this);
        g.addMouseMotionListener(this);
        g.addMouseWheelListener(this);
    }

    final private GameView g;
    private AffineTransform at;
    private Point2D lastClickedPos;

    public AffineTransform getAffineTransform() {
        return at;
    }

    public void mousePressed(MouseEvent e) {
        if (!enabled) return;

        if (e.getButton() == MouseEvent.BUTTON1) {
            g.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            Point2D p = new Point2D.Float(e.getX(), e.getY());
            try {
                AffineTransform inverseAt = at.createInverse();
                inverseAt.transform(p, lastClickedPos);
            } catch (NoninvertibleTransformException excp) {
                GameFrame.showError(excp, () -> {
                });
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (!enabled) return;

        lastClickedPos = new Point2D.Float();
        if (e.getButton() == MouseEvent.BUTTON1)
            g.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0 && enabled) {
            try {
                Point2D p = new Point2D.Double(e.getX(), e.getY());
                Point2D q = new Point2D.Double();

                AffineTransform inverseAt = at.createInverse();
                inverseAt.transform(p, q);

                at.translate(q.getX() - lastClickedPos.getX(), q.getY() - lastClickedPos.getY());
            } catch (NoninvertibleTransformException excp) {
                GameFrame.showError(excp, () -> {
                });
            }

            g.repaint();
        }
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!enabled) return;

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
            GameFrame.showError(excp, () -> {
            });
        }

        if (at.getScaleX() < MINIMAL_ZOOM || at.getScaleY() < MINIMAL_ZOOM) {
            at.setToScale(MINIMAL_ZOOM, MINIMAL_ZOOM);
            at.translate(200, 150); // FIXME : trouver formule avec MINIMAL_ZOOM
        }

        g.repaint();
    }

    public void setEnabled(boolean arg) {
        this.enabled = arg;
    }
}
