package view.menu;

import control.menu.MenuController;
import model.tools.PlayerAnalytics;
import javax.swing.table.DefaultTableModel;
import view.frame.*;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import view.assets.Fonts;
import view.assets.MenuRessources;
import control.game.GameController;
import model.game.Player;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatClientProperties;


/**
 * The beggining menu of the Game
 *
 * @author Arthur Deck
 * @version 1
 */
public class PrettyMenuView extends Form {
    private final MenuController controller;
    private final Menu3D menu3d = new Menu3D();
    private final JPanel panel = new JPanel();
    private final MigLayout mig = new MigLayout("al center, filly");

    public PrettyMenuView(MenuController controller) {
        super();
        this.controller = controller;
        setLayout(mig);
        panel.setBorder(new view.game.ColorableArcableFlatBorder(Color.GREEN, 15));
        menu3d.setFont(Fonts.REGULAR_PARAGRAPH_FONT);

        menu3d.addMenuItem("SinglePlayer", this::singlePlayer);
        menu3d.addMenuItem("MultiPlayer", this::multiPlayer);
        menu3d.addMenuItem("Profile", this::profile);
        menu3d.addMenuItem("Ranking", this::ranking);
        menu3d.addMenuItem("Setting", this::settings);
        menu3d.addMenuItem("Exit", this::exit);

        panel.setVisible(false);

        add(menu3d,"x 15%, y 30%, w 25%, h 50%");
        add(panel, "x 60%, w 30%, h 50%");
        repaint();
    }

    private void singlePlayer() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // TODO :fixIt
                ArrayList<Player> l = new ArrayList<>();
                l.add(Player.createHumanPlayer("Max"));
                l.add(Player.createHumanPlayer("Xi"));
                l.add(Player.createHumanPlayer("Best"));
                l.add(Player.createHumanPlayer("Of"));
                GameController c = new GameController(l, l.get(0));
                view.frame.GameFrame.currentFrame.setForm(c.getGameView());
            }
        }).start();
    }

    private void multiPlayer() {

    }

    private void ranking() {
        panel.removeAll();
        panel.setVisible(true);

        javax.swing.JTable table = new javax.swing.JTable();
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();

        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{},
            new String[]{"No", "Name", "Best Score", "Region"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        scroll.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(10);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
        }

        table.setDefaultRenderer(Object.class, new TableGradientCell());
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background");
        scroll.putClientProperty(FlatClientProperties.STYLE, "border:3,0,3,0,$Table.background,10,10");
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "hoverTrackColor:null");
        table.setDefaultRenderer(Object.class, new TableGradientCell());
        DefaultTableModel model=(DefaultTableModel) table.getModel();
        model.addRow(new Object[]{1, "John Smith", "de", "123 Main St, City", "Manager"});

        int i = 0;
        for (PlayerAnalytics pa : controller.getRanking()) {
            i++;
            model.addRow(new Object[]{i, pa.pseudo(), pa.bestScore(), pa.region()});
        }

        panel.add(table);
        panel.add(scroll);
        repaint();
    }

    private void profile() {
        mig.setComponentConstraints(panel, "x 50%, w 45%, h 70%");
        revalidate();
        panel.removeAll();
        panel.add(new view.login.LoginView());
        panel.setVisible(true);
        repaint();
    }

    private void settings() {

    }

    private void exit() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }).start();
    }

    @Override
    public void show() {
        GameFrame.currentFrame.add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawImage(MenuRessources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setOn(GameFrame g) {
        g.getContentPane().removeAll();
        g.add(this);
        g.repaint();
        g.revalidate();
    }
}