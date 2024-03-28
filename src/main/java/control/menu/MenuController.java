package control.menu;

import model.tools.PlayerAnalytics;
import view.frame.GameFrame;
import view.menu.PrettyMenuView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController {
    PlayerAnalytics playerSession;
    PrettyMenuView view;

    public void start() {
        view = new PrettyMenuView(this);
        view.show();
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
