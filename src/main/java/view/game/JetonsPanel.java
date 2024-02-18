package view.game;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;

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

        this.buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1,6));

        // for (tools.Point p : g.player.getDeck()) {   // TODO : attendre controleur
        //     buttonPanel.add(new JetonButton(p));
        // }

        for (int i = 0; i < 6; i++) buttonPanel.add(new JetonButton(new tools.Point(i, i)));

        this.add(buttonPanel);
        this.g.add(this, BorderLayout.SOUTH);
    }

    public tools.Point getSelection() {
        return selection;
    }

    @Override
    public void repaint() { // TODO : attendre le conteroleur
        // this.buttonPanel.removeAll();
        // for (tools.Point p : g.player.getDeck()) {
        //     buttonPanel.add(new JetonButton(p));
        // }
        super.repaint();
    }

    private class JetonButton extends JButton implements MouseListener {
        tools.Point p;

        JetonButton(tools.Point p) {
            super();
            this.p = p;
            this.setFocusPainted(false);
            this.setText(p.toString());
            this.addActionListener((e) -> {
                System.out.println("On place le jeton !");
                // TODO : bizarre la fonction place cell ne prend pas en compte le joueur qui pose ?
                // TODO : get current player ?
                // TODO : obtenir le nouveau jeton ???
            });   
            this.addMouseListener(this);
        } 

        public void mouseEntered(MouseEvent e) {
            JetonsPanel.this.selection = this.p;
            this.setBorder(new ColorableFlatBorder(new Color(104, 203, 44)));
            g.repaint();
        }

        public void mouseExited(MouseEvent e) {
            JetonsPanel.this.selection = null;
            this.setBorder(UIManager.getBorder("Button.border"));
            g.repaint();
        }

        public void mousePressed(MouseEvent arg0) {}
        public void mouseClicked(MouseEvent arg0) {}
        public void mouseReleased(MouseEvent arg0) {}
    }
}