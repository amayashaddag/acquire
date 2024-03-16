package app.launcher;

import com.formdev.flatlaf.FlatDarculaLaf;
import control.firebaseinit.FirebaseClient;
import control.game.GameController;
import model.game.Player;
import view.frame.GameFrame;
import view.game.GameView;
import view.login.LoginView;
import view.menu.MenuView;

import javax.swing.SwingUtilities;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        GameFrame frame = new GameFrame();
        LoginView loginView = new LoginView();
        frame.setContentPane(loginView);
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
        try {
            FirebaseClient.initialize();
        } catch (Exception e) {
            GameFrame.showError(e, frame::dispose);
            System.out.println("SHOWING ERRORS");
        }
    }

    public static void launchOfflineGame(GameFrame parent) {
        Player offlinePlayer = Player.createHumanPlayer("Player");
        List<Player> players = new LinkedList<>();
        players.add(offlinePlayer);

        GameController controller = new GameController(players, offlinePlayer);
        GameView view = new GameView(controller, offlinePlayer);
        parent.setPanel(view);
    }
}
