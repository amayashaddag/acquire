package view.Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

import javax.swing.Timer;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;


/**
 * <p> A JTable which automatically grow
 * when you pass your mouse on it</p>
 * <p> It use for print player's information </p>
 * 
 * @author Arthur Deck
 * @version 1
 */
public class GrowingJLabel extends JLabel {

    private Timer timer;
    private boolean show;
    protected Dimension initialDimension, zoomingDimension;

    public Dimension getInitialDimension() {
        return initialDimension;
    }

    public void setInitialDimension(Dimension initialDimension) {
        this.initialDimension = initialDimension;
    }

    public Dimension getZoomingDimension() {
        return zoomingDimension;
    }

    public void setZoomingDimension(Dimension zoomingDimension) {
        this.zoomingDimension = zoomingDimension;
    }

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
                int width = getWidth();
                int height = getHeight();
                if (show) {
                    if (width < zoomingDimension.width) {
                        mig.setComponentConstraints(GrowingJLabel.this, "w " + (width + 1) + ", h " + Math.min(height + 1, zoomingDimension.height));
                        getParent().revalidate();
                    } else {
                        timer.stop();
                    }
                } else {
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
                show = isEnabled();
                timer.start();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                show = false;
                timer.start();
            }
        });
    }
}


