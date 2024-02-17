package app;

import frame.GameFrame;
import game.ColorableFlatBorder;
import game.PlayerItem;
import net.miginfocom.swing.MigLayout;
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
        MigLayout mig = new MigLayout("al center, filly", "10[]10");
        jp.setLayout(mig);
        jp.add(new PlayerItem(mig));
        jp.add(new PlayerItem(mig));
        jp.add(new PlayerItem(mig));
        jp.add(new PlayerItem(mig));

        g.add(jp);
        g.setVisible(true);
    }
}
