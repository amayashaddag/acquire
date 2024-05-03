package view.game;

import view.game.BlurPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import model.game.Player;
import model.tools.AutoSetter;
import view.window.GameFrame;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import raven.chart.bar.HorizontalBarChart;
import java.awt.Color;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import java.awt.BorderLayout;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.Map;
import model.game.Corporation;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.JLayeredPane;

/**
 * @author Arthur Deck
 */
@AutoSetter(typeParam = GameView.class)
public class PausePane extends BlurPane {
    public PausePane(GameView gv) {
        this.g = gv;
        this.player = g.getPlayer();
        setLayout(new MigLayout("al center, filly, wrap"));
        init2();
        new Thread(()-> {
            try {
                Thread.sleep(10);
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
        barChart1 = new HorizontalBarChart();
        JLabel header1 = new JLabel("Actions");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart1.setHeader(header1);
        barChart1.setBarColor(Color.decode("#f97316"));
        barChart1.setDataset(createData());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,Component.borderColor,,20");
        panel1.add(barChart1);
        try {
            panel1.setBorder(new ColorableArcableFlatBorder(Color.decode("#f97316"),10));
        } catch(Exception e) {} // Normal
        add(panel1, "split 2,gap 0 20");
    }

    private DefaultPieDataset createData() {
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
