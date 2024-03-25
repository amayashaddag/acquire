package view.menu;

import view.menu.*;
import view.frame.*;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import view.assets.MenuRessources;
import javaswingdev.pggb.PanelGlowingGradient;
import control.game.GameController;
import model.game.Player;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The beggining menu of the Game
 *
 * @author Arthur Deck
 * @version 1
 */
public class PrettyMenuView extends Form {
    private final Menu3D menu3d = new Menu3D();
    private JPanel panel = new JPanel();

    public PrettyMenuView() {
        super();
        setLayout(new MigLayout("al center, filly"));

        menu3d.addMenuItem("SinglePlayer", () -> singlePlayer());
        menu3d.addMenuItem("MultiPlayer", () -> System.out.println("Wait it's to soon !"));
        menu3d.addMenuItem("Classement", () -> System.out.println("Classement"));
        menu3d.addMenuItem("Profile", () -> System.out.println("Profile"));
        menu3d.addMenuItem("Setting", () -> System.out.println("Setting"));
        menu3d.addMenuItem("Exit", () -> exit());

        panel.setVisible(false);

        add(menu3d,"x 15%, y 30%, w 25%, h 50%");
        add(panel, "x 60%, w 30%, h 50%");
        repaint();
    }

    private void singlePlayer() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                ArrayList<Player> l = new ArrayList<>();
                l.add(Player.createHumanPlayer("Max"));
                l.add(Player.createHumanPlayer("Xi"));
                l.add(Player.createHumanPlayer("Best"));
                l.add(Player.createHumanPlayer("Of"));
                GameController c = new GameController(l, l.get(0));
                view.frame.GameFrame.currentFrame.setForm(c.getGameView());
            }
        }).start();

//        panel.removeAll();
//        panel.setOpaque(false);
//        panel.setVisible(true);
//        panel.setLayout(new MigLayout("al center, filly, ins 3, wrap 1"));
//
//
//        Color colorEz = Color.GREEN;
//        PanelGlowingGradient ez = new PanelGlowingGradient() {
//            @Override
//            protected void paintComponent (Graphics g){
//                super.paintComponent(g);
//                g.setFont(g.getFont().deriveFont(Font.BOLD));
//                g.setColor(Color.BLACK);
//                g.drawString("EASY",
//                        getWidth()/2 - 5,
//                        getHeight()/2 - 5);
//            }
//        };
//        ez.setGradientColor1(colorEz.darker());
//        ez.setGradientColor2(colorEz.brighter());
//        ez.setBackground(colorEz.darker());
//        ez.setBackgroundLight(colorEz.brighter());
//
//        Color colorMed = Color.ORANGE;
//        PanelGlowingGradient medium = new PanelGlowingGradient() {
//            @Override
//            protected void paintComponent (Graphics g){
//                super.paintComponent(g);
//                g.setFont(g.getFont().deriveFont(Font.BOLD));
//                g.setColor(Color.BLACK);
//                g.drawString("MEDIUM",
//                        getWidth()/2 - 5,
//                        getHeight()/2 - 5);
//            }
//        };
//        medium.setGradientColor1(colorMed.darker());
//        medium.setGradientColor2(colorMed.brighter());
//        medium.setBackground(colorMed.darker());
//        medium.setBackgroundLight(colorMed.brighter());
//
//
//        MouseAdapter ma = new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                ArrayList<Player> l = new ArrayList<>();
//                l.add(Player.createHumanPlayer("Max"));
//                l.add(Player.createHumanPlayer("Xi"));
//                l.add(Player.createHumanPlayer("Best"));
//                l.add(Player.createHumanPlayer("Of"));
//                GameController c = new GameController(l, l.get(0));
//                view.frame.GameFrame.currentFrame.setForm(c.getGameView());
//            }
//        };
//
//        ez.addMouseListener(ma);
//        medium.addMouseListener(ma);
//
//        panel.add(ez, "w 40%, h 20%");
//        panel.add(medium, "w 30%, h 15%, wrap");
//
//        revalidate();
//        repaint();
    }

    private void exit() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawImage(MenuRessources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setOn(GameFrame g) {
        g.getContentPane().removeAll();
        g.add(this);
        g.repaint();
        g.revalidate();
    }
}