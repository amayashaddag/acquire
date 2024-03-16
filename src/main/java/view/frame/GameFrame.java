package view.frame;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.*;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import control.game.GameController;
import model.game.Player;
import raven.toast.Notifications;
import view.game.GameView;

import com.formdev.flatlaf.FlatDarculaLaf;


/**
 * This is the class Frame for the view of the game
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class GameFrame extends JFrame {
    public final static int DEFAULT_WIDTH = 1200;
    public final static int DEFAULT_HEIGHT = 900;
    public final static String TITLE = "Acquire - Re-edition";

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

    public void setPanel(JPanel panel) {
        this.setContentPane(panel);
        this.revalidate();
        this.repaint();
    }

    /**
     * A graphical display of errors / exceptions
     * @param e : the exception you want display
     * @param task : the task you want to execute (example :  System.exit(1))
     * @apiNote example : GameFrame.showError(new Exception(), () -> System.exit(1))
     */
    public static void showError(Exception e, Runnable task) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        task.run();
    }
}
