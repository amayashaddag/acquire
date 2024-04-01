package app.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.raven.event.EventItem;
import com.raven.form.FormHome;
import com.raven.model.ModelItem;
import com.raven.swing.Background;
import com.raven.swing.MainPanel;
import com.raven.swing.win_button.WinButton;
import com.raven.form.FormHome;

import control.menu.MenuController;
import view.assets.GameResources;
import view.frame.GameFrame;
import view.game.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.formdev.flatlaf.FlatDarculaLaf;

import control.game.GameController;
import model.game.Player;

import javax.swing.*;

public class Debug extends JFrame {

    public static void main(String[] args) {
//        FlatDarculaLaf.setup();
//
//        ArrayList<Player> l = new ArrayList<>();
//        l.add(Player.createHumanPlayer("Max"));
//        l.add(Player.createHumanPlayer("Xi"));
//        l.add(Player.createHumanPlayer("Best"));
//        l.add(Player.createHumanPlayer("Of"));
//        GameController c = new GameController(l, l.get(0));
//        c.getGameView().setVisible(true);
//
//        GameView g = c.getGameView();
        GameFrame frame = GameFrame.currentFrame;
//        frame.add(new view.menu.PrettyMenuView());
//        MenuView mv = new MenuView();
//        mv.setOn(frame);

//
        new MenuController().start();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        /*

//        c.handleCellPlacing(new tools.Point(0,0), l.get(0));
        HashMap<Corporation, Integer> map = new HashMap<>();
        map.put(Corporation.CONTINENTAL, 1);
        map.put(Corporation.WORLDWIDE, 2);
        map.put(Corporation.IMPERIAL, 12);


        g.getCorporationChoice(new ArrayList<>(map.keySet()));
        System.out.println("finif 1");

        g.chooseSellingKeepingOrTradingStocks(map, Corporation.AMERICAN);
        System.out.println("finif 2");

        g.chooseStocksToBuy(map);
        System.out.println("finif 3");
        */


//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Debug().setVisible(true);
//            }
//        });

    }
}
