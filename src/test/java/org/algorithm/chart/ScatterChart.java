package org.algorithm.chart;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;

/**
 * An example of a time series chart create using JFreeChart.  For the most 
 * part, default settings are used, except that the renderer is modified to 
 * show filled shapes (as well as lines) at each data point.
 */
public class ScatterChart extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    private final XYDataset xyDataset;

    private final String panelTitle;
    private final String xAxisLabel;
    private final String yAxisLabel;

    /**
     * A demonstration application showing how to create a simple time series
     * chart.  This example uses monthly data.
     *
     * @param xyDataset  xyDataset.
     * @param title  the frame title.
     * @param xAxisLabel  xAxisLabel.
     * @param yAxisLabel  yAxisLabel.
     * @param width  width.
     * @param height  height.
     */
    public ScatterChart(XYDataset xyDataset, String title, String xAxisLabel, String yAxisLabel, int width, int height) {
        super("Time series chart frame");
        this.xyDataset = xyDataset;
        this.panelTitle = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(width, height));
        setContentPane(chartPanel);
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(xyDataset);
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createScatterPlot(
                panelTitle,  // title
                xAxisLabel,  // x-axis label
                yAxisLabel,  // y-axis label
                dataset);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(true);
            renderer.setDefaultShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        NumberAxis xAxis = new NumberAxis("X");
        plot.setDomainAxis(xAxis);
//        DateAxis axis = (DateAxis) plot.getDomainAxis();
//        axis.setDateFormatOverride(new SimpleDateFormat("YYYY-MM-dd"));

        return chart;
    }


    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        double[] xData = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yData = {5.0, 7.0, 6.0, 8.0, 4.0};
        ScatterChart demo = new ScatterChart(JFreeChartUtil.transfer(xData, yData), "Test Chart", "X", "Y", 800, 600);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}