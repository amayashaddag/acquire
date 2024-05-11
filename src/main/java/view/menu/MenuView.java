package view.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import com.formdev.flatlaf.FlatClientProperties;
import control.menu.MenuController;
import model.tools.PlayerAnalytics;
import model.tools.PreGameAnalytics;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;
import view.assets.Fonts;
import view.assets.MenuResources;
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
public class MenuView extends Form {
    private final static int ANIMATION_TIME = 600;

    private final MenuController controller;
    private final Menu3D menu3d = new Menu3D();
    private final JPanel panel;
    private final MigLayout mig = new MigLayout("al center, filly");
    private boolean aMultiGameIsLaunching = false;
    private boolean haveJoinAGame = false;
    private int numberOfPlayerByGame = 6;
    private Color mainLeftColor;
    private Animator animator;

    public MenuView(MenuController controller) {
        super();

        this.controller = controller;
        setLayout(mig);

        mainLeftColor = MenuResources.Assets.getColor("purple");
        saveUI();
        changeUi();

        this.panel = new JPanel() {
            @Override
            public void setOpaque(boolean isOpaque) {
                if (isOpaque)
                    setBorder(new ColorableArcableFlatBorder(mainLeftColor.darker(), 10));
                else
                    setBorder(null);
        
                super.setOpaque(isOpaque);
            }
        };
        panel.setBackground(mainLeftColor);
        panel.setBorder(new ColorableArcableFlatBorder(mainLeftColor.darker(),15));

        menu3d.setFont(Fonts.BOLD_PARAGRAPH_FONT);
        menu3d.addMenuItem("SINGLE PLAYER", this::singlePlayer);
        menu3d.addMenuItem("MULTI PLAYER", this::multiPlayer);
        menu3d.addMenuItem("PROFIL", this::profile);
        menu3d.addMenuItem("RANKING", this::ranking);
        menu3d.addMenuItem("SPECTATOR", this::spectator);
        menu3d.addMenuItem("EXIT", this::exit);
        menu3d.addGlobalEvent(controller::abortMutiGame);

        panel.setVisible(false);
        panel.setOpaque(false);

        add(menu3d, "x 10%, y 40%, w 25%, h 50%");
        add(panel);
        repaint();

        if (controller.isConnected()) 
            GameFrame.showSuccessNotification("You're connected as " + 
            controller.getPlayerCredentials().pseudo() + " !") ;
        else GameFrame.showErrorNotification(
                "You're not connected !"
            );
    }

