package org.algorithm.chart;

import org.algorithm.ad4j.pojo.IndicatorSeries;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.util.List;

public class JFreeChartUtil {

    public static XYDataset transfer(DefaultXYDataset dataset, String series, double[] x, double[] y){
        dataset = dataset == null ? new DefaultXYDataset() : dataset;
        dataset.addSeries(series, new double[][] {x, y});
        return dataset;
    }

    public static XYDataset transfer(double[] x, double[] y){
        return transfer(null, "data", x, y);
    }

    public static void drawLineChart(String title, double[] xData, double[] yData){
        SwingUtilities.invokeLater(() -> {
            LineChart chart = new LineChart(JFreeChartUtil.transfer(xData, yData), title, "X", "Y", 800, 600);
            chart.pack();
            UIUtils.centerFrameOnScreen(chart);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });
    }

    public static void drawLineChart(String title, List<Integer> xData, List<Double> yData){
        double[] x = new double[xData.size()];
        double[] y = new double[yData.size()];
        for (int i = 0; i < xData.size(); i++) {
            x[i] = xData.get(i);
            y[i] = yData.get(i);
        }

        drawLineChart(title, x, y);
    }

    public static void drawLineChart(String title, List<IndicatorSeries> data){
        double[] x = new double[data.size()];
        double[] y = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            x[i] = data.get(i).getTime();
            y[i] = data.get(i).getValue();
        }

        drawLineChart(title, x, y);
    }

    public static void drawScatterChart(String title, List<IndicatorSeries> data){
        double[] x = new double[data.size()];
        double[] y = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            x[i] = data.get(i).getTime();
            y[i] = data.get(i).getValue();
        }

        SwingUtilities.invokeLater(() -> {
            ScatterChart chart = new ScatterChart(JFreeChartUtil.transfer(x, y), title, "X", "Y", 800, 600);
            chart.pack();
            UIUtils.centerFrameOnScreen(chart);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });
    }


}
