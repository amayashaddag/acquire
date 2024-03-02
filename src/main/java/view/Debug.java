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

        ArrayList<Player> l = new ArrayList<>();
        l.add(Player.createHumanPlayer("caca1"));
        l.add(Player.createHumanPlayer("caca2"));
        l.add(Player.createHumanPlayer("caca3"));
        l.add(Player.createHumanPlayer("caca4"));
        l.add(Player.createHumanPlayer("caca5"));
        GameController c = new GameController(l, l.get(1));
        c.getGameView().setVisible(true);
        GameView g = c.getGameView();
        GameFrame frame = new GameFrame();
        g.setOn(frame);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        ArrayList<Corporation> corpsList = new ArrayList<>();
        corpsList.add(Corporation.CONTINENTAL);
        corpsList.add(Corporation.TOWER);


//         JFrame g = new JFrame();
//         g.setTitle("Acquire");
//         g.setSize(1000, 600);
//         g.setLocationRelativeTo(null);
//         g.setDefaultCloseOperation(3);
//
//         ArrayList<Corporation> l = new ArrayList<>();
//         l.add(Corporation.CONTINENTAL);
//         l.add(Corporation.TOWER);
//         tools.Box<Corporation> monitor = new tools.Box<>(Corporation.AMERICAN);
//         ChoiceCorpPane c = new ChoiceCorpPane(l, monitor);
//         g.add(c);
//
//         g.repaint();
//
//         SwingUtilities.invokeLater(() -> g.setVisible(true));
//
//         synchronized (monitor) {
//             try {
//                 monitor.wait();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//         System.out.println("finn");
//         System.out.println(monitor.get().toString());
    }

    public void test(GameView g, List<Corporation> corpsList) {
        System.out.println("Le choix est : " + g.getCorporationChoice(corpsList));
    }

    public static class Box<T> {
        public Box(T t) {
            this.t = t;
        }

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
