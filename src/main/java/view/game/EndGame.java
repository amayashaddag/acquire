package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import java.util.Set;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatClientProperties;

import control.game.GameController;
import model.game.Corporation;
import model.game.Player;
import net.miginfocom.swing.MigLayout;
import raven.chart.ChartLegendRenderer;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;
import view.assets.GameResources;

/**
 * @author Arthur Deck
 */
public class EndGame extends JPanel {
    private final GameController controller;

    public EndGame(GameController control) {
        this.controller = control;

        init();
        lineChart.startAnimation();
        pieChart1.startAnimation();
        pieChart2.startAnimation();
        pieChart3.startAnimation();
        barChart1.startAnimation();
        barChart2.startAnimation();
    }

    private void init() {
        setOpaque(false);
        setLayout(new MigLayout("wrap,fill,gap 10", "fill"));
        UIManager.put( "Panel.arc", 10 );
        UIManager.put( "Button.arc", 10 );

        createPieChart();
        createLineChart();
        createBarChart();

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
        exitButton.setOpaque(false);
        exitButton.setBorder(new ColorableArcableFlatBorder(Color.decode("#f97316"), 15));
        exitButton.addActionListener((e) -> {
            controller.exitGame();
        });
        add(exitButton, "x 47%, y 96%");
    }

    private void createPieChart() {
        pieChart1 = new PieChart();
        opaque(pieChart1);
        Map<Corporation, Double> map1 = controller.getMapCorporationsRepartitonData();    
        JLabel header1 = new JLabel("Map's corporations repartition");
        header1.setOpaque(false);
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart1.setHeader(header1);
        pieChart1.getChartColor().addColor(createPieColor(map1));
        pieChart1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart1.setDataset(createCorpPieData(map1));
        add(pieChart1, "split 3,height 290");

        pieChart2 = new PieChart();
        opaque(pieChart2);
        Map<Corporation, Double> map2 = controller.getStockCorporationsRepartitionData();
        JLabel header2 = new JLabel("Stock corporations repartition");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart2.setHeader(header2);
        pieChart2.getChartColor().addColor(createPieColor(map2));
        pieChart2.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart2.setDataset(createCorpPieData(map2)); 
        add(pieChart2, "height 290");

        pieChart3 = new PieChart();
        opaque(pieChart3);
        Map<Player, Double> map3 = controller.getPlayerCorporationsRepartitionData();
        JLabel header3 = new JLabel("Actions");
        header3.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart3.setHeader(header3);
        pieChart3.getChartColor().addColor(Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc"));
        pieChart3.setChartType(PieChart.ChartType.DONUT_CHART);
        pieChart3.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart3.setDataset(createPlayerPieData(map3));  
        add(pieChart3, "height 290");
    }

    private void createLineChart() {
        lineChart = new LineChart();
        opaque(lineChart);
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(lineChart);
        createLineChartData();
    }

    private void createBarChart() {
        // BarChart 1
        barChart1 = new HorizontalBarChart();
        opaque(barChart1);
        JLabel header1 = new JLabel("Cash repartition");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart1.setHeader(header1);
        barChart1.setBarColor(Color.decode("#f97316"));
        barChart1.setDataset(createBarData(controller.getPlayersCashRepartition()));
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.setOpaque(false);
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel1.add(barChart1);
        add(panel1, "split 2,gap 0 20");

        // BarChart 2
        barChart2 = new HorizontalBarChart();
        opaque(barChart2);
        JLabel header2 = new JLabel("Net repartition");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart2.setHeader(header2);
        barChart2.setBarColor(Color.decode("#10b981"));
        barChart2.setDataset(createBarData(controller.getPlayersNetRepartition()));
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setOpaque(false);
        panel2.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel2.add(barChart2);
        add(panel2);
    }

    private DefaultPieDataset<String> createBarData(Map<Player, Integer> map) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<Player, Integer> e : map.entrySet()) 
            dataset.addValue(e.getKey().getPseudo(), e.getValue());
        return dataset;
    }

    private DefaultPieDataset<String> createCorpPieData(Map<Corporation, Double> map) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<Corporation, Double> e : map.entrySet()) 
            dataset.addValue(e.getKey().toString(), e.getValue());
        return dataset;
    }

    private DefaultPieDataset<String> createPlayerPieData(Map<Player, Double> map) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<Player, Double> e : map.entrySet()) 
            dataset.addValue(e.getKey().getPseudo(), e.getValue());
        return dataset;
    }

    private Color[] createPieColor(Map<Corporation, Double> map) {
        // Color.decode("#f87171"), Color.decode("#fb923c"), Color.decode("#fbbf24"), Color.decode("#a3e635"), Color.decode("#34d399"), Color.decode("#22d3ee"), Color.decode("#818cf8"), Color.decode("#c084fc")
        Set<Corporation> set = map.keySet();
        Color[] st = new Color[set.size()];
        int i = 0;
        for (Corporation corp : set) {
            st[i] = GameResources.getCorpColor(corp);
            i++;
        }
        return st;
    }

    private void createLineChartData() {
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
        List<Map<String, Integer>> list = controller.getPlayersNetEvolution();
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, Integer> entry : list.get(i).entrySet()) {
                categoryDataset.addValue(entry.getValue(), entry.getKey(), ""+i);
            }
        }

        /**
         * Control the legend we do not show all legend
         */
        try {
            lineChart.setLegendRenderer(new ChartLegendRenderer() {
                @Override
                public Component getLegendComponent(Object legend, int index) {
                    if (index % 5 == 0) {
                        return super.getLegendComponent(legend, index);
                    } else {
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e);
        }

        lineChart.setCategoryDataset(categoryDataset);
        lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
        JLabel header = new JLabel("Net evolution");
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        lineChart.setHeader(header);
    }

    private LineChart lineChart;
    private HorizontalBarChart barChart1;
    private HorizontalBarChart barChart2;
    private PieChart pieChart1;
    private PieChart pieChart2;
    private PieChart pieChart3;

    private void opaque(Component c) {
        if (c instanceof JComponent) {
            JComponent jc = ((JComponent)c);
            jc.setOpaque(false);
            for (Component c2 : jc.getComponents()) {
                opaque(c2);
            }
        }
    }
}