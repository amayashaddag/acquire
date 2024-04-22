package view.login;

import control.auth.*;
import control.game.GameController;
import model.game.Player;
import org.checkerframework.checker.units.qual.N;
import view.assets.Fonts;
import view.assets.LoginInterfaceResources;

import com.formdev.flatlaf.extras.components.FlatButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import view.frame.Form;
import view.frame.GameFrame;
import view.game.GameView;

public class LoginView extends JPanel {


    private final JLabel titleLabel;

    PseudoField pseudoArea;

    private final JPanel loginComponentContainer;

    private final JLabel errorLabel;

    JPanel offlineModeContainer;

    JPanel signUpButtons;

    JPanel loginButtons;
    PasswordField passwordArea;

    EmailField emailArea;

    Border originalBorder;

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

        ImageIcon icon = new ImageIcon("src/main/ressources/images/menu/point-dexclamation.png");
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(15,15,Image.SCALE_SMOOTH);
        JPanel errorLabelContainer = new JPanel();
        errorLabelContainer.setPreferredSize(new Dimension(400,36));
        errorLabelContainer.setLayout(new GridBagLayout());
        GridBagConstraints errorLabelContainerContraints = new GridBagConstraints();
        errorLabelContainerContraints.gridx = 0;
        errorLabelContainerContraints.gridy = 0;
        errorLabelContainerContraints.insets= new Insets(1,1,1,1);
        errorLabelContainerContraints.gridwidth = 400;
        errorLabel = new JLabel(LoginInterfaceResources.ALREADY_REGISTERED_USER_MESSAGE,new ImageIcon(newImage),JLabel.RIGHT);
        errorLabel.setForeground(new Color(209,38,24));
        errorLabel.setAlignmentX(RIGHT_ALIGNMENT);
        errorLabel.setPreferredSize(new Dimension(400,36));
        errorLabel.setVisible(false);
        errorLabelContainer.add(errorLabel,errorLabelContainerContraints);


        FlatButton loginButton = new FlatButton();
        loginButton.setText(LoginInterfaceResources.LOGIN_BUTTON_TEXT);
        loginButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        loginButton.addActionListener((ActionListener) -> loginActionListener());


        FlatButton signInButton = new FlatButton();
        signInButton.setText(LoginInterfaceResources.SIGN_UP_BUTTON_TEXT);
        signInButton.addActionListener((ActionListener)-> fromLoginMenuToSignInMenu());
        signInButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        FlatButton createAccountButton = new FlatButton();
        createAccountButton.setText(LoginInterfaceResources.CREATE_ACCOUNT_BUTTON_TEXT);
        createAccountButton.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        createAccountButton.addActionListener((ActionListener) -> signUPActionListener());

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


        emailArea = new EmailField();
        emailArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        emailArea.setPreferredSize(new Dimension(400,36));

        originalBorder = emailArea.getBorder();


        pseudoArea = new PseudoField();
        pseudoArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        pseudoArea.setVisible(false);
        pseudoArea.setPreferredSize(new Dimension(400,36));


        passwordArea = new PasswordField();
        passwordArea.setFont(Fonts.REGULAR_PARAGRAPH_FONT);
        passwordArea.setPreferredSize(new Dimension(400,36));

        offlineModeContainer = new JPanel();
        offlineModeContainer.add(offlineModeButton);
        offlineModeContainer.setPreferredSize(
                new Dimension(GameFrame.DEFAULT_WIDTH/5,GameFrame.DEFAULT_HEIGHT/4)
        );

        loginAndSignUpButtonContainer.add(loginButton);
        loginAndSignUpButtonContainer.add(signInButton);

        createAccountAndComeBackToLoginContainer.add(comeBackToLoginButton);
        createAccountAndComeBackToLoginContainer.add(createAccountButton);

