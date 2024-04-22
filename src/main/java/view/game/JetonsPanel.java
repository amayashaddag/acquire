package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.tools.Action;
import model.tools.AutoSetter;
import model.tools.Point;
import view.window.GameFrame;

/**
 * The JPanel for the jetons
 *
 * @author Arthur Deck
 * @version 1
 */
@AutoSetter(typeParam = GameView.class)
public class JetonsPanel extends JPanel {
    final private GameView g;
    private Point selection = null;
    private final JPanel buttonPanel;

    JetonsPanel(GameView g) {
        super();
        this.g = g;
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setOpaque(false);

        this.buttonPanel = getNewButtonPanel();

        this.add(buttonPanel);
        this.g.add(this, BorderLayout.SOUTH);
    }

    private JPanel getNewButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 6));

        for (Point p : g.player.getDeck()) {
            buttonPanel.add(new JetonButton(p));
        }

        return buttonPanel;
    }

    public Point getSelection() {
        return selection;
    }

    @Override
    public void repaint() {
        try {
            super.setVisible(g.getController().getCurrentPlayer().equals(g.getPlayer()));
            super.repaint();
            buttonPanel.repaint();
        } catch (NullPointerException e) {
            // Don't worry it's normal. This arrives at the initialisation of JetonPanel.
            new Thread(() -> {
                try {
                    Thread.sleep(10); // We wait a little time for the end of the initialisation of the game and we repaint
                    if (isVisible())
                        SwingUtilities.invokeLater(this::repaint);
                } catch (InterruptedException e2) {
                    GameFrame.showError(e2, this::repaint);
                }
            }).start();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        buttonPanel.setEnabled(enabled);
        for (Component c : buttonPanel.getComponents())
            if (c instanceof JetonButton) {
                JetonButton jb = (JetonButton) c;
                jb.setEnabled(enabled);
            }
    }

    public void updatePlayerDeck() {
        Point[] playerDeck = g.getPlayer().getDeck();
        if (playerDeck.length == 0) {
            buttonPanel.removeAll();
        } else {
            ArrayList<JetonButton> l = new ArrayList<>();
            for (Component c : buttonPanel.getComponents())
                if (c instanceof JetonButton)
                    l.add((JetonButton) c);

            int position = 0;
            for (int i = 0; i < l.size(); i++) {
                if (playerDeck[i] != null) {
                    l.get(i).setVisible(true);
                    l.get(i).setPoint(playerDeck[i]);
                } else {
                    l.get(i).setVisible(false);
                    buttonPanel.remove(l.get(i));
                    buttonPanel.revalidate();
                    buttonPanel.add(l.get(i), position);
                    position = (position == 0) ? buttonPanel.getComponentCount() - 1 : 0;
                }
            }
        }
    }

    private class JetonButton extends JButton {
        Point p;

        JetonButton(Point p) {
            super();
            this.p = p;
            this.setFocusPainted(false);
            this.setText("-");
            this.addActionListener((e) -> {
                new Thread(() -> {
                    Action action = new Action(p, null, null, null);
                    g.getController().handleCellPlacing(action, g.getPlayer());

                    Point[] playerDeck = g.getPlayer().getDeck();
                    if (playerDeck.length == 0) {
                        buttonPanel.removeAll();
                        g.repaint();
                    } else {
                        ArrayList<JetonButton> l = new ArrayList<>();
                        for (Component c : buttonPanel.getComponents())
                            if (c instanceof JetonButton)
                                l.add((JetonButton) c);

                        int position = 0;
                        for (int i = 0; i < l.size(); i++) {
                            if (playerDeck[i] != null) {
                                l.get(i).setVisible(true);
                                l.get(i).setPoint(playerDeck[i]);
                            } else {
                                l.get(i).setVisible(false);
                                buttonPanel.remove(l.get(i));
                                buttonPanel.revalidate();
                                buttonPanel.add(l.get(i), position);
                                position = (position == 0) ? buttonPanel.getComponentCount() - 1 : 0;
                            }
                        }

                        g.repaint();
                    }
                }).start();
            });
            this.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!JetonButton.this.isEnabled()) return;
                    JetonsPanel.this.selection = JetonButton.this.p;
                    JetonButton.this.setBorder(new ColorableArcableFlatBorder(Color.GREEN));
                    g.repaint();
                }

                public void mouseExited(MouseEvent e) {
                    JetonsPanel.this.selection = null;
                    JetonButton.this.setBorder(UIManager.getBorder("Button.border"));
                    g.repaint();
                }
            });
        }

        private void setPoint(Point p) {
            this.p.setX(p.getX());
            this.p.setY(p.getY());
        }
    }
}