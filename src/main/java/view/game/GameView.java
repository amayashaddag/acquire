package view.game;

import control.GameController;
import model.Corporation;
import model.Player;
import model.Board;
import model.Cell;
import net.miginfocom.swing.MigLayout;
import tools.Point;
import view.Form;
import view.GameFrame;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
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

    /**
     * Ask the player to choice into a Corporations list and return his choice.
     * Use in {@link GameController#handleCellPlacing(Point, Player)}.
     *
     * @apiNote call this method will freeze the current thread, so do not call it in the EDT
     * @param unplacedCorps list of possible choice
     * @return the choice of the Player
     */
    public Corporation getCorporationChoice(List<Corporation> unplacedCorps) {
        setEnabled(false);
        jetonsPanel.setVisible(false);
        tools.Box<Corporation> monitor = new tools.Box<>(unplacedCorps.get(0));
        ChoiceCorpPane c = new ChoiceCorpPane(unplacedCorps, monitor);
        add(c, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                showError(e, () -> System.exit(1));
            }
        }

        jetonsPanel.setVisible(true);
        setEnabled(true);
        return monitor.get();
    }

    /**
     * This function handles the buying stocks process which is divided into 3 steps :
     * 1 - Displaying the possible choices for the player.
     * 2 - From the chosen corporations to buy, calculate the total price to pay using
     * {@link GameController#calculateStocksPrice(Map)} function.
     * 3 - Test if player has enough cash.
     * 4 - Call {@link GameController#buyChosenStocks(Map, int, Player)} function to handle the
     * final buying.
     *
     * @param possibleBuyingStocks represents all the possible stocks to buy by the player.
     * @apiNote call this method will freeze the current thread, so do not call it in the EDT
     */
    public void chooseStocksToBuy(Map<Corporation, Integer> possibleBuyingStocks) {
        // TODO : Implement this function
    }

    /**
     * This function displays an interface to the player to choose whether the given stocks
     * in parameter will be held, sold or traded to bank.
     *
     * @apiNote This function, once it knows the corporations to sell/keep/trade, calls
     * the 2 functions that will sell and trade stocks in {@link GameController}
     */
    public void chooseSellingKeepingOrTradingStocks(Map<Corporation, Integer> stocks) {
        setEnabled(false);
        jetonsPanel.setVisible(false);
        Object monitor = new Object();

        class Pane extends JPanel {
            SKT choice;
            Pane(Map.Entry<Corporation, Integer> entry) {
                super();
                choice = SKT.KEEP;
                setOpaque(false);
                setLayout(new MigLayout("al center, filly, ins 0, wrap 1"));

                GlowingItemCorp gli = new GlowingItemCorp(entry.getKey()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setFont(g.getFont().deriveFont(Font.BOLD));
                        g.drawString(entry.getValue().toString(),
                                getShadowSize() + getBorderSize() + 5,
                                2*getShadowSize());
                    }
                };

                JLabel jlChoice = new JLabel(choice.toString());
                jlChoice.setFont(jlChoice.getFont().deriveFont(Font.BOLD));
                jlChoice.setForeground(gli.getColor().brighter());

                gli.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        choice = switch (choice) {
                            case SELL -> SKT.KEEP;
                            case KEEP -> SKT.TRADE;
                            case TRADE -> SKT.SELL;
                        };
                        jlChoice.setText(choice.toString());
                        jlChoice.repaint();
                    }
                    public void mousePressed(MouseEvent mouseEvent) {}
                    public void mouseReleased(MouseEvent mouseEvent) {}
                    public void mouseEntered(MouseEvent mouseEvent) {}
                    public void mouseExited(MouseEvent mouseEvent) {}
                });

                add(gli, "w 100%, h 100%");
                add(jlChoice, "w 30%, h 5%, align center");
            }

            enum SKT {
                SELL,
                KEEP,
                TRADE
            }
        }

        JPanel jp = new JPanel();
        jp.setOpaque(false);
        jp.setLayout(new MigLayout("al center, filly"));

        for (Map.Entry<Corporation, Integer> entry :stocks.entrySet()) {
            jp.add(new Pane(entry), "w 15%, h 30%");
        }

        JButton confirmBtn = new JButton("confirm");

        jp.add(confirmBtn, "w 30, h 20, dock south, al center, gapbottom 30");
        add(jp, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                showError(e, () -> System.exit(1));
            }
        }

        remove(jp);
        jetonsPanel.setVisible(true);
        setEnabled(true);
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mouseListener.setEnabled(enabled);
        playerBoard.setEnabled(enabled);
        jetonsPanel.setEnabled(enabled);
    }

    @Override
    public void repaint() {
        super.repaint();

        if (playerBoard != null)
            playerBoard.repaint();

        if (jetonsPanel != null)
            jetonsPanel.repaint();
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
        pw.println(e.getMessage());
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(this, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        task.run();
    }

    public void showError(Exception e) {showError(e, ()->{});}
}
