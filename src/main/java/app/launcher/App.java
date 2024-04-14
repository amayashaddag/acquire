
package app.launcher;

import com.formdev.flatlaf.FlatDarculaLaf;
import control.firebaseinit.FirebaseClient;
import view.login.LoginView;
import view.window.GameFrame;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        GameFrame frame = new GameFrame();
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
        LoginView loginView = new LoginView();
        frame.setContentPane(loginView);
        try {
            FirebaseClient.initialize();
        } catch (Exception e) {
            GameFrame.showError(e, frame::dispose);
        }
    }
}
