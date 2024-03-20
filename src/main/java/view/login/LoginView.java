package view.login;

import view.assets.Fonts;
import com.formdev.flatlaf.extras.components.FlatButton;
import javax.swing.*;
import java.awt.*;

import view.assets.LoginInterfaceResources;
import view.frame.Form;
import view.frame.GameFrame;

public class LoginView extends JPanel {

    private final JLabel titleLabel;

    private final JPanel loginComponentContainer;
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

            } catch (Exception e) {
                // TODO : Handle exception error showing
            }
        });

        loginAndSignUpButtonContainer.add(loginButton);
        loginAndSignUpButtonContainer.add(signInButton);

        createAccountAndComeBackToLoginContainer.add(comeBackToLoginButton);
        createAccountAndComeBackToLoginContainer.add(createAccountButton);


        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLabel);
        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/7));
        loginComponentContainer.add(emailArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/25));
        loginComponentContainer.add(passwordArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/7));
        loginComponentContainer.add(loginAndSignUpButtonContainer);
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
