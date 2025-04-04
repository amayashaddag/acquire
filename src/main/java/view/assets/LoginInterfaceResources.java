package view.assets;

import javax.swing.*;
import java.awt.*;

public class LoginInterfaceResources {
    public static final String LOGIN_BUTTON_TEXT = "LOGIN";
    public static final String SIGN_UP_BUTTON_TEXT = "GO TO SIGN UP PAGE";
    public static final String GO_TO_LOGIN_PAGE_BUTTON_TEXT = "GO TO LOGIN PAGE";

    public static final String CREATE_ACCOUNT_BUTTON_TEXT = "SIGN UP";
    public static final String EMAIL_PLACEHOLDER_TEXT = "ENTER EMAIL ADDRESS";
    public static final String PASSWORD_PLACEHOLDER_TEXT = "ENTER PASSWORD";

    public static final String OFFLINE_BUTTON_TEXT = "PLAY OFFLINE";

    public static final String EMPTY_FIELD = "You havn't fill all the fields";
    public static final String PASSWORD_SHOWING_IMAGE_PATH = "src/main/ressources/images/login/eye.png";

    public static final String NOT_EXISTING_USER_MESSAGE = "Couldn't found your account";

    public static final String ALREADY_REGISTERED_USER_MESSAGE = "This account already exists";

    public static final String WRONG_PASSWORD_MESSAGE = "Your password/email is wrong";

    public static final String TOO_LONG_PASSWORD_MESSAGE = "Your password is too long (max 20 characters)";

    public static final String NOT_STRONG_ENOUGH_PASSWORD_MESSAGE = "To week password";

    public static final String PSEUDO_PLACEHOLDER_TEXT = "ENTER PSEUDO";

    public static Image PASSWORD_SHOWING_ICON = new ImageIcon(PASSWORD_SHOWING_IMAGE_PATH)
            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

}
