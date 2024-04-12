package view.login;

import com.formdev.flatlaf.extras.components.FlatTextField;
import view.assets.LoginInterfaceResources;

public class PseudoField extends FlatTextField {
    public PseudoField(){
        super();
        this.setPlaceholderText(LoginInterfaceResources.PSEUDO_PLACEHOLDER_TEXT);
    }
}
