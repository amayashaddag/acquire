package control.menu;

import control.game.GameController;
import model.game.Player;
import model.tools.PlayerAnalytics;
import view.frame.GameFrame;
import view.menu.PrettyMenuView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController implements Serializable {
    PlayerAnalytics playerAnalyticsSession;
    PrettyMenuView view;

    public void start() {
        view = new PrettyMenuView(this);
        view.show();
    }

    public PlayerAnalytics getPlayerAnalyticsSession() {
        return playerAnalyticsSession;
    }

    public boolean isConnected() {
        return playerAnalyticsSession != null;
    }

    public void startSingleGame() {
        // FIXME : lancer avec la session du joueur et les IA
        ArrayList<Player> l = new ArrayList<>();
        l.add(Player.createHumanPlayer("Max"));
        l.add(Player.createHumanPlayer("Xi"));
        l.add(Player.createHumanPlayer("Best"));
        l.add(Player.createHumanPlayer("Of"));
        GameController c = new GameController(l, l.get(0));
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
}
