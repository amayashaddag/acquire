package control.menu;

import control.game.GameController;
import model.game.Player;
import model.tools.PlayerAnalytics;
import view.frame.GameFrame;
import view.menu.PrettyMenuView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController {
    private final String FILE_OUTPUT = "A remplir";
    PlayerAnalytics session;
    PrettyMenuView view;

    public MenuController() {
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
        // FIXME : lancer avec la session du joueur et les IA
        ArrayList<Player> l = new ArrayList<>();
        l.add(Player.createHumanPlayer("Max", null));
        l.add(Player.createHumanPlayer("Xi", null));
        l.add(Player.createHumanPlayer("Best", null));
        l.add(Player.createHumanPlayer("Of", null));
        GameController c = new GameController(l, l.get(0), null, true);
        GameFrame.currentFrame.setForm(c.getGameView());
    }

    public PlayerAnalytics getPlayerAnalytics(String uid) {
        // TODO : Should implement this one
        return null;
    }

    public List<PlayerAnalytics> getRanking() {
        // TODO : Implement
        return null;
    }

    public Map<String, Integer> getAvailableGames() {
        // TODO : Implement
        return null;
    }

    public void saveSession() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_OUTPUT);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(session);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.err.println("Error during the save of the Players Session");
            GameFrame.showError(e, ()->{});
        }
    }

    public void loadSession() {
        try {
            FileInputStream fis = new FileInputStream(FILE_OUTPUT);
            ObjectInputStream ois = new ObjectInputStream(fis);
            session = (PlayerAnalytics) ois.readObject();
            System.out.println(session.pseudo());
            ois.close();
            fis.close();
        } catch (Exception e) {
            System.err.println("Error during the load of the Players Session");
            GameFrame.showError(e, ()->{});
        }
    }

    public void setSession(String UID) {
        this.session = getPlayerAnalytics(UID);
    }

    public void setSession(PlayerAnalytics session) {
        this.session = session;
    }
}
