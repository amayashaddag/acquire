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
    GameFrame frameContainer;
    @Override
    public void setOn(GameFrame g) {
        this.setSize(GameFrame.DEFAULT_WIDTH, GameFrame.DEFAULT_HEIGHT);
        g.setContentPane(this);
    }
    public LoginView(GameFrame frame) {
        frameContainer = frame;
        Form loginComponentContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                g.setContentPane(this);
            }
        };
        Form loginAndSignInButtonContainer = new Form() {
            @Override
            public void setOn(GameFrame g) {
                this.setSize(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4);
            }
        };

        loginComponentContainer.setLayout(new BoxLayout(loginComponentContainer, BoxLayout.Y_AXIS));
        loginComponentContainer.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/3,GameFrame.DEFAULT_HEIGHT*3/5));


        JLabel titleLable = new JLabel(InterfaceLoginMessages.LOGIN_BUTTON_TEXT);
        titleLable.setAlignmentX(CENTER_ALIGNMENT);

        FlatButton loginButton = new FlatButton();
        loginButton.setText(InterfaceLoginMessages.LOGIN_BUTTON_TEXT);


        FlatButton signInButton = new FlatButton();
        signInButton.setText(InterfaceLoginMessages.SIGN_IN_BUTTON_TEXT);
        signInButton.addActionListener(ActionListener->{frameContainer.setForm(new SignInView());});


        FlatTextArea idArea = new FlatTextArea();
        idArea.setText(InterfaceLoginMessages.ID_TEXT_AREA);

        FlatTextArea passwordArea = new FlatTextArea();
        passwordArea.setText(InterfaceLoginMessages.PASSWORD_TEXT_AREA);

        loginAndSignInButtonContainer.add(loginButton);
        loginAndSignInButtonContainer.add(signInButton);


        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLable);
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
}
