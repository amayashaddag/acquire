package view;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import control.GameController;
import model.Player;
import view.game.GameView;

import com.formdev.flatlaf.FlatDarculaLaf;


/**
 * This is the class Frame for the view of the game
 * 
 * @author Arthur Deck
 * @version 0.1
 */
public class GameFrame extends JFrame {
    final int DEFAULT_WIDTH = 1200;
    final int DEFAULT_HEIGHT = 900;

    public final static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    /**
     * For inizialise the view
     * 
     * @param player : the player will seen this View
     * @param controller : current game superviser
     */
    public GameFrame() {
        super();
        FlatDarculaLaf.setup();

        setTitle("Acquire");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        setGameView(null, null);
    }

    public void setGameView(GameController controller, Player player) {
        this.setForm(new GameView(controller, player));
    }

    /**
     * <p> Set a form on the current frame 
     * (example : option page, game page, chat page ...) </p>
     * 
     * @param comp : the empty constructor of the form's 
     * component you want to set
     * @apiNote example : setForm(new MapView())
     */
    private void setForm(Form form) {
        this.getContentPane().removeAll();
        form.setOn(this);
        this.repaint();
        this.revalidate();
    }

    /**
     * A graphical display of errors / exceptions
     * @param e : the exception you want display
     * @param task : the task you want execute (example :  System.exit(1))
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
