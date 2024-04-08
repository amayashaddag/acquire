package control.menu;

import control.game.GameController;
import model.tools.PreGameAnalytics;
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

    public List<PreGameAnalytics> getAvailableGames() {
        // TODO : Implement
        return null;
    }

    public void joinPreGame(String preGameUID) {
        // TODO : faire rejoindre au player actuel la partie demandé
    }

    public void createMultiGame() {
        // TODO : creer une nouvelle game multi permettant aux autres joueurs de la
        // rejoindre (game creer par le joueur don c'est la session)
    }

    public void launchMultiGame() {
        // TODO : lorsque le joueur a créer une game multi et qu'il décide qu'il est
        // temps de
        // lancer la parti, cette méthode est appelé et permet de lancer la partie
    }

    public void avortMutiGame() {
        // TODO : le joueur à creer une parti et pour une raison ou une autre décide
        // d'abandonner
        // et de retourner au menu. Cette méthode doit donc supprimer la partie créer
        // par le joueur
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
            GameFrame.showError(e, () -> {
            });
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
            session = null;
            System.err.println("Error during the load of the Players Session");
            GameFrame.showError(e, () -> {
            });
        }
    }

    public void setSession(String UID) {
        this.session = getPlayerAnalytics(UID);
    }

    public void setSession(PlayerAnalytics session) {
        this.session = session;
    }
}
