package view.login;
import view.assets.LoginInterfaceResources;
import view.game.TextField;

public class EmailField extends TextField {
    public EmailField() {
        super();
        this.setLabelText(LoginInterfaceResources.EMAIL_PLACEHOLDER_TEXT);
    }
}
