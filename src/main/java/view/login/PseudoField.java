package view.login;

import view.Components.TextField;
import view.assets.LoginInterfaceResources;

@Deprecated
public class PseudoField extends TextField {
    public PseudoField(){
        super();
        this.setLabelText(LoginInterfaceResources.PSEUDO_PLACEHOLDER_TEXT);
    }
}
