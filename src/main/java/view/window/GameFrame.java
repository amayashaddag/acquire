package view.window;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.game.GameController;
import model.game.Player;
import raven.toast.Notifications;
import view.Components.Form;
import view.game.GameView;

/**
 * This is the class Frame for the view of the game.
 * Must be use as a static class with {@link GameFrame.currentFrame}.
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class GameFrame extends JFrame {
    public final static int DEFAULT_WIDTH = 1200;
    public final static int DEFAULT_HEIGHT = 900;
    public final static int MAX_LINES_TO_PRINT = 10;

    public final static String TITLE = "Acquire";
    public final static String ERROR_MESSAGE_TITLE = "An error occured during the execution :\n";

    public final static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public static GameFrame currentFrame = new GameFrame();

    private boolean isEmpty = false;

    public GameFrame() {
        super();
        FlatDarculaLaf.setup();

        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Notifications.getInstance().setJFrame(this);
    }

    public static GameFrame getCurrentFrame() {
        return currentFrame;
    }

    public static boolean isEmpty() {
        return currentFrame.isEmpty;
    }

    public static void setGameView(GameController controller, Player player) {
        setForm(new GameView(controller, player));
    }

    /**
     * <p>
     * Set a form on the current frame
     * (example : option page, game page, chat page ...)
     * </p>
     * 
     * @param comp : the empty constructor of the form's
     *             component you want to set
     * @apiNote example : setForm(new MapView())
     */
    private void setFormWork(Form form) {
        this.getContentPane().removeAll();
        form.setOn(this);
        this.repaint();
        this.revalidate();
        this.repaint();
        isEmpty = false;
    }

    public static void setForm(Form form) {
        currentFrame.setFormWork(form);
    }

    /**
     * A graphical display of errors / exceptions
     * 
     * @param e    : the exception you want display
     * @param task : the task you want to execute (example : System.exit(1))
     * @apiNote example : GameFrame.showError(new Exception(), () -> System.exit(1))
     */
    public static void showError(Exception e, Runnable task) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        StackTraceElement[] stackTrace = e.getStackTrace();
        int numberOfLinesToPrint = Math.min(stackTrace.length, MAX_LINES_TO_PRINT); // Limiting to 10 lines

        pw.println(ERROR_MESSAGE_TITLE);

        for (int i = 0; i < numberOfLinesToPrint; i++) {
            pw.println(stackTrace[i].toString());
        }

        JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        task.run();
    }

    public static void showSuccessNotification(String msg) {
        new Thread(() -> {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, msg);
        }).start();
    }

    public static void showErrorNotification(String msg) {
        new Thread(() -> {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, msg);
        }).start();
    }
    
    public static void showInfoNotification(String msg) {
        new Thread(() -> {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, msg);
        }).start();
    }

    public static void showWarningNotification(String msg) {
        new Thread(() -> {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, msg);
        }).start();
    }

    public static void recreateCurrentFrame() {
        currentFrame.dispose();
        currentFrame = new GameFrame();
    }
}
