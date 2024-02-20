package login;

import com.formdev.flatlaf.extras.components.FlatTextArea;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceHolderTextArea extends FlatTextArea implements FocusListener {
    public String text;
    public PlaceHolderTextArea(String text){
        this.text=text;
        this.addFocusListener(this);
    }
    @Override
    public void focusGained(FocusEvent e){
        this.setText("");
    }
    @Override
    public void focusLost(FocusEvent e){
        this.setText(text);
    }
}
