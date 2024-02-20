package view.login;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatCheckBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatFormattedTextFieldUI;
import view.Form;
import view.GameFrame;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends Form {

    JLabel titleLabel;

    Form loginComponentContainer;
    Form loginAndSignInButtonContainer;
    Form createAccountAndComeBackToLoginContainer;


    @Override
    public void setOn(GameFrame g) {
        this.setSize(GameFrame.DEFAULT_WIDTH, GameFrame.DEFAULT_HEIGHT);
        g.setContentPane(this);
    }
    public LoginView() {

        loginComponentContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                g.setContentPane(this);
            }
        };
        loginAndSignInButtonContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4));
            }
        };

        createAccountAndComeBackToLoginContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4));
            }
        };

        loginComponentContainer.setLayout(new BoxLayout(loginComponentContainer, BoxLayout.Y_AXIS));
        loginComponentContainer.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/3,GameFrame.DEFAULT_HEIGHT*3/5));


        titleLabel = new JLabel(InterfaceLoginMessages.LOGIN_BUTTON_TEXT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        FlatButton loginButton = new FlatButton();
        loginButton.setText(InterfaceLoginMessages.LOGIN_BUTTON_TEXT);


        FlatButton signInButton = new FlatButton();
        signInButton.setText(InterfaceLoginMessages.SIGN_IN_BUTTON_TEXT);
        signInButton.addActionListener(ActionListener->{fromLoginMenuToSignInMenu();});

        FlatButton createAccountButton = new FlatButton();
        createAccountButton.setText("CREATE ACCOUNT");

        FlatButton comeBackToLoginButton = new FlatButton();
        comeBackToLoginButton.setText("LOGIN");
        comeBackToLoginButton.addActionListener((ActionListener) ->{fromSignInMenuToLoginMenu();});


        PlaceHolderTextArea idArea = new PlaceHolderTextArea(InterfaceLoginMessages.ID_TEXT_AREA);
        idArea.setText(InterfaceLoginMessages.ID_TEXT_AREA);


        PlaceHolderTextArea passwordArea = new PlaceHolderTextArea(InterfaceLoginMessages.PASSWORD_TEXT_AREA);
        passwordArea.setText(InterfaceLoginMessages.PASSWORD_TEXT_AREA);


        loginAndSignInButtonContainer.add(loginButton);
        loginAndSignInButtonContainer.add(signInButton);

        createAccountAndComeBackToLoginContainer.add(comeBackToLoginButton);
        createAccountAndComeBackToLoginContainer.add(createAccountButton);


        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLabel);
        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/8));
        loginComponentContainer.add(idArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/25));
        loginComponentContainer.add(passwordArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/8));
        loginComponentContainer.add(loginAndSignInButtonContainer);
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
        loginComponentContainer.remove(loginAndSignInButtonContainer);
        loginComponentContainer.add(createAccountAndComeBackToLoginContainer);
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }

    public void fromSignInMenuToLoginMenu(){
        titleLabel.setText("LOGIN");
        loginComponentContainer.remove(createAccountAndComeBackToLoginContainer);
        loginComponentContainer.add(loginAndSignInButtonContainer);
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }
}
