package view.menu;

import view.menu.*;
import view.frame.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import view.assets.MenuRessources;

/**
 * The beggining menu of the Game
 *
 * @author Arthur Deck
 * @version 1
 */
public class PrettyMenuView extends Form {
    private final Menu3D menu3d;

    public PrettyMenuView() {
        super();
        setLayout(new MigLayout("al center, filly"));

        menu3d = new Menu3D();
        menu3d.addMenuItem("SoloPlayer", () -> System.out.println("SoloPlayer"));
        menu3d.addMenuItem("MultiPlayer", () -> System.out.println("SoloPlayer"));
        menu3d.addMenuItem("Classement", () -> System.out.println("Classement"));
        menu3d.addMenuItem("Profile", () -> System.out.println("Profile"));
        menu3d.addMenuItem("Setting", () -> System.out.println("Setting"));
        menu3d.addMenuItem("Exit", () -> new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }).start());

        add(menu3d,"x 15%, w 25%, h 50%");
        repaint();
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