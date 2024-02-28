package login;

import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.formdev.flatlaf.extras.components.FlatTextField;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceHolderTextField extends FlatTextField implements FocusListener {
    private String text;

    private boolean alreadyWritten = false;

    public PlaceHolderTextField(String text){
        this.text=text;
        this.addFocusListener(this);
        this.setForeground(Color.GRAY);
    }
    @Override
    public void focusGained(FocusEvent e){
        if (this.text.equals(this.getText()) || this.text.equals(this.getText())){
            System.out.println("Je suis rentré ici");
            this.setText("");
            setForeground(Color.WHITE);
        }
    }
    @Override
    public void focusLost(FocusEvent e){
        if (this.getText().isEmpty()){
            this.setText(text);
            setForeground(Color.GRAY);
        }
    }
}
