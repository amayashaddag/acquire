package view.login;

import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.formdev.flatlaf.extras.components.FlatTextField;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceHolderTextField extends FlatTextField implements FocusListener {
    public String text;
    private boolean alreadyWritten = false;

    public PlaceHolderTextField(String text){
        this.text=text;
        this.addFocusListener(this);
    }
    @Override
    public void focusGained(FocusEvent e){
        if (this.text == InterfaceLoginMessages.PASSWORD_TEXT_AREA || this.text == InterfaceLoginMessages.ID_TEXT_AREA){
            this.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e){
        if (this.text ==  ""){
            this.setText(text);
        }
    }
}
