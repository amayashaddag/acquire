package view.login;

import com.formdev.flatlaf.extras.components.*;
import view.assets.LoginInterfaceResources;

public class PasswordField extends FlatPasswordField {

    public PasswordField() {
        super();
        this.setPlaceholderText(LoginInterfaceResources.PASSWORD_PLACEHOLDER_TEXT);
    }
}
