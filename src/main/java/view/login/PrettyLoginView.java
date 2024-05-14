package view.login;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import view.game.TextField;

import control.menu.MenuController;
import net.miginfocom.swing.MigLayout;

/**
 * @author Deck Arthur
 */
public class PrettyLoginView extends JPanel {
    private final RegisterPane rp;
    private final LoginPane lp;
    private final MenuController control;

    public PrettyLoginView(MenuController control, Color c) {
        super();

        rp = new RegisterPane(c);
        lp = new LoginPane(c);
        this.control = control;

        setLayout(new MigLayout());
        setBackground(Color.GREEN);
        login();
    }

    public void login() {
        removeAll();
        add(lp, "w 100%, h 100%");
        repaint();
    }

    public void register() {
        removeAll();
        add(rp, "w 100%, h 100%");
        repaint();
    }

    class RegisterPane extends JPanel {
        RegisterPane(Color c) {
            setLayout(new MigLayout());

        }
    }

    class LoginPane extends JPanel {
        LoginPane(Color c) {
            setLayout(new MigLayout("al center"));
            
            TextField tf1 = new TextField("Email", c);
            tf1.setOpaque(false);
            add(tf1, "w 80%, gapy 5%, wrap");

            TextField tf2 = new TextField("PassWord", c);
            tf2.setOpaque(false);
            add(tf2, "w 80%, gapy 5%, wrap");

            JButton btn = new JButton("Login");
            add(btn, "w 80%, gapy 5%, wrap");
        }
    }
}
