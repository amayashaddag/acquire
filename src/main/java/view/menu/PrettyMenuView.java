package view.menu;

import view.menu.*;
import view.frame.*;

import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;


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

        menu3d = new Menu3D("SoloPlayer", "MultiPlayer", "Classement",  "Profile", "Setting", "Exit");
        menu3d.addEvent((i) -> {
            switch (i) {
                case 0 -> System.out.println("On lance partie solo");
                case 1 -> System.out.println("On lance partie multi");
                case 2 -> System.out.println("On affiche le classement");
                case 3 -> System.out.println("On affiche le profile");
                case 4 -> System.out.println("On affiche Setting");
                case 5 -> System.exit(0);
            }
        });

        add(menu3d,"x 20%, w 20%, h 50%");
        repaint();
    }



    @Override
    public void setOn(GameFrame g) {
        g.getContentPane().removeAll();
        g.add(this);
        g.repaint();
        g.revalidate();
    }
}