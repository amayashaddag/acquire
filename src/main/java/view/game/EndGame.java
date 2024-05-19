package view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import javax.swing.JButton;
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
        UIManager.put( "JPanel.arc", 10 );

        createPieChart();
        createLineChart();
        createBarChart();

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(view.assets.Fonts.REGULAR_PARAGRAPH_FONT);
        exitButton.addActionListener((e) -> {
            controller.exitGame();
        });
        add(exitButton, "x 47%, y 97%");
    }

    private void createPieChart() { 
        pieChart1 = new PieChart();
        Map<Corporation, Double> map1 = null;    // FIXME : ivicici
        JLabel header1 = new JLabel("Map's corporations repartition");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1");
        pieChart1.setHeader(header1);
        pieChart1.getChartColor().addColor(createPieColor(map1));
        pieChart1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        pieChart1.setDataset(createCorpPieData(map1));
        add(pieChart1, "split 3,height 290");

        pieChart2 = new PieChart();
        Map<Corporation, Double> map2 = null;   // FIXME :::  ic
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
        Map<Player, Double> map3 = null;   // FIXME :::  ic
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
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        add(lineChart);
        createLineChartData();
    }

    private void createBarChart() {
        // BarChart 1
        barChart1 = new HorizontalBarChart();
        JLabel header1 = new JLabel("Monthly Income");
        header1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart1.setHeader(header1);
        barChart1.setBarColor(Color.decode("#f97316"));
        barChart1.setDataset(createData());
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel1.add(barChart1);
        add(panel1, "split 2,gap 0 20");

        // BarChart 2
        barChart2 = new HorizontalBarChart();
        JLabel header2 = new JLabel("Monthly Expense");
        header2.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:+1;"
                + "border:0,0,5,0");
        barChart2.setHeader(header2);
        barChart2.setBarColor(Color.decode("#10b981"));
        barChart2.setDataset(createData());
        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5,$Component.borderColor,,20");
        panel2.add(barChart2);
        add(panel2);
    }

    private DefaultPieDataset<String> createData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        Random random = new Random();
        dataset.addValue("July (ongoing)", 0);
        dataset.addValue("June", random.nextInt(100));
        dataset.addValue("May", random.nextInt(100));
        dataset.addValue("April", random.nextInt(100));
        dataset.addValue("March", random.nextInt(100));
        dataset.addValue("February", random.nextInt(100));
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
        Stack<Color> st = new Stack<>();
        for (Corporation corp : map.keySet())
            st.add(GameResources.getCorpColor(corp));
        return (Color[]) st.toArray();
    }

    private void createLineChartData() {
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        Random ran = new Random();
        int randomDate = 30;
        for (int i = 1; i <= randomDate; i++) {
            String date = df.format(cal.getTime());
            categoryDataset.addValue(ran.nextInt(700) + 5, "Income", date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Expense", date);
            categoryDataset.addValue(ran.nextInt(700) + 5, "Profit", date);

            cal.add(Calendar.DATE, 1);
        }

        /**
         * Control the legend we do not show all legend
         */
        try {
            lineChart.setLegendRenderer(new ChartLegendRenderer() {
                @Override
                public Component getLegendComponent(Object legend, int index) {
                    if (index % 5 == 0) {   // FIXME: 5
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
        JLabel header = new JLabel("Income Data");
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
}