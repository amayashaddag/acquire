package view.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.formdev.flatlaf.extras.components.FlatButton;

import control.auth.AlreadyRegisteredUserException;
import control.auth.AuthController;
import control.auth.NotExistingUserException;
import control.auth.NotStrongEnoughPasswordException;
import control.auth.TooLongPasswordException;
import control.auth.WrongPasswordException;
import control.menu.MenuController;
import view.assets.Fonts;
import view.assets.LoginInterfaceResources;
import view.game.TextField;
import view.window.Form;
import view.window.GameFrame;

public class LoginView extends JPanel {

    private final JLabel errorLabel;
    private final JLabel titleLabel;

    private final JPanel offlineModeContainer;
    private final JPanel loginComponentContainer;
    private final JPanel signUpButtons;
    private final JPanel loginButtons;


    private final TextField pseudoArea;
    private final PasswordField passwordArea;
    private final TextField emailArea;
    private final Border originalBorder;

    private final MenuController menuController;

    public LoginView(MenuController menuController, Color c) {
        super();
        this.setOpaque(false);

        this.menuController = menuController;

        loginComponentContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                g.setContentPane(this);
            }
        };

        loginComponentContainer.setLayout(new BoxLayout(loginComponentContainer, BoxLayout.Y_AXIS));
        loginComponentContainer
                .setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH / 3, GameFrame.DEFAULT_HEIGHT * 3 / 5));

        titleLabel = new JLabel(LoginInterfaceResources.LOGIN_BUTTON_TEXT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(Fonts.TITLE_FONT);

        ImageIcon icon = new ImageIcon("src/main/ressources/images/menu/point-dexclamation.png");
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        JPanel errorLabelContainer = new JPanel();
        errorLabelContainer.setPreferredSize(new Dimension(400, 36));
        errorLabelContainer.setLayout(new GridBagLayout());
        GridBagConstraints errorLabelContainerContraints = new GridBagConstraints();
        errorLabelContainerContraints.gridx = 0;
        errorLabelContainerContraints.gridy = 0;
        errorLabelContainerContraints.insets = new Insets(1, 1, 1, 1);
        errorLabelContainerContraints.gridwidth = 400;
        errorLabel = new JLabel(LoginInterfaceResources.ALREADY_REGISTERED_USER_MESSAGE, new ImageIcon(newImage),
                JLabel.RIGHT);
        errorLabel.setForeground(new Color(209, 38, 24));
        errorLabel.setAlignmentX(RIGHT_ALIGNMENT);
        errorLabel.setPreferredSize(new Dimension(400, 36));
        errorLabel.setVisible(false);
        errorLabelContainer.add(errorLabel, errorLabelContainerContraints);
        errorLabelContainer.setOpaque(false);

        FlatButton loginButton = new FlatButton();
        loginButton.setText(LoginInterfaceResources.LOGIN_BUTTON_TEXT);
        loginButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        loginButton.addActionListener((ActionListener) -> loginActionListener());

        FlatButton signInButton = new FlatButton();
        signInButton.setText(LoginInterfaceResources.SIGN_UP_BUTTON_TEXT);
        signInButton.addActionListener((ActionListener) -> fromLoginMenuToSignInMenu());
        signInButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        FlatButton createAccountButton = new FlatButton();
        createAccountButton.setText(LoginInterfaceResources.CREATE_ACCOUNT_BUTTON_TEXT);
        createAccountButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        createAccountButton.addActionListener((ActionListener) -> signUPActionListener());

        FlatButton comeBackToLoginButton = new FlatButton();
        comeBackToLoginButton.setText(LoginInterfaceResources.GO_TO_LOGIN_PAGE_BUTTON_TEXT);
        comeBackToLoginButton.addActionListener((ActionListener) -> fromSignInMenuToLoginMenu());
        comeBackToLoginButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        emailArea = new TextField(LoginInterfaceResources.EMAIL_PLACEHOLDER_TEXT, c);
        emailArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        emailArea.setPreferredSize(new Dimension(400, 36));

        originalBorder = emailArea.getBorder();

        pseudoArea = new TextField(LoginInterfaceResources.PSEUDO_PLACEHOLDER_TEXT, c);
        pseudoArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        pseudoArea.setVisible(false);
        pseudoArea.setPreferredSize(new Dimension(400, 36));

        passwordArea = new PasswordField();
        passwordArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        passwordArea.setPreferredSize(new Dimension(400, 36));

        offlineModeContainer = new JPanel();
        offlineModeContainer.setPreferredSize(
                new Dimension(GameFrame.DEFAULT_WIDTH / 5, GameFrame.DEFAULT_HEIGHT / 4));
        offlineModeContainer.setOpaque(false);

        JPanel loginAndSignUpButtonContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH / 5, GameFrame.DEFAULT_HEIGHT / 4));
            }
        };
        loginAndSignUpButtonContainer.add(loginButton);
        loginAndSignUpButtonContainer.add(signInButton);
        loginAndSignUpButtonContainer.setOpaque(false);

        JPanel createAccountAndComeBackToLoginContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH / 5, GameFrame.DEFAULT_HEIGHT / 4));
            }
        };
        createAccountAndComeBackToLoginContainer.add(comeBackToLoginButton);
        createAccountAndComeBackToLoginContainer.add(createAccountButton);
        createAccountAndComeBackToLoginContainer.setOpaque(false);

        loginButtons = new JPanel();
        loginButtons.add(loginAndSignUpButtonContainer);
        loginButtons.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 15));
        loginButtons.add(offlineModeContainer);
        loginButtons.setOpaque(false);

        signUpButtons = new JPanel();
        signUpButtons.add(createAccountAndComeBackToLoginContainer);
        signUpButtons.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 15));
        signUpButtons.setOpaque(false);

        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLabel);
        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 25));
        loginComponentContainer.add(pseudoArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 25));
        loginComponentContainer.add(emailArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 25));
        loginComponentContainer.add((passwordArea));
        loginComponentContainer.add((errorLabelContainer));
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT / 15));
        loginComponentContainer.add(loginButtons);
        loginComponentContainer.add(Box.createVerticalGlue());
        loginComponentContainer.setOpaque(false);
        this.add(loginComponentContainer);
    }

    public void fromLoginMenuToSignInMenu() {
        titleLabel.setText("SIGN UP");
        loginButtons.remove(offlineModeContainer);
        signUpButtons.add(offlineModeContainer);
        loginComponentContainer.remove(loginButtons);
        loginComponentContainer.add(signUpButtons);
        pseudoArea.setVisible(true);
        resetPlaceHolder();
        hideError();
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }

    public void fromSignInMenuToLoginMenu() {
        titleLabel.setText("LOGIN");
        signUpButtons.remove(offlineModeContainer);
        loginButtons.add(offlineModeContainer);
        loginComponentContainer.remove(signUpButtons);
        loginComponentContainer.add(loginButtons);
        pseudoArea.setVisible(false);
        resetPlaceHolder();
        hideError();
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }

    public void resetPlaceHolder() {
        pseudoArea.setText("");
        emailArea.setText("");
        passwordArea.setText("");
    }

    public void printError(String message) {
        errorLabel.setText(message);
        emailArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(209, 38, 24)));
        passwordArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(209, 38, 24)));
        pseudoArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(209, 38, 24)));
        errorLabel.setVisible(true);
        loginComponentContainer.repaint();
    }

    public void signUPActionListener() {
        try {
            if (pseudoArea.getText().isEmpty() || emailArea.getText().isEmpty()
                    || charArrayToString(passwordArea.getPassword()).isEmpty()) {
                printError(LoginInterfaceResources.EMPTY_FIELD);
                return;
            }
            String res = AuthController.signUpWithEmailAndPassword(emailArea.getText(), pseudoArea.getText(),
                    charArrayToString(passwordArea.getPassword()));
            setSession(res);

            menuController.getView().multiPlayer();
        } catch (AlreadyRegisteredUserException e) {
            printError(LoginInterfaceResources.ALREADY_REGISTERED_USER_MESSAGE);
        } catch (NotStrongEnoughPasswordException e) {
            printError(LoginInterfaceResources.NOT_STRONG_ENOUGH_PASSWORD_MESSAGE);
        } catch (TooLongPasswordException e) {
            printError(LoginInterfaceResources.TOO_LONG_PASSWORD_MESSAGE);
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    public void loginActionListener() {
        try {
            if (emailArea.getText().isEmpty() || charArrayToString(passwordArea.getPassword()).isEmpty()) {
                printError(LoginInterfaceResources.EMPTY_FIELD);
                return;
            }
            String res = AuthController.loginWithEmailAndPassword(emailArea.getText(),
                    charArrayToString(passwordArea.getPassword()));
            setSession(res);

            menuController.getView().multiPlayer();
        } catch (NotExistingUserException e) {
            printError(LoginInterfaceResources.NOT_EXISTING_USER_MESSAGE);
        } catch (WrongPasswordException e) {
            printError(LoginInterfaceResources.WRONG_PASSWORD_MESSAGE);
        } catch (Exception e) {
            errorInterrupt(e);
        }
    }

    private void setSession(String uid) throws Exception {
        menuController.setSession(uid);
    }

    public static String charArrayToString(char[] c) {
        return String.copyValueOf(c);
    }

    public void hideError() {
        passwordArea.setBorder(originalBorder);
        pseudoArea.setBorder(originalBorder);
        emailArea.setBorder(originalBorder);
        errorLabel.setVisible(false);
        loginComponentContainer.repaint();
    }

    private void errorInterrupt(Exception e) {
        GameFrame.showError(e, () -> {
            System.out.println(e.getMessage());
            GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(this);
            parent.dispose();
        });
    }
}
