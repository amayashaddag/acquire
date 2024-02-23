package view.game;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The JPanel for the jetons
 * 
 * @author Arthur Deck
 * @version 1
 */
public class JetonsPanel extends JPanel {
    final private GameView g;
    tools.Point selection = null;
    JPanel buttonPanel;

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

        for (tools.Point p : g.player.getDeck()) {
            buttonPanel.add(new JetonButton(p));
        }

        return buttonPanel;
    }

    public tools.Point getSelection() {
        return selection;
    }

    private class JetonButton extends JButton {
        tools.Point p;

        JetonButton(tools.Point p) {
            super();
            this.p = p;
            this.setFocusPainted(false);
            this.setText(p.toString());
            this.addActionListener((e) -> {
                g.getController().handleCellPlacing(p, g.getPlayer());

                tools.Point[] playerDeck = g.getPlayer().getDeck();
                if (playerDeck.length == 0) {
                    buttonPanel.removeAll();
                    g.repaint();
                } else {
                    ArrayList<JetonButton> l = new ArrayList<>();
                    for (Component c : buttonPanel.getComponents())
                        if (c instanceof JetonButton)
                            l.add((JetonButton)c);

                    if (l.size() != g.getPlayer().getDeck().length)
                        g.showError(new Exception("length of player's deck different of JetonButton's number"), 
                        () -> buttonPanel = getNewButtonPanel());
                    else {
                        for (int i = 0; i < l.size(); i++) {
                            l.get(i).setPoint(playerDeck[i]);
                            l.get(i).setText(playerDeck[i].toString());
                        }
                    }
                    g.repaint();
                }
            });
            this.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    JetonsPanel.this.selection = JetonButton.this.p;
                    JetonButton.this.setBorder(new ColorableFlatBorder(new Color(104, 203, 44)));
                    g.repaint();
                }

                public void mouseExited(MouseEvent e) {
                    JetonsPanel.this.selection = null;
                    JetonButton.this.setBorder(UIManager.getBorder("Button.border"));
                    g.repaint();
                }
            });
        }

        private void setPoint(tools.Point p) {
            this.p.setX(p.getX());
            this.p.setY(p.getY());
        }
    }
}