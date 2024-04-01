package control.menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import control.database.GameDatabaseConnection;
import control.game.GameController;
import model.game.Player;
import model.tools.PlayerAnalytics;
import view.game.GameView;
import view.menu.PrettyMenuView;

/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController {
    private final String FILE_OUTPUT = "A remplir";

    private PlayerAnalytics session;
    private PrettyMenuView view;

    public MenuController() throws Exception {
        loadSession();
    }

    public void start() {
        view = new PrettyMenuView(this);
        view.show();
    }

    public PlayerAnalytics getPlayerAnalyticsSession() {
        return session;
    }

    public boolean isConnected() {
        return session == null;
    }

    public void startSingleGame() {
        List<Player> players = new LinkedList<>();
        Player p = Player.createHumanPlayer("Player", null);
        GameController controller = new GameController(players, p, null, false);
        GameView view = controller.getGameView();

        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    public PlayerAnalytics getPlayerAnalytics(String uid) {
        // TODO : Should implement this one
        return null;
    }

    public List<PlayerAnalytics> getRanking() {
        // TODO : Implement
        return null;
    }

    public Map<String, Integer> getAvailableGames() throws Exception {
        return GameDatabaseConnection.getAvailableGames();
    }

    public void saveSession() throws Exception {
        FileOutputStream fos = new FileOutputStream(FILE_OUTPUT);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(session);
        oos.close();
        fos.close();
    }

    public void loadSession() throws Exception {
        FileInputStream fis = new FileInputStream(FILE_OUTPUT);
        ObjectInputStream ois = new ObjectInputStream(fis);
        session = (PlayerAnalytics) ois.readObject();
        System.out.println(session.pseudo());
        ois.close();
        fis.close();
    }

    public void setSession(String UID) {
        this.session = getPlayerAnalytics(UID);
    }

    public void setSession(PlayerAnalytics session) {
        this.session = session;
    }
}
