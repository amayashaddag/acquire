package view.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatClientProperties;
import control.menu.MenuController;
import model.tools.PlayerAnalytics;
import model.tools.PreGameAnalytics;
import net.miginfocom.swing.MigLayout;
import view.assets.Fonts;
import view.assets.MenuRessources;
import view.game.ColorableArcableFlatBorder;
import view.login.LoginView;
import view.window.Form;
import view.window.GameFrame;

/**
 * The beggining menu of the Game
 *
 * @author Arthur Deck
 * @version 1
 */
public class PrettyMenuView extends Form {
    private final MenuController controller;
    private final Menu3D menu3d = new Menu3D();
    private final JPanel panel;
    private final MigLayout mig = new MigLayout("al center, filly");
    private boolean aMultiGameIsLaunching = false;
    private boolean haveJoinAGame = false;
    private int numberOfPlayerByGame = 6;

    public PrettyMenuView(MenuController controller) {
        super();

        this.controller = controller;
        setLayout(mig);

        UIManager.put("Button.background", MenuRessources.Assets.getColor("blue"));
        UIManager.put("Label.font", view.assets.Fonts.REGULAR_PARAGRAPH_FONT);

        this.panel = new JPanel() {
            @Override
            public void setOpaque(boolean isOpaque) {
                if (isOpaque)
                    setBorder(new ColorableArcableFlatBorder(MenuRessources.Assets.getColor("blue").darker(), 10));
                else
                    setBorder(null);
        
                super.setOpaque(isOpaque);
            }
        };
        panel.setBackground(MenuRessources.Assets.getColor("blue"));

        menu3d.setFont(Fonts.BOLD_PARAGRAPH_FONT);
        menu3d.addMenuItem("SINGLE PLAYER", this::singlePlayer);
        menu3d.addMenuItem("MULTI PLAYER", this::multiPlayer);
        menu3d.addMenuItem("PROFIL", this::profile);
        menu3d.addMenuItem("RANKING", this::ranking);
        menu3d.addMenuItem("SETTING", this::settings);
        menu3d.addMenuItem("EXIT", this::exit);
        menu3d.addGlobalEvent(controller::abortMutiGame);
        menu3d.addGlobalEvent(() -> panel.setVisible(false));

        panel.setVisible(false);
        panel.setOpaque(false);

        add(menu3d, "x 10%, y 40%, w 25%, h 50%");
        add(panel, "x 60%, y 40%, w 30%, h 50%");
        repaint();
    }

    public boolean haveJoinAGame() {
        return haveJoinAGame;
    }

    public void setHaveJoinAGame(boolean b) {
        haveJoinAGame = b;
    }

    public int getNumberOfPlayerByGame() {
        return numberOfPlayerByGame;
    }

    public boolean aMultiGameIsLaunching() {
        return aMultiGameIsLaunching;
    }

    public void setAMultiGameIsLaunching(boolean b) {
        aMultiGameIsLaunching = b;
    }

