package app.launcher;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import control.firebaseinit.FirebaseClient;
import control.database.DatabaseConnection;
import control.game.GameController;
import model.game.Player;
import view.frame.GameFrame;
import view.game.GameView;

public class NetworkDebug {
    public static void main(String[] args) {        
        try {

            FirebaseClient.initialize();

            GameFrame f = new GameFrame();

            int maxPlayers = 6;
            Player hoster = Player.createHumanPlayer("Amayas", "AMA283729382");
            Player joiner = Player.createHumanPlayer("Arthur", "ART283729382");
            List<Player> allPlayers = new LinkedList<>();
            Collections.addAll(allPlayers, hoster, joiner);

            String gameId = DatabaseConnection.createGame(maxPlayers);
            DatabaseConnection.setCurrentPlayer(gameId, hoster.getUID());
            System.out.println(gameId);

            for (Player p : allPlayers) {
                DatabaseConnection.addPlayer(gameId, p);
            }

            GameController controller = new GameController(allPlayers, hoster, gameId, true);
            GameView view = controller.getGameView();

            f.setContentPane(view);
            f.revalidate();
            
            SwingUtilities.invokeLater(() -> {
                f.setVisible(true);
            });
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
