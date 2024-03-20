package app.launcher;

import com.raven.event.EventItem;
import com.raven.model.ModelItem;
import com.raven.swing.Background;
import com.raven.swing.MainPanel;
import com.raven.swing.win_button.WinButton;
import com.raven.form.FormHome;

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
        /* FlatDarculaLaf.setup();

        ArrayList<Player> l = new ArrayList<>();
        l.add(Player.createHumanPlayer("Max"));
        l.add(Player.createHumanPlayer("Xi"));
        l.add(Player.createHumanPlayer("Best"));
        l.add(Player.createHumanPlayer("Of"));
        GameController c = new GameController(l, l.get(0));
        c.getGameView().setVisible(true);

        GameView g = c.getGameView();
        GameFrame frame = new GameFrame();

        g.setOn(frame);
        SwingUtilities.invokeLater(() -> frame.setVisible(true)); */
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

    private FormHome home;
    private Point animatePoint;
    private ModelItem itemSelected;

    private MainPanel mainPanel;
    private WinButton winButton;
    private Background background1;
    private JPanel header;

    Debug() {
        initComponents();
        setBackground(new Color(0, 0, 0, 0));
        home = new FormHome();
        winButton.initEvent(this, background1);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(home);
        testData();

    }

    private Point getLocationOf(Component com) {
        Point p = home.getPanelItemLocation();
        int x = p.x;
        int y = p.y;
        int itemX = com.getX();
        int itemY = com.getY();
        int left = 10;
        int top = 35;
        return new Point(x + itemX + left, y + itemY + top);
    }

    private void testData() {
        home.setEvent(new EventItem() {
            @Override
            public void itemClick(Component com, ModelItem item) {
                home.setSelected(com);
                home.showItem(item);
                System.out.println("Voici l'item "+ item.getItemName());
            }
        });
        int ID = 1;
        for (int i = 0; i <= 5; i++) {
            int targetWidth = 100;
            int targetHeight = 100;
            BufferedImage resizedImage = new BufferedImage(
                    targetWidth,
                    targetHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(GameResources.Assets.BLUE_TOWER_CELL, 0, 0, targetWidth, targetHeight, null);
            g.dispose();
            home.addItem(new ModelItem(ID++, "ItemName", "Descrption", 160, "Adidas", new ImageIcon(resizedImage)));
        }
    }

    private void initComponents() {

        background1 = new com.raven.swing.Background();
        header = new javax.swing.JPanel();
        winButton = new com.raven.swing.win_button.WinButton();
        mainPanel = new com.raven.swing.MainPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        header.setOpaque(false);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                                .addContainerGap(1101, Short.MAX_VALUE)
                                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
        headerLayout.setVerticalGroup(
                headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headerLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(winButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(82, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 517, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout background1Layout = new javax.swing.GroupLayout(background1);
        background1.setLayout(background1Layout);
        background1Layout.setHorizontalGroup(
                background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(background1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        background1Layout.setVerticalGroup(
                background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(background1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }
}