    public void singlePlayer() {
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

    public void multiPlayer() {
        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }

        mig.setComponentConstraints(panel, "x 60%, y 40%, w 30%, h 50%");
        revalidate();
        panel.removeAll();
        panel.setLayout(new MigLayout("al center, fill, insets 0, wrap"));

        JPanel scrollPane = new JPanel();
        scrollPane.setLayout(new MigLayout("center x, insets 5"));
        panel.add(scrollPane);
        String btnContraints = "w 70%, h 5%, wrap";

        JButton createGameBtn = new JButton();
        if (aMultiGameIsLaunching && haveJoinAGame) {
            aMultiGameIsLaunching = false;
            haveJoinAGame = false;
            controller.abortMutiGame();
            multiPlayer();
        } else if (aMultiGameIsLaunching) {
            createGameBtn.setText("Abort game");
            createGameBtn.setBackground(Color.RED);
            createGameBtn.addActionListener((e) -> {
                controller.abortMutiGame();
                aMultiGameIsLaunching = false;
                multiPlayer();
            });

            JButton startBtn = new JButton("Start Game");
            startBtn.setBackground(Color.GREEN);
            startBtn.addActionListener((e) -> {
                controller.launchMultiGame();
            });
            scrollPane.add(startBtn, btnContraints);
        } else if (haveJoinAGame) {
            JButton startBtn = new JButton("Quit queue");
            startBtn.setBackground(Color.GREEN);
            startBtn.addActionListener((e) -> {
                controller.quitGame();
                haveJoinAGame = false;
            });
            scrollPane.add(startBtn, btnContraints);
        } else {
            createGameBtn.setText("Create new game");
            createGameBtn.setBackground(Color.GREEN);
            createGameBtn.addActionListener((e) -> {
                controller.createMultiGame(numberOfPlayerByGame);
                aMultiGameIsLaunching = true;
                multiPlayer();
            });
        }
        scrollPane.add(createGameBtn, btnContraints);

        List<PreGameAnalytics> list = controller.getAvailableGames();
        if (list != null && !list.isEmpty()) {
            for (PreGameAnalytics p : list) {
                JButton btn = new JButton();
                btn.setText(p.hostName() + " : " + p.currentNumberOfPlayer()
                        + " / " + p.maxNumberOfPlayer());
                btn.addActionListener((ActionListener) -> {
                    controller.joinPreGame(p);
                });
                scrollPane.add(btn, btnContraints);
                if (aMultiGameIsLaunching || haveJoinAGame)
                    btn.setEnabled(false);
            }
        }

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinner.addChangeListener((e) -> numberOfPlayerByGame = (int) spinner.getValue());
        JPanel spinnerPane = new JPanel();
        spinnerPane.add(new JLabel("Player by game : "));
        spinnerPane.add(spinner);
        scrollPane.add(spinnerPane, btnContraints);

        JScrollPane scroll = new JScrollPane(scrollPane);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, "grow");
        scroll.setOpaque(false);
        scrollPane.setOpaque(false);
        panel.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        panel.repaint();
        panel.setVisible(true);
        repaint();
    }

    public void ranking() {
        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }

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
        List<PlayerAnalytics> playerRanking;
        try {
            playerRanking = controller.getRanking();
        } catch (Exception e) {
            playerRanking = new LinkedList<>();
            GameFrame.showError(e, () -> {
                GameFrame parent = (GameFrame) SwingUtilities.getWindowAncestor(this);
                parent.dispose();
            });
        }

        for (PlayerAnalytics pa : playerRanking) {
            i++;
            model.addRow(new Object[] { i, pa.pseudo(), pa.bestScore(), null });
        }

        panel.add(table);
        panel.add(scroll);
        panel.setVisible(true);
        repaint();
    }

    public void profile() {
        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }

        mig.setComponentConstraints(panel, "x 60%, y 40%, w 20%, h 30%");
        panel.removeAll();

        PlayerAnalytics p = controller.getPlayerAnalytics();
        panel.setLayout(new MigLayout("al center, insets 10, wrap 1"));
        panel.add(new JLabel("Pseudo : " + p.pseudo()));
        panel.add(new JLabel("Won Games" + p.wonGames()));
        panel.add(new JLabel("Played Games" + p.playedGames()));
        JButton jb = new JButton("Change account");
        jb.setBackground(MenuRessources.Assets.getColor("blue").darker());
        jb.addActionListener((e) -> {
            panel.removeAll();
            panel.add(new LoginView(controller));
            repaint();
        });
        panel.add(jb);

        panel.setOpaque(true);
        panel.setVisible(true);
        panel.revalidate();
        revalidate();
        repaint();
    }

    public void settings() {
        // TODO : pour les test 
        // mig.setComponentConstraints(panel, "x 0, y 0, w 100%, h 0%");
        // panel.removeAll();
        // panel.add(new view.game.EndGame());
        // panel.repaint();
        // panel.setVisible(true);
        // remove(menu3d);
        // repaint();
        view.game.BlurPane bb = new view.game.BlurPane(this);
        JButton jv = new JButton("Stop");
        jv.addActionListener((e) -> bb.blur(false));
        bb.blurWith(jv);
        // bb.add(new JLabel("test 1,2,1,2"));
        // bb.repaint();
        // ((GameFrame) SwingUtilities.getWindowAncestor(this)).setGlassPane(new view.game.BlurPane());
        // ((GameFrame) SwingUtilities.getWindowAncestor(this)).getGlassPane().setVisible(true);
    }

    public void exit() {
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
        g.drawImage(MenuRessources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setOn(GameFrame g) {
        g.getContentPane().removeAll();
        g.add(this);
        g.repaint();
        g.revalidate();
    }

    private void displayLoginView() {
        mig.setComponentConstraints(panel, "x 50%, y 40%, w 45%, h 50%");
        revalidate();
        panel.removeAll();
        panel.setOpaque(false);
        LoginView lv = new LoginView(controller);
        lv.setOpaque(false);
        panel.add(lv);
        panel.setVisible(true);
        repaint();
    }
}
