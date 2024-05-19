
package app.launcher;

import java.awt.Graphics;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.firebaseinit.FirebaseClient;
import control.menu.MenuController;
import view.window.Form;
import view.window.GameFrame;

public class App {
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
}
