package view.game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.ui.FlatScrollBarUI;

import control.game.GameController;
import model.game.Board;
import model.game.Cell;
import model.game.Corporation;
import model.game.Player;
import model.tools.Point;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;
import view.Components.Form;
import view.assets.GameResources;
import view.window.GameFrame;

/**
 * The panel which has the map
 * 
 * @author Arthur Deck
 * @version 0.1
 * @see Form
 */
public class GameView extends Form {

    final protected GameController controller;
    final protected Player player;
    final private JetonsPanel jetonsPanel;
    final private MouseManager mouseListener;
    final private PlayerBoard playerBoard;
    final private PausePane pausePane;

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
        this.pausePane = new PausePane(this);
    }

    public Player getPlayer() {
        return player;
    }

    public GameController getController() {
        return controller;
    }

    public void pause() {
        pausePane.pause();
    }

    private void paintMap(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.drawImage(GameResources.GImage.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
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

                if (new Point(row, col).equals(jetonsPanel.getSelection()))
                    g2d.drawImage(GameResources.GImage.SELECTED_CELL, x - cellWidth, y - cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isOwned())
                    g2d.drawImage(GameResources.GImage.getCorpImage(currentCell.getCorporation()), x - cellWidth, y - cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isOccupied())
                    g2d.drawImage(GameResources.GImage.OCCUPIED_CELL, x -cellWidth, y -cellHeight, cellWidth, cellHeight*2, this);
                else if (currentCell.isEmpty())
                    g2d.drawImage(GameResources.GImage.EMPTY_CELL, x -cellWidth, y -cellHeight, cellWidth, cellHeight*2, this);
            }
        }

        g2d.dispose();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isEnabled()) {
            paintMap(g);
        } else {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            paintMap(g2d);
            g2d.dispose();
            g.drawImage(
                GameResources.GImage.applyGaussianBlur(image, 2, 1), 
                0, 0, this);
        }
        
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
        model.tools.Box<Corporation> monitor = new model.tools.Box<>(null);
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
        setEnabled(false);
        jetonsPanel.setVisible(false);

        Object monitor = new Object();

        class Pane extends JPanel {
            private int choice;
            private final Corporation corp;
            Pane(Map.Entry<Corporation, Integer> entry) {
                super();
                corp = entry.getKey();

                setOpaque(false);
                setLayout(new MigLayout("al center, filly, ins 0, wrap 1"));

                GlowingItemCorp gli = new GlowingItemCorp(entry.getKey()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setFont(g.getFont().deriveFont(Font.BOLD));
                        g.setColor(getColor().darker());
                        g.drawString(entry.getValue().toString(),
                                getShadowSize() + getBorderSize() + 5,
                                2*getShadowSize());
                    }
                };

                JLabel jlChoice = new JLabel("0");
                jlChoice.setFont(jlChoice.getFont().deriveFont(Font.BOLD));
                jlChoice.setForeground(gli.getColor().brighter());

                gli.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        choice = (choice+1)%(entry.getValue()+1);
                        jlChoice.setText(""+choice);
                        jlChoice.repaint();
                    }
                    public void mousePressed(MouseEvent mouseEvent) {}
                    public void mouseReleased(MouseEvent mouseEvent) {}
                    public void mouseEntered(MouseEvent mouseEvent) {}
                    public void mouseExited(MouseEvent mouseEvent) {}
                });

                add(gli, "w 100%, h 100%");
                add(jlChoice, "w 5%, h 5%, align center");
            }

            public int getChoice() {
                return choice;
            }

            public Corporation getCorp() {return corp;}
        }

        JPanel jp = new JPanel();
        jp.setOpaque(false);
        jp.setLayout(new MigLayout("al center, filly"));

        for (Map.Entry<Corporation, Integer> entry : possibleBuyingStocks.entrySet()) {
            jp.add(new Pane(entry), "w 15%, h 30%");
        }

        JButton buyBtn = new JButton("Buy");
        buyBtn.addActionListener((e) -> {
            int c = 0;
            HashMap<Corporation, Integer> panier = new HashMap<>();

            for (Component comp : jp.getComponents())
                if (comp instanceof Pane) {
                    c += ((Pane) comp).getChoice();
                    panier.put(((Pane) comp).getCorp(), ((Pane) comp).getChoice());
                }

            int totalPriceToPay = controller.calculateStocksPrice(panier);

            if (c > 3 || c < 0)
                GameFrame.showErrorNotification(GameNotifications.CANNOT_BUY_MORE_THAN_THREE);
            else if (player.hasEnoughCash(totalPriceToPay)) {
                controller.buyChosenStocks(panier, totalPriceToPay, player);
                synchronized (monitor) {
                    monitor.notify();
                }
            } else
                GameFrame.showErrorNotification(GameNotifications.NOT_ENOUGH_CASH);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(buyBtn);

        jp.add(btnPanel, "dock south, al center, gapbottom 30");
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

    /**
     * This function displays an interface to the player to choose whether the given stocks
     * in parameter will be held, sold or traded to bank.
     *
     * @param major This parameter refers to the major corporation that acquired all other ones.
     *              This parameter is essential because it is used while trading to know which corporation
     *              the controller should trade player stocks with.
     *
     * @apiNote This function, once it knows the corporations to sell/keep/trade, calls
     * the 2 functions that will sell and trade stocks in {@link GameController}
     * Call this method will freeze the current thread, so do not call it in the EDT.
     */
    public void chooseSellingKeepingOrTradingStocks(Map<Corporation, Integer> stocks, Corporation major) {
        setEnabled(false);
        jetonsPanel.setVisible(false);

        Object monitor = new Object();

        class Pane extends JPanel {
            final Map.Entry<Corporation, Integer> entry;
            SKT choice;

            Pane(Map.Entry<Corporation, Integer> entry) {
                super();
                choice = SKT.KEEP;
                this.entry = entry;

                setOpaque(false);
                setLayout(new MigLayout("al center, filly, ins 0, wrap 1"));

                GlowingItemCorp gli = new GlowingItemCorp(entry.getKey()) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setFont(g.getFont().deriveFont(Font.BOLD));
                        g.setColor(getColor().darker());
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

            public Map.Entry<Corporation, Integer> getEntry() {
                return entry;
            }

            public SKT getChoice() {
                return choice;
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

        JButton confirmBtn = new JButton("Confirm");
        confirmBtn.addActionListener((e) -> {
            synchronized (monitor) {
                monitor.notify();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(confirmBtn);

        jp.add(btnPanel, "dock south, al center, gapbottom 30");
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

        Map<Corporation, Integer> toSell = new HashMap<>();
        Map<Corporation, Integer> toTrade = new HashMap<>();

        for (Component comp : jp.getComponents())
            if (comp instanceof Pane) {
                Map.Entry<Corporation, Integer> entry = ((Pane) comp).getEntry();
                Pane.SKT choice = ((Pane) comp).getChoice();
                if (choice == Pane.SKT.SELL) {
                    toSell.put(entry.getKey(), entry.getValue());
                } else if (choice == Pane.SKT.TRADE) {
                    toTrade.put(entry.getKey(), entry.getValue());
                }
            }
 
        controller.sellStocks(toSell, player);
        controller.tradeStocks(toTrade, player, major);

        remove(jp);
        jetonsPanel.setVisible(true);
        setEnabled(true);
        repaint();
    }

    public void recieveChat(Player p, String message, boolean notify) {
        if (notify)
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_LEFT, 
            p.getPseudo() + " : " + message);
        pausePane.recieveChat(p, message);
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
        if (playerBoard != null) {
            playerBoard.repaint();
        }
        
        if (jetonsPanel != null) {
            jetonsPanel.repaint();
        }
    }

    /**
     * {@link Form#setOn(GameFrame)}
     */
    public void setOn(GameFrame g) {
        this.setSize(g.getWidth(), g.getHeight());
        g.setContentPane(this);
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
        pw.println(e.getMessage());
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(null, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        task.run();
    }

    public static void showError(Exception e) {showError(e, ()->{});}

    public void endGame() {
        JScrollPane scroll = new JScrollPane(new EndGame(controller));
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setBackground(new Color(0,0,0,0));
        class FSBUI extends FlatScrollBarUI {
            @Override
            protected Color getThumbColor(JComponent c, boolean hover, boolean pressed) {
                return EndGame.colorBtnSBar;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
        }
        scrollBar.setUI(new FSBUI());

        pausePane.blurWith(scroll);
    }

    public void updatePlayerDeck() {
        jetonsPanel.updatePlayerDeck();
    }

    public void showSuccessNotification(String msg) {
        SwingUtilities.invokeLater(() -> {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, 
            Notifications.Location.TOP_RIGHT, msg);
        });
    }

    public void showErrorNotification(String msg) {
        SwingUtilities.invokeLater(() -> {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
            pausePane.isBlur() ? Notifications.Location.TOP_LEFT : Notifications.Location.TOP_RIGHT, 
            msg);
        });
    }
    
    public void showInfoNotification(String msg) {
        SwingUtilities.invokeLater(() -> {
            Notifications.getInstance().show(Notifications.Type.INFO, 
            pausePane.isBlur() ? Notifications.Location.TOP_LEFT : Notifications.Location.TOP_RIGHT, 
            msg);
        });
    }

    public void showWarningNotification(String msg) {
        SwingUtilities.invokeLater(() -> {
            Notifications.getInstance().show(Notifications.Type.WARNING, 
            pausePane.isBlur() ? Notifications.Location.TOP_LEFT : Notifications.Location.TOP_RIGHT, 
            msg);
        });
    }
}
