package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import model.game.Corporation;
import model.game.Player;
import model.tools.AutoSetter;
import net.miginfocom.swing.MigLayout;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.pie.DefaultPieDataset;
import view.window.GameFrame;

/**
 * @author Arthur Deck
 */
@AutoSetter(typeParam = GameView.class)
public class PausePane extends BlurPane {
    public PausePane(GameView gv) {
        this.g = gv;
        this.player = g.getPlayer();
        setLayout(new MigLayout("al center, filly, wrap"));
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
            } catch (InterruptedException e2) {
                GameFrame.showError(e2, this::repaint);
            }
        }).start();
    }

    final GameView g;
    final Player player;
    boolean keyPressed;
    HorizontalBarChart barChart1; // For player's actions

    private void init2() {
        class HBC extends HorizontalBarChart {
            HBC() {
                super();
                super.valuesFormat = new DecimalFormat();
            }
        }

        Color color = Color.decode("#f97316");
        barChart1 = new HBC();
        barChart1.setBarColor(color);
        barChart1.setDataset(createData());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel1.add(barChart1);
        panel1.setBorder(new ColorableArcableFlatBorder((java.awt.Color)color,10));
        add(panel1, "split 2,gap 0 20");

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener((ActionListener) -> {
            g.pause();
            g.getController().exitGame();
        });
        add(exitButton, "x 90%, y 2%, gap 10");
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
}
