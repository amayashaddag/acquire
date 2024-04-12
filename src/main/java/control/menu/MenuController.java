package control.menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import control.auth.AuthController;
import control.database.GameDatabaseConnection;
import control.game.GameController;
import model.game.Player;
import model.tools.PlayerAnalytics;
import model.tools.PlayerCredentials;
import model.tools.PreGameAnalytics;
import view.frame.GameFrame;
import view.game.GameView;
import view.menu.PrettyMenuView;

/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController {
    private final String FILE_OUTPUT = "src/main/ressources/session/game-session.ser";

    private PlayerAnalytics session;
    private PrettyMenuView view;

    public MenuController() {
        loadSession();
    }

    public void start() {
        view = new PrettyMenuView(this);
        view.show();
        view.repaint();
        view.revalidate();
    }

    public PlayerAnalytics getPlayerAnalyticsSession() {
        return session;
    }

    public boolean isConnected() {
        return session != null;
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
        try {
            return GameDatabaseConnection.getPlayerAnalytics(uid);
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
            return null;
        }
    }

    public PlayerCredentials getPlayerCredentials(String uid) throws Exception {
        try {
            return AuthController.getPlayerCredentials(uid);
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
            return null;
        }
    }

    public List<PlayerAnalytics> getRanking() {
        try {
            return GameDatabaseConnection.getRanking();
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
            return null;
        }
    }

    public List<PreGameAnalytics> getAvailableGames() {
        try {
            return GameDatabaseConnection.getAvailableGames();
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
            return null;
        }
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
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
        }
    }

    public void loadSession() {
        try {
            FileInputStream fis = new FileInputStream(FILE_OUTPUT);
            
            if (fis.available() > 0) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                session = (PlayerAnalytics) ois.readObject();
                System.out.println(session.pseudo());
                ois.close();
            }

            fis.close();
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
        }

    }

    public void setSession(String UID) throws Exception {
        this.session = getPlayerAnalytics(UID);
    }

    public void setSession(PlayerAnalytics session) {
        this.session = session;
    }
}
