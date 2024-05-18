package view.login;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import view.assets.LoginInterfaceResources;
import view.assets.MenuResources;
import view.game.PasswordField;
import view.game.TextField;
import view.window.GameFrame;
import control.auth.AuthController;
import control.auth.NotExistingUserException;
import control.auth.WrongPasswordException;
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
        setOpaque(false);
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
            setOpaque(false);
            
            TextField tf1 = new TextField("Email", c);
            tf1.setOpaque(false);
            add(tf1, "w 80%, gapy 5%, wrap");

            PasswordField tf2 = new PasswordField("PassWord", c);
            tf2.setOpaque(false);
            add(tf2, "w 80%, gapy 5%, wrap");

            JLabel jbl = new JLabel();
            jbl.setOpaque(false);
            jbl.setForeground(MenuResources.getColor("red"));
            add(jbl, "w 80%, gapy 5%, wrap");

            JButton btn = new JButton("Login");
            btn.addActionListener((f) -> {
                try {
                    if (tf1.getText().isEmpty() || String.copyValueOf(tf2.getPassword()).isEmpty()) {
                        jbl.setText(LoginInterfaceResources.EMPTY_FIELD);
                        getParent().repaint(); 
                        return;
                    }
                    String res = AuthController.loginWithEmailAndPassword(tf1.getText(),
                        String.copyValueOf(tf2.getPassword()));
                    
                    control.setSession(res);
                    control.getView().multiPlayer(); 
                    control.getView().repaint();
                } catch (NotExistingUserException e) {
                    jbl.setText(LoginInterfaceResources.NOT_EXISTING_USER_MESSAGE);
                    repaint();
                    getParent().repaint(); 
                } catch (WrongPasswordException e) {
                    jbl.setText(LoginInterfaceResources.WRONG_PASSWORD_MESSAGE);
                    getParent().repaint();  
                } catch (Exception e) {
                    GameFrame.showError(e, () -> System.exit(1));
                }
            });
            add(btn, "w 80%, gapy 5%, wrap");
        }
    }
}
