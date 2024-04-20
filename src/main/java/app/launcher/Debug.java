package app.launcher;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.firebaseinit.FirebaseClient;
import control.menu.MenuController;
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
            GameFrame.showError(e, () -> {
                frame.dispose();
            });
        }

    }
}