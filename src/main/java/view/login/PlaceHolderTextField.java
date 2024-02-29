package view.login;

import com.formdev.flatlaf.extras.components.FlatTextArea;
import com.formdev.flatlaf.extras.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PlaceHolderTextField extends FlatPasswordField implements FocusListener {
    private String text;

    private boolean alreadyWritten = false;

    public PlaceHolderTextField(String text){
        this.text=text;
        this.addFocusListener(this);
        this.setForeground(Color.GRAY);


        this.setLayout(new BorderLayout());
        JButton jb = new JButton();
        jb.addActionListener((e) -> {
            if (getEchoChar() == '\u2022')
                PlaceHolderTextField.this.setEchoChar((char) 0);
            else
                PlaceHolderTextField.this.setEchoChar('\u2022');
        });
        jb.setFocusPainted(false);
        jb.setBorderPainted(false);
        jb.setContentAreaFilled(false);
        jb.setOpaque(false);
        this.add(jb, BorderLayout.EAST);

        ImageIcon ico = new ImageIcon("src/main/ressources/login/oeil.png");
        Image img = ico.getImage();
        Image rsz = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        jb.setIcon(new ImageIcon(rsz));
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
