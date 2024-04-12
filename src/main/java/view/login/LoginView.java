package view.login;

import control.game.GameController;
import model.game.Player;
import org.checkerframework.checker.units.qual.N;
import view.assets.Fonts;
import view.assets.LoginInterfaceResources;

import com.formdev.flatlaf.extras.components.FlatButton;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import view.frame.Form;
import view.frame.GameFrame;
import view.game.GameView;

public class LoginView extends JPanel {

    private final String NOT_EXISTING_USER_MESSAGE = "Couldn't found your account. Please try again.";

    private final String ALREADY_REGISTERED_USER_MESSAGE = "This account already exists.";

    private final String WRONG_PASSWORD_MESSAGE = "Your password or your email is wrong. Please try again.";

    private final String NOT_STRONG_ENOUGH_PASSWORD_MESSAGE = "To week password.";

    private final JLabel titleLabel;

    private final JPanel loginComponentContainer;

    private final JLabel errorLabel;

    private final JPanel loginAndSignUpButtonContainer = new Form() {
        @Override
        public void setOn(GameFrame g) {
            this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4));
        }
    };
    private final JPanel createAccountAndComeBackToLoginContainer = new Form() {
        @Override
        public void setOn(GameFrame g) {
            this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4));
        }
    };

    public LoginView() {

        loginComponentContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                g.setContentPane(this);
            }
        };

        loginComponentContainer.setLayout(new BoxLayout(loginComponentContainer, BoxLayout.Y_AXIS));
        loginComponentContainer.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/3,GameFrame.DEFAULT_HEIGHT*3/5));


        titleLabel = new JLabel(LoginInterfaceResources.LOGIN_BUTTON_TEXT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(Fonts.TITLE_FONT);

        String circle = "\u24EA"; // Unicode du caractère de cercle
        String exclamation = "\u0021"; // Unicode du point d'exclamation
        String circleExclamation = circle + exclamation;
        errorLabel = new JLabel(circleExclamation+"test");
        errorLabel.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        errorLabel.setForeground(new Color(138,50,36));
        //errorLabel.setVisible(false);

        FlatButton loginButton = new FlatButton();
        loginButton.setText(LoginInterfaceResources.LOGIN_BUTTON_TEXT);
        loginButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);


        FlatButton signInButton = new FlatButton();
        signInButton.setText(LoginInterfaceResources.SIGN_UP_BUTTON_TEXT);
        signInButton.addActionListener((ActionListener)-> fromLoginMenuToSignInMenu());
        signInButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        FlatButton createAccountButton = new FlatButton();
        createAccountButton.setText(LoginInterfaceResources.CREATE_ACCOUNT_BUTTON_TEXT);
        createAccountButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        FlatButton comeBackToLoginButton = new FlatButton();
        comeBackToLoginButton.setText(LoginInterfaceResources.GO_TO_LOGIN_PAGE_BUTTON_TEXT);
        comeBackToLoginButton.addActionListener((ActionListener) -> fromSignInMenuToLoginMenu());
        comeBackToLoginButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        FlatButton offlineModeButton = new FlatButton();
        offlineModeButton.setText(LoginInterfaceResources.OFFLINE_BUTTON_TEXT);
        offlineModeButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        offlineModeButton.addActionListener((ActionListener) -> {
            GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(LoginView.this);
            SwingUtilities.invokeLater(() -> {
                parent.setVisible(true);
            });

            Player p = Player.createHumanPlayer("PLAYER", "");
            List<Player> players = new LinkedList<>();
            players.add(p);

            GameController controller = new GameController(players, p, "", false);
            GameView view = controller.getGameView();
            parent.setContentPane(view);
        });


        EmailField emailArea = new EmailField();
        emailArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        PasswordField passwordArea = new PasswordField();
        passwordArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);


        loginButton.addActionListener((ActionListener) -> {
            try {
                // TODO : Implement a password verifying process
            } catch (Exception e) {
                // TODO : Handle exception error showing
            }

        });

        createAccountButton.addActionListener((ActionListener) -> {
            try {
                // TODO : Implement user registration process
            } catch (Exception e) {
                // TODO : Handle exception error showing
            }
        });

        JPanel offlineModeContainer = new JPanel();
        offlineModeContainer.add(offlineModeButton);
        offlineModeContainer.setPreferredSize(
                new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4)
        );

        loginAndSignUpButtonContainer.add(loginButton);
        loginAndSignUpButtonContainer.add(signInButton);

        createAccountAndComeBackToLoginContainer.add(comeBackToLoginButton);
        createAccountAndComeBackToLoginContainer.add(createAccountButton);


        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLabel);
        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/7));
        loginComponentContainer.add(emailArea);
        loginComponentContainer.add(errorLabel);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/25));
        loginComponentContainer.add(passwordArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/7));
        loginComponentContainer.add(loginAndSignUpButtonContainer);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));
        loginComponentContainer.add(offlineModeContainer);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(loginComponentContainer,gbc);
    }
    public void fromLoginMenuToSignInMenu(){
        titleLabel.setText("SIGN IN");
        loginComponentContainer.remove(loginAndSignUpButtonContainer);
        loginComponentContainer.add(createAccountAndComeBackToLoginContainer);
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }

    public void fromSignInMenuToLoginMenu(){
        titleLabel.setText("LOGIN");
        loginComponentContainer.remove(createAccountAndComeBackToLoginContainer);
        loginComponentContainer.add(loginAndSignUpButtonContainer);
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }
}
