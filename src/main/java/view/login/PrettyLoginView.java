package view.login;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.JLabel;
import view.assets.LoginInterfaceResources;
import view.assets.MenuResources;
import view.game.PasswordField;
import view.game.TextField;
import view.window.GameFrame;
import control.auth.AlreadyRegisteredUserException;
import control.auth.AuthController;
import control.auth.NotExistingUserException;
import control.auth.NotStrongEnoughPasswordException;
import control.auth.TooLongPasswordException;
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
    private final Runnable task;

    public PrettyLoginView(MenuController control, Color c) {
        this(control, c, null);
    }

    public PrettyLoginView(MenuController control, Color c, Runnable r) {
        super();

        rp = new RegisterPane(c);
        lp = new LoginPane(c);
        this.control = control;
        this.task = (r == null) ? ()->{} : r;

        setLayout(new MigLayout());
        setOpaque(false);
        register();
    }

    public void login() {
        removeAll();
        add(lp);
        repaint();
        try {
            getParent().revalidate();
            getParent().repaint();
        } catch (Exception e) {}
    }

    public void register() {
        removeAll();
        add(rp);
        repaint();
        try {
            getParent().revalidate();
            getParent().repaint();
        } catch (Exception e) {}
    }

    class RegisterPane extends JPanel {
        Color color;

        RegisterPane(Color c) {
            this.color = c;

            setOpaque(false);
            setLayout(new MigLayout("al center"));

            TextField tf0 = new TextField("Username", c);
            tf0.setOpaque(false);
            add(tf0, "w 80%, gapy 5%, wrap");

            TextField tf1 = new TextField("Email", c);
            tf1.setOpaque(false);
            add(tf1, "w 80%, gapy 5%, wrap");

            PasswordField tf2 = new PasswordField("PassWord", c);
            tf2.setOpaque(false);
            tf2.setShowAndHide(true);
            add(tf2, "w 80%, gapy 5%, wrap");

            JLabel jbl = new JLabel();
            jbl.setOpaque(false);
            jbl.setForeground(MenuResources.getColor("red").darker());
            add(jbl, "w 80%, gapy 5%, wrap");

            JButton btn = new JButton("Sign up");
            btn.addActionListener((f) -> {
                try {
                    if (tf0.getText().isEmpty() || tf1.getText().isEmpty()
                            || String.copyValueOf(tf2.getPassword()).isEmpty()) {
                        jbl.setText(LoginInterfaceResources.EMPTY_FIELD);
                        return;
                    }
                    String res = AuthController.signUpWithEmailAndPassword(tf1.getText(), tf0.getText(),
                            String.copyValueOf(tf2.getPassword()));
                    control.setSession(res);

                    task.run();
                } catch (AlreadyRegisteredUserException e) {
                    jbl.setText(LoginInterfaceResources.ALREADY_REGISTERED_USER_MESSAGE);
                } catch (NotStrongEnoughPasswordException e) {
                    jbl.setText(LoginInterfaceResources.NOT_STRONG_ENOUGH_PASSWORD_MESSAGE);
                } catch (TooLongPasswordException e) {
                    jbl.setText(LoginInterfaceResources.TOO_LONG_PASSWORD_MESSAGE);
                } catch (Exception e) {
                    GameFrame.showError(e, () -> System.exit(1));
                }
            });
            add(btn, "w 80%, gapy 5%, wrap");
            add(createLoginLabel(), "w 80%, gapy 5%, wrap");
        }

        private JPanel createLoginLabel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null");
            JButton cmdRegister = new JButton("<html><a href=\"#\">Login</a></html>");
            cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:3,3,3,3");
            cmdRegister.setContentAreaFilled(false);
            cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cmdRegister.addActionListener(e -> {
                login();
            });
            JLabel label = new JLabel("Already have an account ?");
            label.putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]foreground:lighten(@foreground,30%);" +
                    "[dark]foreground:darken(@foreground,30%)");
            panel.add(label);
            panel.add(cmdRegister);
            label.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
            cmdRegister.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
            label.setForeground(color);
            label.setOpaque(false);
            cmdRegister.setOpaque(false);
            panel.setOpaque(false);
            return panel;
        }
    }

    class LoginPane extends JPanel {   
        Color color;

        LoginPane(Color c) {
            this.color = c;

            setLayout(new MigLayout("al center"));
            setOpaque(false);
            
            TextField tf1 = new TextField("Email", c);
            tf1.setOpaque(false);
            add(tf1, "w 80%, gapy 5%, wrap");

            PasswordField tf2 = new PasswordField("PassWord", c);
            tf2.setOpaque(false);
            tf2.setShowAndHide(true);
            add(tf2, "w 80%, gapy 5%, wrap");

            JLabel jbl = new JLabel();
            jbl.setOpaque(false);
            jbl.setForeground(MenuResources.getColor("red").darker());
            add(jbl, "w 80%, gapy 5%, wrap");

            JButton btn = new JButton("Login");
            btn.addActionListener((f) -> {
                try {
                    if (tf1.getText().isEmpty() || String.copyValueOf(tf2.getPassword()).isEmpty()) {
                        jbl.setText(LoginInterfaceResources.EMPTY_FIELD);
                        return;
                    }
                    String res = AuthController.loginWithEmailAndPassword(tf1.getText(),
                        String.copyValueOf(tf2.getPassword()));
                    
                    control.setSession(res);
                    task.run(); 
                } catch (NotExistingUserException e) {
                    jbl.setText(LoginInterfaceResources.NOT_EXISTING_USER_MESSAGE);
                } catch (WrongPasswordException e) {
                    jbl.setText(LoginInterfaceResources.WRONG_PASSWORD_MESSAGE);
                } catch (Exception e) {
                    GameFrame.showError(e, () -> System.exit(1));
                }
            });
            add(btn, "w 80%, gapy 5%, wrap");
            add(createSignupLabel(), "w 80%, gapy 5%, wrap");
        }

        private JPanel createSignupLabel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null");
            JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
            cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:3,3,3,3");
            cmdRegister.setContentAreaFilled(false);
            cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cmdRegister.addActionListener(e -> {
                register();
            });
            JLabel label = new JLabel("Don't have an account ?");
            label.putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]foreground:lighten(@foreground,30%);" +
                    "[dark]foreground:darken(@foreground,30%)");
            panel.add(label);
            panel.add(cmdRegister);
            label.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
            cmdRegister.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
            label.setForeground(color);
            label.setOpaque(false);
            cmdRegister.setOpaque(false);
            panel.setOpaque(false);
            return panel;
        }
    }
}
