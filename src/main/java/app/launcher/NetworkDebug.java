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
            Player p1 = Player.createHumanPlayer("Arthur", "ART20304I85");
            Player p2 = Player.createHumanPlayer("Igor", "IGO203234I85");
            List<Player> allPlayers = new LinkedList<>();
            Collections.addAll(allPlayers, hoster, p1, p2);


            String gameId = DatabaseConnection.createGame(maxPlayers);
            DatabaseConnection.setCurrentPlayer(gameId, hoster.getUID());
            System.out.println(gameId);

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
