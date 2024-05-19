package view.login;
import view.Components.TextField;
import view.assets.LoginInterfaceResources;

@Deprecated
public class EmailField extends TextField {
    public EmailField() {
        super();
        this.setLabelText(LoginInterfaceResources.EMAIL_PLACEHOLDER_TEXT);
    }
}
