package view.login;

import com.formdev.flatlaf.extras.components.FlatPasswordField;

import view.assets.LoginInterfaceResources;

@Deprecated
public class PasswordField extends FlatPasswordField {

    public PasswordField() {
        super();
        this.setPlaceholderText(LoginInterfaceResources.PASSWORD_PLACEHOLDER_TEXT);
    }
}