        loginButtons = new JPanel();
        loginButtons.add(loginAndSignUpButtonContainer);
        loginButtons.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));
        loginButtons.add(offlineModeContainer);

        signUpButtons = new JPanel();
        signUpButtons.add(createAccountAndComeBackToLoginContainer);
        signUpButtons.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));

        /*JPanel alignement = new JPanel();
        alignement.setLayout(new GridBagLayout());
        alignement.setPreferredSize(new Dimension(GameFrame.DEFAULT_WIDTH/3,GameFrame.DEFAULT_HEIGHT*3/10));

        GridBagConstraints pseudoContraints = new GridBagConstraints();
        pseudoContraints.gridx = 0;
        pseudoContraints.gridy = 0;
        pseudoContraints.anchor = GridBagConstraints.LINE_START;
        pseudoContraints.insets = new Insets(0,0,0,0);
        pseudoContraints.weighty = 2;
        alignement.add(pseudoArea,pseudoContraints);

        GridBagConstraints emailContraints = new GridBagConstraints();
        emailContraints.gridx = 0;
        emailContraints.gridy = pseudoArea.getHeight() + GameFrame.DEFAULT_HEIGHT/25;
        emailContraints.anchor = GridBagConstraints.LINE_START;
        emailContraints.insets = new Insets(0,0,0,0);
        alignement.add(emailArea,emailContraints);

        GridBagConstraints passwordContraints = new GridBagConstraints();
        passwordContraints.gridx = 0;
        passwordContraints.gridy = emailArea.getY() + emailArea.getHeight() + GameFrame.DEFAULT_HEIGHT/25;
        passwordContraints.anchor = GridBagConstraints.LINE_START;
        passwordContraints.insets = new Insets(0,0,0,0);
        alignement.add(passwordArea,passwordContraints);

        GridBagConstraints errorLabelContraints = new GridBagConstraints();
        errorLabelContraints.gridx = 0;
        errorLabelContraints.gridy = passwordArea.getY() + passwordArea.getHeight();
        errorLabelContraints.anchor = GridBagConstraints.LINE_START;
        errorLabelContraints.insets = new Insets(0,0,0,0);
        alignement.add(errorLabel,errorLabelContraints);*/

        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(titleLabel);
        loginComponentContainer.add(Box.createHorizontalGlue());
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));
        loginComponentContainer.add(pseudoArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/25));
        loginComponentContainer.add(emailArea);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/25));
        loginComponentContainer.add((passwordArea));
        loginComponentContainer.add((errorLabelContainer));
        //loginComponentContainer.add(alignement);
        loginComponentContainer.add(Box.createVerticalStrut(GameFrame.DEFAULT_HEIGHT/15));
        loginComponentContainer.add(loginButtons);
        loginComponentContainer.add(Box.createVerticalGlue());

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(loginComponentContainer,gbc);
    }
    public void fromLoginMenuToSignInMenu(){
        titleLabel.setText("SIGN UP");
        loginButtons.remove(offlineModeContainer);
        signUpButtons.add(offlineModeContainer);
        loginComponentContainer.remove(loginButtons);
        loginComponentContainer.add(signUpButtons);
        pseudoArea.setVisible(true);
        resetPlaceHolder();
        System.out.println("height : "+errorLabel.getHeight()+"\nwidth : "+errorLabel.getWidth());
        hideError();
        loginComponentContainer.revalidate();
        loginComponentContainer.repaint();
    }



    public void fromSignInMenuToLoginMenu(){
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
    public void resetPlaceHolder(){
        pseudoArea.setText("");
        emailArea.setText("");
        passwordArea.setText("");
    }

    public void printError(String message){
        errorLabel.setText(message);
        emailArea.setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(209,38,24)));
        passwordArea.setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(209,38,24)));
        pseudoArea.setBorder(BorderFactory.createMatteBorder(2,2,2,2,new Color(209,38,24)));
        errorLabel.setVisible(true);
        loginComponentContainer.repaint();
    }

    public void signUPActionListener(){
        try{
            if (pseudoArea.getText().isEmpty() || emailArea.getText().isEmpty() || charArrayToString(passwordArea.getPassword()).isEmpty()){
                printError(LoginInterfaceResources.NOT_STRONG_ENOUGH_PASSWORD_MESSAGE);
                return;
            }
            String res = AuthController.signUpWithEmailAndPassword(emailArea.getText(),pseudoArea.getText(),charArrayToString(passwordArea.getPassword()));
        }catch (AlreadyRegisteredUserException e){
            printError(LoginInterfaceResources.ALREADY_REGISTERED_USER_MESSAGE);
            return;
        }catch (NotStrongEnoughPasswordException e){
            printError(LoginInterfaceResources.NOT_STRONG_ENOUGH_PASSWORD_MESSAGE);
            return;
        }catch (Exception e){
            return;
        }
    }

    public void loginActionListener(){
        try {
            String res = AuthController.loginWithEmailAndPassword(emailArea.getText(),charArrayToString(passwordArea.getPassword()));
        }catch (NotExistingUserException e){
            printError(LoginInterfaceResources.NOT_EXISTING_USER_MESSAGE);
            return;
        }catch (WrongPasswordException e){
            printError(LoginInterfaceResources.WRONG_PASSWORD_MESSAGE);
            return;
        }catch (Exception e){
            return;
        }
    }

    public static String charArrayToString(char[] c){
        return "" + c;
    }

    public void hideError(){
        passwordArea.setBorder(originalBorder);
        pseudoArea.setBorder(originalBorder);
        emailArea.setBorder(originalBorder);
        errorLabel.setVisible(false);
        loginComponentContainer.repaint();
    }


}
