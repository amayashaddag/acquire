package view.game;

import control.GameController;
import model.Corporation;
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
import java.util.List;

import javax.swing.*;

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

        g2d.drawImage(Ressources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
        g2d.setTransform(mouseListener.getAffineTransform());

        Board board = controller.getBoard();

        int cellWidth = getWidth() / Board.BOARD_WIDTH;
        int cellHeight = getHeight() / Board.BOARD_HEIGHT;

        int x = 0;
        int y = 0;
        for (int row = 0; row < Board.BOARD_WIDTH; row++) { 
            x = -row * cellWidth / 2 + getWidth() / 2;
            y = row * cellHeight / 3 + getHeight() / 6;
            for (int col = 0; col < Board.BOARD_HEIGHT; col++) {
                Cell currentCell = board.getCell(row, col);
                x += cellWidth / 2;
                y += (cellHeight / 3);

                if (new tools.Point(row, col).equals(jetonsPanel.getSelection()))
                    g2d.drawImage(Ressources.Assets.SELECTED_CELL, x - cellWidth, y - cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isOwned())
                    g2d.drawImage(Ressources.Assets.getCorpImage(currentCell.getCorporation()), x - cellWidth, y - cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isOccupied())
                    g2d.drawImage(Ressources.Assets.OCCUPIED_CELL, x -cellWidth, y -cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isEmpty())
                    g2d.drawImage(Ressources.Assets.EMPTY_CELL, x -cellWidth, y -cellHeight, cellWidth, cellHeight*2, this);
            }
        }

        g2d.dispose();
    }

    public Corporation getCorporationChoice(List<Corporation> unplacedCorps) {
        // TODO : demander au joueur quelle truc
        return unplacedCorps.get(0);
    }

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
     * @param task : the task you want to execute (example :  System.exit(1))
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
