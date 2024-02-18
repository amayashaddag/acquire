package view.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

import javax.swing.Timer;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;


/**
 * <p> A JLabel wich automatly grow
 * when you pass your mouse on it</p>
 * <p> It grow for width not for height </p>
 * 
 * @author Arthur Deck
 * @version 1
 */
public class GrowingJLabel extends JLabel {

    private Timer timer;
    private boolean show;

    private final Dimension initialDimension, zoomingDimension;

    public GrowingJLabel(MigLayout mig) {
        this(mig, new Dimension(100, 100), new Dimension(200, 120));
    }

    public GrowingJLabel(MigLayout mig, Dimension initial, Dimension zooming) {
        super();
        this.initialDimension = initial;
        this.zoomingDimension = zooming;

        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (show) {
                    int width = getWidth();
                    int height = getHeight();
                    if (width < zoomingDimension.width) {
                        mig.setComponentConstraints(GrowingJLabel.this, "w " + (width + 1) + ", h " + Math.min(height + 1, zoomingDimension.height));
                        getParent().revalidate();
                    } else {
                        timer.stop();
                    }
                } else {
                    int width = getWidth();
                    int height = getHeight();
                    if (width > initialDimension.width) {
                        mig.setComponentConstraints(GrowingJLabel.this, "w " + (width - 1) + ", h " + Math.max(height - 1, initialDimension.height));
                        getParent().revalidate();
                    } else {
                        timer.stop();
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                show = true;
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                show = false;
                timer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.RED);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
    }
}


