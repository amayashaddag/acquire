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

    public static final String PASSWORD_SHOWING_IMAGE_PATH = "src/main/ressources/images/login/eye.png";

    public static Image PASSWORD_SHOWING_ICON = new ImageIcon(PASSWORD_SHOWING_IMAGE_PATH)
            .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

}
