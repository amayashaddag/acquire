package view.game;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
                init2();
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
    ArrayList<Player> reportedPlayers = new ArrayList<>();

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
        MsgPane jt = new MsgPane();
        jt.setOpaque(false);
        jt.setEditable(false);
        jt.setVisible(true);
        jt.setContentType("text/html");
        jt.setAuthor(p);

        if (p == null)
            jt.setText(msg);
        else {
            String c, psd;
            if (p.equals(player)) {
                c = String.format("#%06x", mainColor.getRGB() & 0xFFFFFF);
                psd = "You";
            } else {
                jt.setMaskedText("<html><body><font color='"+String.format("#%06x", 
                mainColor.darker().getRGB() & 0xFFFFFF)+"'>"+
                p.getPseudo()+" : </font>*********</body></html>");
                c = String.format("#%06x", mainColor.darker().getRGB() & 0xFFFFFF);
                psd = p.getPseudo();

                boolean isMaskedPlayer = maskedPlayers.contains(p);
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
                            popupMenu.add(new JMI((jt.isMasked() ? "Unmask" : "Mask") + " message", 
                            (f)->{
                                jt.maskUnMask();
                                chatPane.revalidate();
                            }));
        
                            popupMenu.add(new JMI((isMaskedPlayer ? "Unmask" : "Mask") + " player", 
                            (f)->{
                                if (isMaskedPlayer)
                                    maskedPlayers.remove(p);   
                                else 
                                    maskedPlayers.add(p);   
                                
                                for (Component c : chatPane.getComponents())
                                        if (c instanceof MsgPane ) {
                                            MsgPane cp = (MsgPane) c;
                                            if (cp.getAuthor() != null && cp.getAuthor().equals(p))
                                                cp.mask(!isMaskedPlayer);
                                        }  
                                chatPane.revalidate();
                            }));

                            if (!reportedPlayers.contains(p))
                                popupMenu.add(new JMI("Report and mask player", 
                                (f)->{
                                    g.getController().repportPlayer(p, player);
                                    reportedPlayers.add(p);
                                }));
                            
                            popupMenu.show(jt, e.getX(), e.getY());
                        }
                    }
                });

                if (isMaskedPlayer)
                    jt.mask();
            }

            int maxCharByLine = 25; // Anthyconstiutionnellement
            for (String s : msg.split(" ")) {
                int length = s.length();
                if (length > maxCharByLine)   
                    msg = msg.replace(s, formatMsg(s, maxCharByLine));
            }

            jt.setText("<html><body><font color='"+c+"'>"+psd+" : </font>"+msg+"</body></html>");
        }
        chatPane.add(jt,"w 95%");
        chatPane.revalidate();
    }

    private String formatMsg(String s, int maxLenght) {
        int length = s.length();
        if (length < maxLenght)
            return s;
        else 
            return formatMsg(s.substring(0, length/2), maxLenght) + "\n" +
            formatMsg(s.substring(length/2, length), maxLenght);
    }

    private void sendChat(String msg) {
        recieveChat(player, msg);
        
        if (g.getController().isOnlineMode()) { // FIXME : pas propre, trouver une meilleur solution
            g.getController().sendChat(msg, player);
        }
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

    private class MsgPane extends JTextPane {
        boolean masked = false;
        String txt = "";
        String maskedText = "*********";
        Player author;

        Player getAuthor() {
            return author;
        }

        void setAuthor(Player author) {
            this.author = author;
        }

        void mask(boolean b) {
            if (b) mask();
            else unMask();
        }

        void mask() {
            masked = true;
            super.setText(maskedText);
        }

        void unMask() {
            masked = false;
            super.setText(txt);
        }

        void maskUnMask() {
            if (masked)
                unMask();
            else
                mask();
        }

        boolean isMasked() {
            return masked;
        }

        void setMaskedText(String maskedText) {
            this.maskedText = maskedText;
        }

        @Override
        public void setText(String arg0) {
            txt = arg0;
            if (masked)
                super.setText(maskedText);
            else 
                super.setText(arg0);
        }
    }
}
