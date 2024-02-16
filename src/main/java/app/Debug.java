package app;

import frame.GameFrame;
import game.ColorableButtonBorder;
import tools.Point;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.ui.*;

import java.awt.event.*;

import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import com.formdev.flatlaf.util.DerivedColor;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.util.Map;

import javax.swing.plaf.basic.BasicBorders;

public class Debug {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();

        JFrame g = new JFrame();
        g.setTitle("Acquire");
        g.setSize(1000, 600);
        g.setLocationRelativeTo(null);
        g.setDefaultCloseOperation(3);
        g.setResizable(false);

        JPanel jp = new JPanel();
        jp.add(new DeckButton(null));
        g.add(jp);

        g.setVisible(true);
        g.repaint();
    }

    public static class DeckButton extends JButton implements MouseListener {

        DeckButton(Point p) {
            super();
            this.setText("Click");
            this.addActionListener((e) -> System.out.println("caca"));
            this.addMouseListener(this);
            System.out.println("Border : " + getBorder());
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            Borduree zda = new Borduree();
            this.setBorder(new ColorableButtonBorder(new Color(2, 200, 46)));
        }

        public void mouseExited(MouseEvent e) {
            this.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    public static class Borduree extends FlatBorder {
        @Override
        protected Color getOutlineColor(Component c) {
            return Color.GREEN;
        }    
    }
}
