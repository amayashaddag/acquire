package view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
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
import view.login.PrettyLoginView;
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
    private int numberOfSimulation = 100;
    private Color mainLeftColor;
    private Animator animator;

    public MenuView(MenuController controller) {
        super();

        this.controller = controller;
        setLayout(mig);

        mainLeftColor = MenuResources.getColor("purple");
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

            @Override
            protected void paintComponent(Graphics g) {
                if (isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(this.getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
            }
        };
        panel.setBackground(mainLeftColor);
        panel.setBorder(new ColorableArcableFlatBorder(mainLeftColor.darker(),15));
        panel.setVisible(false);

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
        revalidate();
        repaint();

        if (controller.isConnected()) 
            GameFrame.showSuccessNotification("You're connected as " + 
            controller.getPlayerCredentials().pseudo() + " !") ;
        else GameFrame.showErrorNotification(
                "You're not connected !"
            );
    }

    private void changeUi() {
        UIManager.put("control", Color.RED);
        UIManager.put("Button.background", mainLeftColor.darker());
        UIManager.put("Button.disabledBackground", mainLeftColor.darker());
        UIManager.put("Label.background", mainLeftColor);
        UIManager.put("Label.font", Fonts.REGULAR_PARAGRAPH_FONT);
        UIManager.put("Spinner.buttonBackground", mainLeftColor);
        UIManager.put("Button.font", Fonts.BOLD_PARAGRAPH_FONT);
        UIManager.put("Spinner.font", Fonts.REGULAR_PARAGRAPH_FONT);
        UIManager.put("Table.font", Fonts.REGULAR_PARAGRAPH_FONT);
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
        UIManager.put("Button.font", Fonts.REGULAR_FONT_PATH);
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
        updatePanelPourcent(this::singlePlayerWork, 
            0.55, 0.45, 0.20, 0.25);
    }

    private void singlePlayerWork() {
        panel.setLayout(new MigLayout("align x, fill, insets 0"));

        String btnContraints = "w 70%, h 5%, wrap";

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        JPanel spinnerPane = new JPanel();
        spinnerPane.add(new JLabel("Player by game : "));
        spinnerPane.add(spinner);
        spinnerPane.setBackground(mainLeftColor.darker());
        spinner.setBackground(mainLeftColor);

        Consumer<Consumer<Integer>> f = (g) -> {
            Object o = spinner.getValue();
            if (o instanceof Integer)
                g.accept((int)o);
            else spinner.setValue(numberOfPlayerByGame);
        };

        JButton ezBtn = new JButton("Easy");
        ezBtn.setBackground(MenuResources.getColor("green"));
        ezBtn.addActionListener((e) -> f.accept(controller::startSingleGameEasy));
        JButton medBtn = new JButton("Medium");
        medBtn.setBackground(MenuResources.getColor("orange"));
        medBtn.addActionListener((e) -> f.accept(controller::startSingleGameEasy));
        JButton hardBtn = new JButton("Hard");
        hardBtn.setBackground(MenuResources.getColor("red").darker());
        hardBtn.addActionListener((e) -> f.accept(controller::startSingleGameEasy));

        panel.add(ezBtn, "x 13%, y 5%, gapy 2%,"+btnContraints);
        panel.add(medBtn, "x 13%, gapy 2%,"+btnContraints);
        panel.add(hardBtn, "x 13%, gapy 2%,"+btnContraints);
        panel.add(spinnerPane, "center x, y 70%, gapy 5%," + btnContraints);
        panel.setOpaque(true);
    }

    public void spectator() {
        updatePanelPourcent(this::spectatorWork, 
            0.55, 0.5, 0.3, 0.20);
    }

    private void spectatorWork() {
        BiConsumer<JSpinner,Consumer<Integer>> f = (s,g) -> {
            Object o = s.getValue();
            if (o instanceof Integer)
                g.accept((int)o);
            else s.setValue(numberOfPlayerByGame);
        };

        JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinner1.addChangeListener((e) -> f.accept(spinner1, (i)-> numberOfPlayerByGame=i));
        JPanel spinnerPane1 = new JPanel();
        spinnerPane1.add(new JLabel("Player by game : "));
        spinnerPane1.add(spinner1);
        spinnerPane1.setBackground(mainLeftColor.darker());
        spinner1.setBackground(mainLeftColor);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 5));
        spinner2.addChangeListener((e) -> f.accept(spinner1, (i)-> numberOfSimulation=i));
        JPanel spinnerPane2 = new JPanel();
        spinnerPane2.add(new JLabel("Simulation's deep : "));
        spinnerPane2.add(spinner2);
        spinnerPane2.setBackground(mainLeftColor.darker());
        spinner2.setBackground(mainLeftColor);

        JButton btn = new JButton("Start");
        btn.setBackground(MenuResources.getColor("green"));
        btn.addActionListener((e) -> controller.startSpectatorGame(numberOfPlayerByGame, numberOfSimulation));
        
        String btnContraints = "w 70%, h 5%, wrap";
        panel.setLayout(new MigLayout("align x, fill, insets 0"));
        panel.add(spinnerPane1, "center x, gapy 2%," + btnContraints);
        panel.add(spinnerPane2, "center x, gapy 2%," + btnContraints);
        panel.add(btn, "center x, gapy 2%," + btnContraints);
        panel.setOpaque(true);
    }

    public void multiPlayer() {
        if (!controller.isConnected()) {
            displayLoginView(this::multiPlayer);
            return;
        }
        updatePanelPourcent(this::multiPlayerWork, 
            0.55, 0.40, 0.30, 0.50);
    }

    private void multiPlayerWork() {
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
            updateMultiPlayer();
        } else if (aMultiGameIsLaunching) {
            createGameBtn.setText("Abort game");
            createGameBtn.setBackground(MenuResources.getColor("red"));
            createGameBtn.addActionListener((e) -> {
                controller.abortMutiGame();
                aMultiGameIsLaunching = false;
                updateMultiPlayer(); 
            });

            JButton startBtn = new JButton("Start Game");
            startBtn.setBackground(MenuResources.getColor("green"));
            startBtn.addActionListener((e) -> {
                controller.launchMultiGame();
            });
            panel.add(startBtn, "x 13%, y 5%, gapy 1%,"+btnContraints);
        } else if (haveJoinAGame) {
            JButton startBtn = new JButton("Quit queue");
            startBtn.setBackground(MenuResources.getColor("red"));
            startBtn.addActionListener((e) -> {
                controller.quitGame();
                haveJoinAGame = false;
                updateMultiPlayer();
            });
            panel.add(startBtn, btnContraints);
        } else {
            createGameBtn.setText("Create new game");
            createGameBtn.setBackground(MenuResources.getColor("green"));
            createGameBtn.addActionListener((e) -> {
                controller.createMultiGame(numberOfPlayerByGame);
                aMultiGameIsLaunching = true;
                updateMultiPlayer();
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

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinner.addChangeListener((e) -> {
            Object o = spinner.getValue();
            if (o instanceof Integer)
                numberOfPlayerByGame = (int)o;
        });
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

    public void updateMultiPlayer() {
        if (panel.isVisible()) {
            int index = menu3d.getPressedIndex();
            if (index != -1 && menu3d.getItems().get(index).getText().equals("MULTI PLAYER")) {
                panel.removeAll();
                multiPlayerWork();
                revalidate();
                repaint();
            }
        }
    }

    public void ranking() {
        if (!controller.isConnected()) {
            displayLoginView(this::ranking);
            return;
        }
        updatePanelPourcent(this::rankingWork,
            0.55, 0.45, 0.30, 0.30);
    }

    private void rankingWork() {
        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setBackground(mainLeftColor);

        table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
                new String[] { "No", "Name", "Best Score", "Region" }) {
            boolean[] canEdit = new boolean[] { false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(10);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row % 3 == 0) 
                    renderer.setBackground(mainLeftColor.brighter());
                else if (row % 3 == 1)
                    renderer.setBackground(mainLeftColor);
                else
                    renderer.setBackground(mainLeftColor.darker());
                
                return renderer;
            }
        });
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background");
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
        scroll.setOpaque(false);
        table.setOpaque(false);
        panel.setOpaque(false);
    }

    public void profile() {
        if (!controller.isConnected()) {
            displayLoginView(this::profile);
            return;
        }
        updatePanelPourcent(this::profileWork, 
            0.55, 0.47, 0.2, 0.3);
    }

    private void profileWork() {
        class HBC extends HorizontalBarChart {
            HBC() {
                super();
                super.valuesFormat = new DecimalFormat();
                setBackground(mainLeftColor);
            }
        }

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
        jb.addActionListener((e) -> {
            displayLoginView(this::profile);
        });
        panel.add(jb, "x 25%, gapy 10");

        panel.setOpaque(true);
    }

    public void exit() {
        hidePanel();
        new Thread(() -> {
            try {
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }).start();
    }

    private void displayLoginView(Runnable r) {  
        updatePanelPourcent(() -> {
            panel.setLayout(new MigLayout("align x, fill, insets 0"));
            mig.setComponentConstraints(panel, "x 55%, y 40%, w 30%, h 40%");
            panel.revalidate();
            PrettyLoginView lv = new PrettyLoginView(controller, mainLeftColor.darker(), r);
            lv.setSize(panel.getSize());
            panel.add(lv);
            panel.setBackground(mainLeftColor);
            panel.setOpaque(true);
        }, 0.55, 0.40, 0.30, 0.40);
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
                mig.setComponentConstraints(panel, "x "+getWidth()+100+",y " + y);
                panel.setVisible(false);
                revalidate();
            }
        });
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.2f);
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

            @Override
            public void end() {
                mig.setComponentConstraints(panel, "x " +x +",y " + y +",w " + w +",h " + h);
                panel.setVisible(true);
                revalidate();
            }
        });
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.2f);
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
            panel.removeAll();
            panel.setSize(w,h);
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
        GameFrame.setForm(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(MenuResources.MImage.BACKGROUND, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setOn(GameFrame g) {
        g.setContentPane(this);
        g.revalidate();
        g.repaint();
    }

    @Override
    public void repaint() {
        if (panel != null)
            panel.repaint();
        super.repaint();
    }
}
