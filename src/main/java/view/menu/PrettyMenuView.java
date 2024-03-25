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
    private final JPanel panel = new JPanel();
    private final MigLayout mig = new MigLayout("al center, filly");

    public PrettyMenuView() {
        super();
        setLayout(mig);

        menu3d.addMenuItem("SinglePlayer", () -> singlePlayer());
        menu3d.addMenuItem("MultiPlayer", () -> multiPlayer());
        menu3d.addMenuItem("Classement", () -> classement());
        menu3d.addMenuItem("Profile", () -> profile());
        menu3d.addMenuItem("Setting", () -> settings());
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
    }

    private void multiPlayer() {

    }

    private void classement() {

    }

    private void profile() {
        mig.setComponentConstraints(panel, "x 50%, w 40%, h 70%");
        revalidate();
        panel.removeAll();
        panel.add(new view.login.LoginView());
        panel.setVisible(true);
        repaint();
    }

    private void settings() {

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