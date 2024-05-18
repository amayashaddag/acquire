package view.login;

import view.assets.LoginInterfaceResources;
import view.game.TextField;

@Deprecated
public class PseudoField extends TextField {
    public PseudoField(){
        super();
        this.setLabelText(LoginInterfaceResources.PSEUDO_PLACEHOLDER_TEXT);
    }
}
