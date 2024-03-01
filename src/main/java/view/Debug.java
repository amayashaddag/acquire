package view;

import javaswingdev.pggb.PanelGlowingGradient;
import model.Corporation;
import net.miginfocom.swing.MigLayout;
import view.game.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import com.formdev.flatlaf.FlatDarculaLaf;

import control.GameController;
import model.Player;

import javax.swing.*;
import javax.swing.plaf.LayerUI;

public class Debug {

    public static void main(String[] args) {
        FlatDarculaLaf.setup();

//        ArrayList<Player> l = new ArrayList<>();
//        l.add(Player.createHumanPlayer("caca1"));
//        l.add(Player.createHumanPlayer("caca2"));
//        l.add(Player.createHumanPlayer("caca3"));
//        l.add(Player.createHumanPlayer("caca4"));
//        l.add(Player.createHumanPlayer("caca5"));
//        GameController c = new GameController(l, l.get(1));
//        c.getGameView().setVisible(true);
//        GameView g = c.getGameView();
//        GameFrame frame = new GameFrame();
//        g.setOn(frame);
//        frame.setVisible(true);

         JFrame g = new JFrame();
         g.setTitle("Acquire");
         g.setSize(1000, 600);
         g.setLocationRelativeTo(null);
         g.setDefaultCloseOperation(3);

         ArrayList<Corporation> l = new ArrayList<>();
         l.add(Corporation.CONTINENTAL);
         l.add(Corporation.AMERICAN);
         ChoiceCorpPane c = new ChoiceCorpPane(l);
         g.add(c);

         g.repaint();
         g.setVisible(true);

        while (c.getChoice() == null) {

        }
        System.out.println(c.getChoice());
    }

    public static class Box<T> {
        private T t;

        public void set(T t) {
            this.t = t;
        }

        public T get() {
            return t;
        }
    }

    public static class Caca extends JDialog {
        private final Box<Corporation> corp;
        private final JPanel contentPanel;
        public Caca(JFrame parent, List<Corporation> corps, Box<Corporation> corp) {
            super(parent, false);
            this.corp = corp;
            this.contentPanel = new JPanel();

            setContentPane(contentPanel);

            setUndecorated(true);
            setSize(300, 100);
            setLocationRelativeTo(parent);

            contentPanel.setLayout(new MigLayout("al center, filly", "10[]10"));
            contentPanel.setOpaque(true);

            for (Corporation c : corps) {
                JButton jb = new JButton() {
                    @Override
                    public void paint(Graphics g) {
                        super.paint(g);
                        g.setColor(Color.RED);
                        g.fillRect(0,0,30,30);
                    }
                };
                jb.addActionListener(e -> {
                    System.out.println("deadza");
                    corp.set(c);
                    System.out.println(corp.get());
                    dispose();
                });
                contentPanel.add(jb);
            }

            setVisible(true);
        }
    }
}
