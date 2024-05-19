package control.menu;

import java.io.File;
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
import view.assets.MenuResources;
import view.game.GameView;
import view.menu.MenuView;
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
    private MenuView view;

    private Timer onlineObserver;
    private PreGameAnalytics joinedGameAnalytics;

    public static final int DEFAULT_MAX_PLAYERS = 3;
    public static final int ONLINE_OBSERVER_DELAY = 2000;

    private static final int EASY_MODE_NUM_SIMULATIONS = 1;
    private static final int MEDIUM_MODE_NUM_SIMULATIONS = 5;
    private static final int HARD_MODE_NUM_SIMULATIONS = 10;

    public MenuController() {
        loadSession();

        this.onlineObserver = new Timer(ONLINE_OBSERVER_DELAY, (ActionListener) -> {
            try {

                view.updateMultiPlayer();

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

        onlineObserver.start();
    }

    public void start() {
        view = new MenuView(this);
        view.show();
        view.repaint();
        view.revalidate();

        SwingUtilities.invokeLater(() -> {
            GameFrame.currentFrame.setVisible(true);
        });
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

    public MenuView getView() {
        return view;
    }

    public boolean isConnected() {
        return session != null;
    }

    public void startSingleGameEasy(int numberOfPlayer) {
        startSingleGame(numberOfPlayer, EASY_MODE_NUM_SIMULATIONS, false);
    }

    public void startSingleGameMedium(int numberOfPlayer) {
        startSingleGame(numberOfPlayer, MEDIUM_MODE_NUM_SIMULATIONS, false);
    }

    public void startSingleGameHard(int numberOfPlayer) {
        startSingleGame(numberOfPlayer, HARD_MODE_NUM_SIMULATIONS, false);
    }

    public void startSingleGame(int numberOfPlayers, int difficulty, boolean spectator) {
        view.undoUI();

        List<Player> players = new LinkedList<>();

        for (int i = 0; i < numberOfPlayers - 1; i++) {
            Player bot = Player.createBotPlayer();
            players.add(bot);
        }

        Player p = Player.createHumanPlayer("Player", null);
        if (!spectator) {
            players.add(p);
        }

        GameController controller = GameController.createOfflineGameController(players, p, difficulty);
        GameView gameView = controller.getGameView();

        SwingUtilities.invokeLater(() -> {
            GameFrame parent = GameFrame.currentFrame;
            parent.setContentPane(gameView);
            gameView.setVisible(true);
            gameView.revalidate();
            gameView.repaint();
            parent.requestFocus();

            onlineObserver.stop();
        });
    }

    public void startSpectatorGame(int numberOfPlayers, int numberOfSimulations) {
        startSingleGame(numberOfPlayers + 1, numberOfSimulations, true);
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
            } else {
                GameFrame.showInfoNotification(MenuResources.JOINED_GAME_MESSAGE);
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
            GameController controller = GameController.createOnlineGameController(
                    currentPlayers,
                    currentPlayer,
                    joinedGameAnalytics.gameID());
            GameView gameView = controller.getGameView();
            GameFrame parent = GameFrame.currentFrame;

            parent.setContentPane(gameView);
            parent.setVisible(true);
            parent.revalidate();
            parent.repaint();
            parent.requestFocus();

            return currentPlayer;
        } catch (Exception e) {
            errorInterrupt(e);

            return null;
        } finally {
            view.undoUI();
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
            GameDatabaseConnection.removePlayer(session.uid(), joinedGameAnalytics.gameID());
        } catch (Exception e) {
            errorInterrupt(e);
        }
        view.setHaveJoinAGame(false);
    }

    public void saveSession() {
        try {

            File file = new File(FILE_OUTPUT);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
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

            File file = new File(FILE_OUTPUT);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream fis = new FileInputStream(file);

            if (fis.available() > 0) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                PlayerCredentials temp = (PlayerCredentials) ois.readObject();

                if (AuthController.alreadyRegisteredUser(temp.email())) {
                    session = temp;
                }

                ois.close();
            }

            fis.close();
        } catch (Exception e) {
            errorInterrupt(e);
        }

    }

    public void setSession(String UID) throws Exception {
        PlayerCredentials credentials = getPlayerCredentials(UID);
        setSession(credentials);
    }

    public void setSession(PlayerCredentials session) {
        this.session = session;
        saveSession();
        GameFrame.showInfoNotification(MenuResources.loggedInMessage(session.pseudo()));
    }

    private void errorInterrupt(Exception e) {
        GameFrame.showError(e, () -> {
            GameFrame parent = GameFrame.currentFrame;
            onlineObserver.stop();
            parent.dispose();
        });
    }
}