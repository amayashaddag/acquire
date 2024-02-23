package view.game;

import control.GameController;
import model.Player;
import model.Board;
import model.Cell;
import view.Form;
import view.GameFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import raven.toast.Notifications;

/**
 * The panel which has the map
 * 
 * @author Arthur Deck
 * @version 0.1
 * @see view.Form
 */
public class GameView extends Form {

    final protected GameController controller;
    final protected Player player;
    final private JetonsPanel jetonsPanel;
    final private MouseManager mouseListener;
    final private PlayerBoard playerBoard;

    AffineTransform at;

    public GameView(GameController controller, Player player) {
        super();
        this.controller = controller;
        this.player = player;

        this.setLayout(new BorderLayout());
        this.at = new AffineTransform();
        
        this.jetonsPanel = new JetonsPanel(this);
        this.mouseListener = new MouseManager(this);
        this.playerBoard = new PlayerBoard(this);
    }

    public Player getPlayer() {
        return player;
    }

    public GameController getController() {
        return controller;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setTransform(mouseListener.getAffineTransform());
        g2d.drawImage(Ressources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);

        Board board = controller.getBoard();

        int cellWidth = getWidth() / Board.BOARD_WIDTH;
        int cellHeight = getHeight() / Board.BOARD_HEIGHT;

        int x = 0;
        int y = 0;
        for (int row = 0; row < Board.BOARD_WIDTH; row++) { 
            x = -row * cellWidth / 2 + getWidth() / 2 - cellWidth;
            y = row * cellHeight / 2;
            for (int col = 0; col < Board.BOARD_HEIGHT; col++) {
                Cell currentCell = board.getCell(row, col);
                x += cellWidth / 2;
                y += cellHeight / 2;

                if (!currentCell.isDead())
                    g2d.drawImage(Ressources.Assets.GRASS, x, y, cellWidth, cellHeight, this);

                if (currentCell.isOccupied()) { // TODO : mettre des images à la places des ronds
                    int radiusX = cellWidth/2;
                    int radiusY = cellHeight/2;
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(x+radiusX/2, y+radiusY/2, radiusX, radiusY);
                } else if (currentCell.isOwned()) {
                    int radiusX = cellWidth/2;
                    int radiusY = cellHeight/2;
                    Color c = switch (currentCell.getCorporation()) {
                        case WORLDWIDE -> Color.BLUE;
                        case TOWER -> Color.CYAN;
                        case CONTINENTAL -> Color.MAGENTA;
                        case SACKSON -> Color.ORANGE;
                        case AMERICAN -> Color.PINK;
                        case FESTIVAL -> Color.RED;
                        case IMPERIAL -> Color.YELLOW;
                        default -> null;
                    };
                    g2d.setColor(c);
                    g2d.fillOval(x+radiusX/2, y+radiusY/2, radiusX, radiusY);
                }

                if (new tools.Point(row, col).equals(jetonsPanel.getSelection())) {
                    g2d.setColor(new Color(0, 255, 0, 128));
                    int radiusX = cellWidth/3;
                    int radiusY = cellHeight/3;
                    g2d.fillOval(x+radiusX, y+radiusY, radiusX, radiusY);
                }
            }
        }

        g2d.dispose();
    }

    /*------------------ Form ------------------- */

    /**
     * {@link view.Form#setOn(GameFrame)}
     */
    public void setOn(GameFrame g) {
        this.setSize(g.getWidth(), g.getHeight());
        g.getContentPane().add(this);
    }

    public void showSuccessNotification(String msg) {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_RIGHT, msg);
    }

    public void showErrorNotification(String msg) {
        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_RIGHT, msg);
    }
    
    public void showInfoNotification(String msg) {
        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, msg);
    }

    public void showWarningNotification(String msg) {
        Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, msg);
    }

    /**
     * A graphical display of errors / exceptions
     * @param e : the exception you want display
     * @param task : the task you want execute (example :  System.exit(1))
     * @apiNote example : GameFrame.showError(new Exception(), () -> System.exit(1))
     */
    public void showError(Exception e, Runnable task) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(this, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        task.run();
    }
}
