package app.launcher;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.firebaseinit.FirebaseClient;
import control.menu.MenuController;
import net.miginfocom.swing.MigLayout;
import view.window.GameFrame;

public class Debug extends JFrame {

    public static void main(String[] args) throws Exception {

        FirebaseClient.initialize();

        FlatDarculaLaf.setup();
        GameFrame frame = GameFrame.currentFrame;
        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        try {
            MenuController menuController = new MenuController();
            menuController.start();
        } catch (Exception e) {
            GameFrame.showError(e, frame::dispose);
        }

        // MigLayout mig = new MigLayout();
        // frame.setLayout(mig);
        // JButton btn = new JButton("Clique ici");
        // frame.add(btn, "w 20, h 20");
            
        // btn.addActionListener((e) ->f(frame, btn, 200, 200));
    }

    public static void f(JFrame frame, Component com, int x, int y) {
        int deltaX = x - com.getX();
        int deltaY = y - com.getY();
        Animator animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                int newX = (int) (deltaX * fraction);
                int newY = (int) (deltaY * fraction);
                com.setLocation(newX, newY);
                com.getParent().repaint();
            }
        });
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.2f);
        animator.start();
    }
}
