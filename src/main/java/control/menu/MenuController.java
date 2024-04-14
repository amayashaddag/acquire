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
import view.game.GameView;
import view.menu.PrettyMenuView;
import view.window.GameFrame;

/**
 * The controller for the menu
 *
 * @author Arthur Deck
 * @version 1
 */
public class MenuController {
    private final String FILE_OUTPUT = "src/main/ressources/session/game-session.ser";

    // ONLY FOR TEST, // TODO : Should remove later
    private PlayerCredentials session = new PlayerCredentials("SOME-WEIRD-UID", "amayas@icloud.dz", "my___ass");
    private PrettyMenuView view;

    public static final int DEFAULT_MAX_PLAYERS = 3;

    public MenuController() {
        loadSession();
    }

    public void start() {
        view = new PrettyMenuView(this);
        view.show();
        view.repaint();
        view.revalidate();
    }

    public PlayerCredentials getPlayerCredentials() {
        return session;
    }

    public PlayerCredentials getPlayerCredentials(String userId) {
        try {
            return AuthController.getPlayerCredentials(userId);
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });

            return null;
        }
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

    public PlayerAnalytics getPlayerAnalytics() {
        return getPlayerAnalytics(session.uid());
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
        try {
            if (!GameDatabaseConnection.isGameStarted(preGameUID)
                && !GameDatabaseConnection.isGameFull(preGameUID)) {
                    GameDatabaseConnection.addPlayer(preGameUID, session);
            } else {

            }
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
        }
    }

    public void createMultiGame() {
        try {
            String gameId = GameDatabaseConnection.createGame(session, DEFAULT_MAX_PLAYERS);
            joinPreGame(gameId);
        } catch (Exception e) {
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
                parent.dispose();
            });
        }
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
                session = (PlayerCredentials) ois.readObject();
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
        this.session = getPlayerCredentials(UID);
    }

    public void setSession(PlayerCredentials session) {
        this.session = session;
    }
}
