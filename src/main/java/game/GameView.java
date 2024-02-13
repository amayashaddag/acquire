package game;

import control.GameController;
import frame.Form;
import frame.GameFrame;
import model.Player;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
 * The panel which has the map
 * 
 * @author Arthur Deck
 * @version 0.1
 * @see frame.Form
 */
public class GameView extends Form {

    final int SIZE = 12;
    final GameController controller;
    final Player player;
    AffineTransform at;

    public GameView(GameController controller, Player player) {
        super();
        this.at = new AffineTransform();
        this.controller = controller;
        this.player = player;

        this.addMouseListener(new GameMouseListener());
        this.addMouseMotionListener(new GameMouseMotionListener());
        this.addMouseWheelListener(new GameMouseWheelListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setTransform(at);
        g2d.drawImage(GameRessources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);

        int cellWidth = getWidth() / SIZE;
        int cellHeight = getHeight() / SIZE;

        int x = 0;
        int y = 0;
        for (int row = 0; row < SIZE; row++) {
            x = -row * cellWidth / 2 + getWidth() / 2 - cellWidth;
            y = row * cellHeight / 2; // FIXME 20 for menu bar
            for (int col = 0; col < SIZE; col++) {
                x += cellWidth / 2;
                y += cellHeight / 2;
                g2d.drawImage(GameRessources.Assets.GRASS, x, y, cellWidth, cellHeight, this);
            }
        }

        g2d.dispose();
    }

    /*------------------ Form ------------------- */

    /**
     * {@link frame.Form#setOn()}
     */
    public void setOn(GameFrame g) {
        this.setSize(g.getWidth(), g.getHeight()); // FIXME map != null
        g.getContentPane().add(this); // TODO : en attente du controleur pour [][]
    }


    /*------------------ Listener ------------------- */

    private Point2D lastClickedPos = new Point2D.Float();

    private class GameMouseListener implements MouseListener {
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
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
            lastClickedPos = new Point2D.Float();
            if (e.getButton() == MouseEvent.BUTTON1)
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

    }

    private class GameMouseMotionListener implements MouseMotionListener {
        public void mouseMoved(MouseEvent e) {
        }

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
                    GameFrame.showError(excp, () -> {
                    });
                }

                repaint();
            }
        }

        private void wrapTranslation() {
            double matrix[] = new double[6];
            at.getMatrix(matrix);

            if (at.getTranslateX() > 0)
                matrix[4] = 0;
            if (at.getTranslateY() > 0)
                matrix[5] = 0;
            // FIXME : dépacement droite bas

            at = new AffineTransform(matrix);
        }
    }

    private class GameMouseWheelListener implements MouseWheelListener {
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
                GameFrame.showError(excp, () -> {
                });
            }

            if (at.getScaleX() < 1 || at.getScaleY() < 1) {
                at.setToScale(1, 1);
            }

            repaint();
        }
    }
}
