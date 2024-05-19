package app.launcher;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.firebaseinit.FirebaseClient;
import control.menu.MenuController;
import view.window.Form;
import view.window.GameFrame;

public class Debug extends JFrame {

    public static void main(String[] args) {

        FirebaseClient.initialize();

        FlatDarculaLaf.setup();
        GameFrame frame = GameFrame.currentFrame;
        GameFrame.setForm(new Form() {
            @Override
            public void setOn(GameFrame g) {
                g.setContentPane(this);
                g.revalidate();
                g.repaint();
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(view.assets.MenuResources.MImage.LOADING_BACKGROUND,
                    0, 0, GameFrame.currentFrame.getWidth(),
                    GameFrame.currentFrame.getHeight(), null);
            }
        });
        SwingUtilities.invokeLater(() -> frame.setVisible(true));

        try {
            MenuController menuController = new MenuController();
            menuController.start();
        } catch (Exception e) {
            GameFrame.showError(e, frame::dispose);
        }
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
