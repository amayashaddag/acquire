package view;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.Component;

import control.GameController;
import javaswingdev.GradientDropdownMenu;
import javaswingdev.MenuEvent;
import model.Player;
import net.miginfocom.swing.MigLayout;

import com.formdev.flatlaf.FlatDarculaLaf;


/**
 * This is the class View 
 */
public class GameFrame extends JFrame {
    final int DEFAULT_WIDTH = 1200;
    final int DEFAULT_HEIGHT = 900;
    final GradientDropdownMenu menu;

    final static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

    /**
     * For inizialise the view
     * 
     * @param player : the player will seen this View
     * @param controller : current game superviser
     */
    public GameFrame(Player player, GameController controller) {
        super();
        FlatDarculaLaf.setup();

        this.player = player;
        this.controller = controller;

        setTitle("Acquire");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menu = getInitialMenu();
        MapView map = new MapView(null, this.getWidth(), this.getHeight());
        this.addMouseWheelListener(map);
        setComponent(map); // TODO : en attente du controleur pour [][]
    }

    Player player;
    GameController controller;

    private GradientDropdownMenu getInitialMenu() {
        GradientDropdownMenu menu = new GradientDropdownMenu();
        menu.addItem("Information", "Player Information","Global statement");
        menu.addItem("Action", "act1", "act2", "...");
        menu.addItem("Chat", "...");
        menu.addItem("Option", "Full Screen", "Normal Screen", "Exit");
        menu.applay(this);

        menu.addEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex, boolean menuItem) {
                if (menuItem) {
                    if (menu.getMenuNameAt(index, subIndex) == "Exit") {
                        System.exit(0);
                    } else if (menu.getMenuNameAt(index, subIndex) == "Full Screen") {
                        device.setFullScreenWindow(GameFrame.this);
                    } else if (menu.getMenuNameAt(index, subIndex) == "Normal Screen") {
                        device.setFullScreenWindow(null);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, menu.getMenuNameAt(index, subIndex));
                    }
                }
            }
        });

        return menu;
    }

    /**
     * This method MUST be use for set a PAGE (example : option page, game page, chat page ...)
     * @param comp : the page's component you want to set
     */
    private void setComponent(Component comp) {
        this.getContentPane().removeAll();
        this.getContentPane().add(comp);
        menu.applay(this);
        this.repaint();
        this.revalidate();
    }

    /**
     * A graphical display of errors / exceptions
     * @param e : the exception you want display
     * @param task : the task you want execute (example :  System.exit(1))
     */
    public void showError(Exception e, Runnable task) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(null, sw.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
        task.run();
    }
}
