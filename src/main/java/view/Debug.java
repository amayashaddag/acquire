package view;

import net.miginfocom.swing.MigLayout;
import tools.Point;
import view.game.ColorableFlatBorder;
import view.game.GameView;
import view.game.GrowingJLabel;
import view.game.annotations.AutoSetter;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.ui.*;

import java.awt.event.*;

import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import com.formdev.flatlaf.util.DerivedColor;
import com.formdev.flatlaf.util.UIScale;

import java.util.Map;

import javax.swing.plaf.basic.BasicBorders;

@AutoSetter
public class Debug {
    public Debug(GameView g) {
        
    }
    public static void main(String[] args) {
        // FlatDarculaLaf.setup();

        // JFrame g = new JFrame();
        // g.setTitle("Acquire");
        // g.setSize(1000, 600);
        // g.setLocationRelativeTo(null);
        // g.setDefaultCloseOperation(3);

        // JPanel jp = new JPanel();
        // MigLayout mig = new MigLayout("al center, filly", "10[]10");
        // jp.setLayout(mig);
        // jp.add(new PlayerItem(mig), "w 100, h 100");
        // jp.add(new PlayerItem(mig), "w 100, h 100");
        // jp.add(new PlayerItem(mig), "w 100, h 100");

        // JPanel root = new JPanel();
        // root.setLayout(new BorderLayout());
        // root.add(jp, BorderLayout.NORTH);

        // g.add(root);
        // g.setVisible(true);

        GameFrame f = new GameFrame();
        f.setVisible(true);
    }
}