    private void changeUi() {
        UIManager.put("Button.background", mainLeftColor.darker());
        UIManager.put("Button.disabledBackground", mainLeftColor.darker());
        UIManager.put("Label.background", mainLeftColor);
        UIManager.put("Label.font", view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
    }

    private Color Button_background;
    private Color Label_background;
    private Color Button_disabledBackground;
    private void saveUI() {
        Button_background = (Color) UIManager.get("Button.background", null);
        Label_background = (Color) UIManager.get("Label.background", null);
        Button_disabledBackground = (Color) UIManager.get("Button.disabledBackground", null);
    }

    public void undoUI() {
        UIManager.put("Button.background", Button_background);
        UIManager.put("Label.background", Label_background);
        UIManager.put("Button.disabledBackground", Button_disabledBackground);
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
                undoUI();
                controller.startSingleGame();
            }
        }).start();
    }

    public void spectator() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                undoUI();
                controller.startSpectatorGame();
            }
        }).start();
    }

    public void multiPlayer() {
        updatePanelPourcent(this::multiPlayerWork, 
            0.6, 0.40, 0.30, 0.50);
    }

    private void multiPlayerWork() {
        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }

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
            createGameBtn.setBackground(MenuResources.Assets.getColor("red"));
            createGameBtn.addActionListener((e) -> {
                controller.abortMutiGame();
                aMultiGameIsLaunching = false;
                multiPlayer();
            });

            JButton startBtn = new JButton("Start Game");
            startBtn.setBackground(MenuResources.Assets.getColor("green"));
            startBtn.addActionListener((e) -> {
                controller.launchMultiGame();
            });
            panel.add(startBtn, "x 13%, y 5%, gapy 1%,"+btnContraints);
        } else if (haveJoinAGame) {
            JButton startBtn = new JButton("Quit queue");
            startBtn.setBackground(MenuResources.Assets.getColor("red"));
            startBtn.addActionListener((e) -> {
                controller.quitGame();
                haveJoinAGame = false;
            });
            panel.add(startBtn, btnContraints);
        } else {
            createGameBtn.setText("Create new game");
            createGameBtn.setBackground(MenuResources.Assets.getColor("green"));
            createGameBtn.addActionListener((e) -> {
                controller.createMultiGame(numberOfPlayerByGame);
                aMultiGameIsLaunching = true;
                multiPlayer();
            });
        }
        panel.add(createGameBtn, "x 13%, gapy 5%," + btnContraints);

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

        JScrollPane scroll = new JScrollPane(scrollPane);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setBackground(mainLeftColor);
        panel.add(scroll, "h 75%, w 100%, wrap");

        // FIXME : changer couleur fleches JSPinner
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinner.addChangeListener((e) -> numberOfPlayerByGame = (int) spinner.getValue());
        JPanel spinnerPane = new JPanel();
        spinnerPane.add(new JLabel("Player by game : "));
        spinnerPane.add(spinner);
        spinnerPane.setBackground(mainLeftColor.darker());
        spinner.setBackground(mainLeftColor);
        panel.add(spinnerPane, "x 13%, y 85%, gapy 5%," + btnContraints);

        scroll.setOpaque(false);
        scrollPane.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.setOpaque(true);
    }

    private void ranking() {
        updatePanelPourcent(this::rankingWork,
            0.6, 0.3, 0.3, 0.5);
    }

    private void rankingWork() {
        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }
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
    }

    private void profile() {
        updatePanelPourcent(this::profileWork, 
            0.6, 0.4, 0.2, 0.3);
    }

    private void profileWork() {
        class HBC extends HorizontalBarChart {
            HBC() {
                super();
                super.valuesFormat = new DecimalFormat();
                setBackground(mainLeftColor);
            }
        }

        if (!controller.isConnected()) {
            displayLoginView();
            return;
        }

        panel.removeAll();

        PlayerAnalytics p = controller.getPlayerAnalytics();
        panel.setLayout(new MigLayout("al center, filly, insets 10, wrap 1"));
        panel.add(new JLabel("Pseudo : " + p.pseudo()), "x 25%, gapy 10");
        HBC hbc = new HBC();
        hbc.setBarColor(mainLeftColor.darker());
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.addValue("Won Games", p.wonGames());
        dataset.addValue("Played Games", p.playedGames());
        dataset.addValue("Lost Games", p.lostGames());
        hbc.setDataset(dataset);
        panel.add(hbc, "gapy 10");
        JButton jb = new JButton("Change account");
        jb.setBackground(MenuResources.Assets.getColor("blue").darker());
        jb.addActionListener((e) -> {
            displayLoginView();
        });
        panel.add(jb, "x 25%, gapy 10");

        panel.setOpaque(true);
    }

    public void exit() {
        hidePanel();
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

    private void displayLoginView() {   // FIXME : à refaire
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

    private void hidePanel() {
        if (!panel.isVisible()) return;
        
        int x = panel.getX();
        int y = panel.getY();
        int deltaX = getWidth() + 100 - panel.getX();
        animator = new Animator(ANIMATION_TIME, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                int newX = x + (int) (deltaX * fraction);
                panel.setLocation(newX, y);
                repaint();
            }

            @Override
            public void end() {
                panel.setVisible(false);
            }
        });
        animator.setAcceleration(0.1f);
        animator.setDeceleration(0.1f);
        animator.start();
    }

    private void showPanel(int x, int y, int w, int h) {
        if (panel.isVisible()) return;

        int startX = getWidth() + 100;
        int deltaX = x - startX;
        animator = new Animator(ANIMATION_TIME, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                int newX = startX + (int) (deltaX * fraction);
                panel.setLocation(newX, y);
                repaint();
            }

            @Override
            public void begin() {
                mig.setComponentConstraints(panel, "x " + startX +",y " + y +",w " + w +",h " + h);
                panel.setVisible(true);
                revalidate();
            }
        });
        animator.setAcceleration(0.1f);
        animator.setDeceleration(0.1f);
        animator.start();
    }

    /**
     * Smooth hide the Panel. Do your work and smooth show the Panel
     * @param work must do all the update you want in your panel
     */
    private void updatePanel(Runnable work, int x, int y, int w, int h) {
        if (panel.isVisible()) {
            hidePanel();
            if (animator != null)
                animator.addTarget(new TimingTargetAdapter() {
                    @Override
                    public void end() {
                        updatePanel(work, x, y, w, h);
                    }
                });
        } else {
            work.run();
            showPanel(x, y, w, h);
        }
    }

    /**
     * Same as updatePane but coordinate are in % like on a miglayout.
     * For exemple "x 10%, y 20%, w 15%, h 80%" -> (0.1, 0.2, 0.15, 0.8)
     * @param work
     * @param x
     * @param y
     * @param w
     * @param h
     */
    private void updatePanelPourcent(Runnable work, double x, double y, double w, double h) {
        int width = getWidth();
        int height = getHeight();
        updatePanel(work, 
            (int) (width * x), 
            (int) (height * y), 
            (int) (width * w), 
            (int) (height * h)
        );
    }

    @Override
    public void show() {
        GameFrame.currentFrame.add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(MenuResources.Assets.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setOn(GameFrame g) {
        g.setContentPane(this);
        g.repaint();
        g.revalidate();
    }
}
