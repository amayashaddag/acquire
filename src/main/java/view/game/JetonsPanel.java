package view.game;

import tools.AutoSetter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The JPanel for the jetons
 *
 * @author Arthur Deck
 * @version 1
 */
@AutoSetter(typeParam = GameView.class)
public class JetonsPanel extends JPanel {
    final private GameView g;
    private tools.Point selection = null;
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

        for (tools.Point p : g.player.getDeck()) {
            buttonPanel.add(new JetonButton(p));
        }

        return buttonPanel;
    }

    public tools.Point getSelection() {
        return selection;
    }

    @Override
    public void repaint() {
        try {
            if (g.getController().getCurrentPlayer().equals(g.getPlayer()))
                super.repaint();
        } catch (NullPointerException e) {
            // Don't worry it's normal. This arrives at the initialisation of JetonPanel.
        }
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
                            l.add((JetonButton) c);

                    int position = 0;
                    for (int i = 0; i < l.size(); i++) {
                        if (playerDeck[i] != null) {
                            l.get(i).setVisible(true);
                            l.get(i).setPoint(playerDeck[i]);
                            l.get(i).setText(playerDeck[i].toString());
                        } else {
                            l.get(i).setVisible(false);
                            buttonPanel.remove(l.get(i));
                            buttonPanel.revalidate();
                            buttonPanel.add(l.get(i), position);
                            position = (position == 0) ? buttonPanel.getComponentCount()-1 : 0;
                        }
                    }

                    g.repaint();
                }
            });
            this.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
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

        private void setPoint(tools.Point p) {
            this.p.setX(p.getX());
            this.p.setY(p.getY());
        }
    }
}