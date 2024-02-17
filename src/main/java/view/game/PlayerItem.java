package view.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class PlayerItem extends JLabel {

    private Timer timer;
    private boolean show;

    public PlayerItem(MigLayout mig) {
        super();

        setText("cdzafdza");
        
        timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (show) {
                    int width = getWidth();
                    int height = getHeight();
                    if (height < 220) {
                        mig.setComponentConstraints(PlayerItem.this, "w " + (width + 1) + ", h " + (height + 1));
                        getParent().revalidate();
                    } else {
                        timer.stop();
                    }
                } else {
                    int width = getWidth();
                    int height = getHeight();
                    if (height > 100) {
                        mig.setComponentConstraints(PlayerItem.this, "w " + (width - 1) + ", h " + (height - 1));
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
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}


