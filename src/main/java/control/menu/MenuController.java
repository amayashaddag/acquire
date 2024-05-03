package control.menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

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

    private PlayerCredentials session;
    private PrettyMenuView view;

    private Timer onlineObserver;
    private PreGameAnalytics joinedGameAnalytics;

    public static final int DEFAULT_MAX_PLAYERS = 3;
    public static final int ONLINE_OBSERVER_DELAY = 2000;

    public MenuController() {
        loadSession();

        this.onlineObserver = new Timer(ONLINE_OBSERVER_DELAY, (ActionListener) -> {
            try {

                view.repaint();
                view.revalidate();

                if (joinedGameAnalytics == null) {
                    onlineObserver.stop();
                    return;
                }

                if (GameDatabaseConnection.isGameStarted(joinedGameAnalytics.gameID())) {
                    joinGame();
                    return;
                }

                if (GameDatabaseConnection.isGameEnded(joinedGameAnalytics.gameID())) {
                    joinedGameAnalytics = null;
                    return;
                }

            } catch (Exception e) {
                errorInterrupt(e);
            }
        });
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
            errorInterrupt(e);

            return null;
        }
    }

    public PrettyMenuView getView() {
        return view;
    }

    public boolean isConnected() {
        return session != null;
    }

    public void startSingleGame() {
        List<Player> players = new LinkedList<>();

        Player p = Player.createHumanPlayer("Player", null);
        Player bot = Player.createBotPlayer();

        players.add(p);
        players.add(bot);

        GameController controller = new GameController(players, p, null, false);
        GameView gameView = controller.getGameView();

        SwingUtilities.invokeLater(() -> {
            GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
            parent.setContentPane(gameView);
            gameView.setVisible(true);
            gameView.revalidate();
            gameView.repaint();

            onlineObserver.stop();
        });
    }

    public PlayerAnalytics getPlayerAnalytics(String uid) {
        try {
            return GameDatabaseConnection.getPlayerAnalytics(uid);
        } catch (Exception e) {
            errorInterrupt(e);
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
            errorInterrupt(e);
            return null;
        }
    }

    public List<PreGameAnalytics> getAvailableGames() {
        try {
            return GameDatabaseConnection.getAvailableGames();
        } catch (Exception e) {
            errorInterrupt(e);
            return null;
        }
    }

    public void joinPreGame(PreGameAnalytics gameAnalytics) {
        try {
            if (!GameDatabaseConnection.isGameStarted(gameAnalytics.gameID())
                    && !GameDatabaseConnection.isGameFull(gameAnalytics.gameID())) {
                GameDatabaseConnection.addPlayer(gameAnalytics.gameID(), session);

                this.joinedGameAnalytics = gameAnalytics;
                this.onlineObserver.start();
            } else {
                // TODO : Should show notification
            }
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    private Player joinGame() {
        this.onlineObserver.stop();
        try {
            List<Player> currentPlayers = GameDatabaseConnection.getAllPlayers(joinedGameAnalytics.gameID());
            Optional<Player> currentPlayerOpt = currentPlayers.stream()
                    .filter(e -> e.getUID().equals(session.uid()))
                    .findFirst();
            if (currentPlayerOpt.isEmpty()) {
                throw new Exception();
            }

            Player currentPlayer = currentPlayerOpt.get();
            GameController controller = new GameController(
                    currentPlayers,
                    currentPlayer,
                    joinedGameAnalytics.gameID(),
                    true);
            GameView gameView = controller.getGameView();
            GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);

            parent.setContentPane(gameView);
            parent.revalidate();
            parent.repaint();

            return currentPlayer;
        } catch (Exception e) {
            errorInterrupt(e);

            return null;
        }
    }

    public void createMultiGame(int numberOfPlayer) {
        try {
            PreGameAnalytics gameAnalytics = GameDatabaseConnection.createGame(session, numberOfPlayer);
            joinPreGame(gameAnalytics);
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    public void launchMultiGame() {
        try {
            GameDatabaseConnection.startGame(joinedGameAnalytics.gameID());
            Player currentPlayer = joinGame();
            GameDatabaseConnection.setCurrentPlayer(
                    joinedGameAnalytics.gameID(),
                    currentPlayer.getUID());
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    public void abortMutiGame() {
        if (!view.aMultiGameIsLaunching())
            return;
        try {
            GameDatabaseConnection.removeGame(joinedGameAnalytics.gameID());
            joinedGameAnalytics = null;
        } catch (Exception e) {
            errorInterrupt(e);
        }
        view.setAMultiGameIsLaunching(false);
    }

    public void quitGame() {
        try {
            GameDatabaseConnection.removePlayer(session.uid());
        } catch (Exception e) {
            errorInterrupt(e);
        }
        view.setHaveJoinAGame(false);
    }

    public void saveSession() {
        try {
            FileOutputStream fos = new FileOutputStream(FILE_OUTPUT);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(session);
            oos.close();
            fos.close();
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    public void loadSession() {
        try {
            FileInputStream fis = new FileInputStream(FILE_OUTPUT);

            if (fis.available() > 0) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                session = (PlayerCredentials) ois.readObject();
                ois.close();
            }

            fis.close();
        } catch (Exception e) {
            errorInterrupt(e);
        }

    }

    public void setSession(String UID) throws Exception {
        this.session = getPlayerCredentials(UID);
        System.out.println(session);
    }

    public void setSession(PlayerCredentials session) {
        this.session = session;
    }

    private void errorInterrupt(Exception e) {
        GameFrame.showError(e, () -> {
            GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(view);
            onlineObserver.stop();
            parent.dispose();
        });
    }
}