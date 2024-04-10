package view.menu;

import control.menu.MenuController;
import model.tools.PreGameAnalytics;
import model.tools.PlayerAnalytics;
import javax.swing.table.DefaultTableModel;
import view.frame.*;
import java.awt.*;
import java.security.Policy;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import view.assets.Fonts;
import view.assets.MenuRessources;
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
    private boolean aMultiGameIsLaunching = false;

    public PrettyMenuView(MenuController controller) {
        super();
        this.controller = controller;
        setLayout(mig);
        menu3d.setFont(new Font("Bambino", Font.BOLD, 16));

        menu3d.addMenuItem("SINGLE PLAYER", this::singlePlayer);
        menu3d.addMenuItem("MULTI PLAYER", this::multiPlayer);
        menu3d.addMenuItem("PROFIL", this::profile);
        menu3d.addMenuItem("RANKING", this::ranking);
        menu3d.addMenuItem("SETTING", this::settings);
        menu3d.addMenuItem("EXIT", this::exit);

        panel.setVisible(false);
        panel.setOpaque(false);

        add(menu3d, "x 36%, y 55%, w 25%, h 50%");
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
                controller.startSingleGame();
            }
        }).start();
    }

    private void multiPlayer() {
        mig.setComponentConstraints(panel, "x 60%, w 30%, h 50%");
        revalidate();
        panel.removeAll();

        JButton createGameBtn = new JButton();
        if (aMultiGameIsLaunching) {
            createGameBtn.setText("Avort game");
            createGameBtn.setBackground(Color.RED);
            createGameBtn.addActionListener((e) -> {
                controller.avortMutiGame();
                aMultiGameIsLaunching = false;
                multiPlayer();
            });

        } else {
            createGameBtn.setText("Create new game");
            createGameBtn.setBackground(Color.GREEN);
            createGameBtn.addActionListener((e) -> {
                controller.createMultiGame();
                aMultiGameIsLaunching = true;
                multiPlayer();
            });
        }
        panel.add(createGameBtn);

        List<PreGameAnalytics> list = controller.getAvailableGames();
        if (list != null && !list.isEmpty()) {
            for (PreGameAnalytics p : list) {
                JButton btn = new JButton();
                btn.setText(p.hostName() + " : " + p.currentNumberOfPlayer()
                        + " / " + p.maxNumberOfPlayer());

                // TODO :ajouter action au btn pour rejoindre la game

                panel.add(btn);
            }
        }

        panel.repaint();
        panel.setVisible(true);
        repaint();
    }

    private void ranking() {
        mig.setComponentConstraints(panel, "x 60%, w 30%, h 50%");
        revalidate();
        panel.removeAll();

        javax.swing.JTable table = new javax.swing.JTable();
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();

        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
                new String[] { "No", "Name", "Best Score", "Region" }) {
            boolean[] canEdit = new boolean[] { false, false, false, false, false };

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
        scroll.putClientProperty(FlatClientProperties.STYLE,
                "border:3,0,3,0,$Table.background,10,10");
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "hoverTrackColor:null");
        table.setDefaultRenderer(Object.class, new TableGradientCell());
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int i = 0;
        for (PlayerAnalytics pa : controller.getRanking()) {
            i++;
            model.addRow(new Object[] { i, pa.pseudo(), pa.bestScore(), pa.region() });
        }

        panel.add(table);
        panel.add(scroll);
        panel.setVisible(true);
        repaint();
    }

    private void profile() {
        mig.setComponentConstraints(panel, "x 50%, w 45%, h 70%");
        revalidate();
        panel.removeAll();

        if (!controller.isConnected())
            panel.add(new view.login.LoginView());
        else {
            PlayerAnalytics p = controller.getPlayerAnalyticsSession();
            panel.setLayout(new GridLayout(4, 1));
            panel.add(new JLabel("Pseudo : " + p.pseudo()));
            panel.add(new JLabel("Email : " + p.email()));
            panel.add(new JLabel("Won Games" + p.wonGames()));
            panel.add(new JLabel("Played Games" + p.playedGames()));
            JButton jb = new JButton("Change account");
            jb.addActionListener((e) -> {
                panel.removeAll();
                panel.add(new view.login.LoginView());
                repaint();
            });
            panel.add(jb);
        }

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
