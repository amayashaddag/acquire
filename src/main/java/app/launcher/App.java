
package app.launcher;

import com.formdev.flatlaf.FlatDarculaLaf;
import control.firebaseinit.FirebaseClient;
import view.frame.GameFrame;
import view.login.LoginView;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        GameFrame frame = new GameFrame();
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
        LoginView loginView = new LoginView();
        frame.setPanel(loginView);
        try {
            FirebaseClient.initialize();
        } catch (Exception e) {
            GameFrame.showError(e, frame::dispose);
        }
    }
}
