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
                x += cellWidth / 2;
                y += cellHeight / 2;
                g2d.drawImage(Ressources.Assets.GRASS, x, y, cellWidth, cellHeight, this);

                Cell currentCell = board.getCell(row, col);
                if (currentCell.isOccupied()) {
                    // y'a un jeton sans entreprise
                } else if (currentCell.isOwned()) {
                    // y'a une entreprise dessus
                } else if (currentCell.isDead()) {
                    // C'est mort
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
     * {@link view.Form#setOn()}
     */
    public void setOn(GameFrame g) {
        this.setSize(g.getWidth(), g.getHeight()); // FIXME map != null
        g.getContentPane().add(this);
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
