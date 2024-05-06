package view.game;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatScrollBarUI;
import javax.swing.JScrollPane;
import model.game.Corporation;
import model.game.Player;
import model.tools.AutoSetter;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;
import view.window.GameFrame;
import javax.swing.JScrollBar;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;


/**
 * @author Arthur Deck
 */
@AutoSetter(typeParam = GameView.class)
public class PausePane extends BlurPane {
    public PausePane(GameView gv) {
        this.g = gv;
        this.player = g.getPlayer();
        mainColor = Color.decode("#f97316");

        setLayout(new MigLayout("align center, aligny center, flowy"));
        new Thread(()-> {
            try {
                Thread.sleep(100);
                init(gv);
                getJFrame().addKeyListener(new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (!keyPressed) {
                            pause();
                            keyPressed = true;
                        }  
                    }
        
                    @Override
                    public void keyReleased(KeyEvent e) {
                        keyPressed = false;
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {}
                });
                init2();
                revalidate();
                repaint();
            } catch (InterruptedException e2) {
                GameFrame.showError(e2, this::repaint);
            }
        }).start();
    }

    final GameView g;
    final Player player;
    final Color mainColor;
    TextField jtf;
    boolean keyPressed;
    HorizontalBarChart barChart1; // For player's actions
    JPanel chatPane;
    JScrollBar scrollBar;
    ArrayList<Player> maskedPlayers = new ArrayList<Player>();

    private void init2() {
        class HBC extends HorizontalBarChart {
            HBC() {
                super();
                super.valuesFormat = new DecimalFormat();
            }
        }

        barChart1 = new HBC();
        barChart1.setBarColor(mainColor);
        barChart1.setDataset(createData());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel1.add(barChart1);
        panel1.setBorder(new ColorableArcableFlatBorder((java.awt.Color)mainColor,5));
        add(panel1, "gapy 10%");

        chatPane = new JPanel();
        chatPane.setLayout(new MigLayout("wrap"));
        chatPane.setOpaque(false);
        JScrollPane js = new JScrollPane(chatPane);
        scrollBar = js.getVerticalScrollBar();
        scrollBar.setBackground(new Color(0,0,0,0));
        class FSBUI extends FlatScrollBarUI {
            @Override
            protected Color getThumbColor(JComponent c, boolean hover, boolean pressed) {
                return mainColor;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
        }
        scrollBar.setUI(new FSBUI());
        js.setBorder(BorderFactory.createEmptyBorder());
        js.setOpaque(false);
        js.getViewport().setOpaque(false);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(js, "growx, h 40%");

        jtf = new TextField("Enter your message");
        jtf.setLabelColor(mainColor);
        jtf.setLineColor(mainColor);
        jtf.setOpaque(false);
        jtf.addActionListener((e) -> {
            String msg = jtf.getText();
            jtf.setText("");
            getJFrame().requestFocus();
            if (!msg.isBlank()) 
                sendChat(msg);
        });
        jtf.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE)
                    getJFrame().requestFocus();
            }
            @Override
            public void keyReleased(KeyEvent arg0) {}
            @Override
            public void keyTyped(KeyEvent arg0) {}
        });
        add(jtf, "growx");

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(mainColor);
        exitButton.addActionListener((e) -> {
            g.pause();
            g.getController().exitGame();
        });
        add(exitButton, "x 93%, y 1%, gap 10");

        MouseAdapter ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                getJFrame().requestFocus();
            }
        };
        addMouseListener(ml);
        chatPane.addMouseListener(ml);
        barChart1.addMouseListener(ml);

        recieveChat(null, "Welcome on the on online chat. Please be respectfull and courtoie. Good Game !");
        sendChat("connected");
    }

    public void recieveChat(Player p, String msg) {
        JTextPane jt = new JTextPane();
        jt.setOpaque(false);
        jt.setEditable(false);
        jt.setVisible(true);
        jt.setContentType("text/html");

        // FIXME : descendre dans le if apres les test
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    class JMI extends JMenuItem {
                        JMI(String s, ActionListener a) {
                            super(s);
                            addActionListener(a);
                        }
                    }

                    JPopupMenu popupMenu = new JPopupMenu();
                    popupMenu.add(new JMI("Mask message", (f)->{
                        jt.setText("<html><body><font color='"+String.format("#%06x", mainColor.darker().getRGB() & 0xFFFFFF)+"'>"+
                        p.getPseudo()+" : </font>*********</body></html>");
                        chatPane.revalidate();
                    }));
                    popupMenu.add(new JMI("Mask player", (f)->maskedPlayers.add(p)));
                    popupMenu.show(jt, e.getX(), e.getY());
                }
            }
        });

        if (p == null)
            jt.setText(msg);
        else {
            String c, psd;
            if (p.equals(player)) {
                c = String.format("#%06x", mainColor.getRGB() & 0xFFFFFF);
                psd = "You";
            } else {
                c = String.format("#%06x", mainColor.darker().getRGB() & 0xFFFFFF);
                psd = p.getPseudo();
                if (maskedPlayers.contains(p))
                    msg = "*********";

                // FIXME : uncoment after test
                // jt.addMouseListener(new MouseAdapter() {
                //     @Override
                //     public void mousePressed(MouseEvent e) {
                //         if (e.getButton() == MouseEvent.BUTTON1) {
                //             JPopupMenu popupMenu = new JPopupMenu();
                //             popupMenu.add(new JMenuItem("Item 1"));
                //             popupMenu.add(new JMenuItem("Item 2"));
                //             popupMenu.show(jt, e.getX(), e.getY());
                //         }
                //     }
                // });
            }
            jt.setText("<html><body><font color='"+c+"'>"+psd+" : </font>"+msg+"</body></html>");
        }
        
        chatPane.add(jt,"w 95%");
        chatPane.revalidate();
    }

    private void sendChat(String msg) {
        recieveChat(player, msg);
        g.getController().sendChat(msg, player);
    }

    private DefaultPieDataset<String> createData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Map<Corporation, Integer> stocks = player.getEarnedStocks();
        for (Corporation c : stocks.keySet()) 
            dataset.addValue(c.toString(), stocks.get(c));
        return dataset;
    }

    public void pause() {
        if (!isBlur()) {
            g.setEnabled(false);
            barChart1.setDataset(createData());
            blur(true);
        } else {
            blur(false);
            g.setEnabled(true);
        }
    }

    @Override
    public void repaint() {
        if (scrollBar != null)
            scrollBar.setValue(scrollBar.getMaximum());

        if (g != null && g.getController().isBanChat(player))
            jtf.setVisible(false);
            
        super.repaint();
    }

    @Override
    public JFrame getJFrame() {
        return GameFrame.currentFrame;
    }
}
