package view.login;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import view.Form;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;

public class SignInView extends Form {
    public void setOn(GameFrame g) {
        this.setSize(GameFrame.DEFAULT_WIDTH, GameFrame.DEFAULT_HEIGHT);
        g.setContentPane(this);
    }
    public SignInView() {
        Form signInComponentContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH*2/5,GameFrame.DEFAULT_HEIGHT));
                g.setContentPane(this);
            }
        };
        Form loginAndSignInButtonContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setSize(GameFrame.DEFAULT_WIDTH*2/5,GameFrame.DEFAULT_HEIGHT/5);
            }
        };
        JLabel titleLable = new JLabel("SIGN IN");
        signInComponentContainer.setLayout(new BoxLayout(signInComponentContainer, BoxLayout.Y_AXIS));

        FlatButton loginButton = new FlatButton();
        loginButton.setText(InterfaceLoginMessages.LOGIN_BUTTON_TEXT);


        FlatButton createAccountButton = new FlatButton();
        createAccountButton.setText("CREATE ACCOUNT");

        FlatTextArea idArea = new FlatTextArea();
        idArea.setText(InterfaceLoginMessages.ID_TEXT_AREA);

        FlatTextArea passwordArea = new FlatTextArea();
        passwordArea.setText(InterfaceLoginMessages.PASSWORD_TEXT_AREA);

        loginAndSignInButtonContainer.add(loginButton);
        loginAndSignInButtonContainer.add(createAccountButton);
        signInComponentContainer.add(Box.createVerticalStrut(150));
        signInComponentContainer.add(titleLable);
        signInComponentContainer.add(Box.createVerticalStrut(150));
        signInComponentContainer.add(idArea);
        signInComponentContainer.add(Box.createVerticalStrut(150));
        signInComponentContainer.add(passwordArea);
        signInComponentContainer.add(Box.createVerticalStrut(150));
        signInComponentContainer.add(loginAndSignInButtonContainer);
        this.add(signInComponentContainer);
    }
}
