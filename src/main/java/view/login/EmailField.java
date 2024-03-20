package view.login;

import com.formdev.flatlaf.extras.components.FlatTextField;
import view.assets.LoginInterfaceResources;

public class EmailField extends FlatTextField {
    public EmailField() {
        super();
        this.setPlaceholderText(LoginInterfaceResources.EMAIL_PLACEHOLDER_TEXT);
    }
}
