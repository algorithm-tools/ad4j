package org.algorithm.ad4j.model;

import org.algorithm.ad4j.model.adm.*;
import org.algorithm.ad4j.pojo.AnomalyDetectionContext;
import org.algorithm.ad4j.pojo.AnomalyDetectionLog;
import org.algorithm.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithm.ad4j.pojo.IndicatorSeries;
import org.algorithm.ad4j.utils.IndicatorSeriesUtil;
import org.algorithm.chart.JFreeChartUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class ADMTest {

    public AnomalyDetectionLog log;

    @Before
    public void before(){
        log = new AnomalyDetectionLog();
    }

    @Test
    public void testADM_2ndDerivationMBP(){
        double[] data = {10.0, 12.0, 12.5, 85.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};;
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);

        AbstractADM model = new ADM_2ndDerivationMBP();
        model.init(new AnomalyDetectionContext());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_GESD(){
        double[] data = new double[]{10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_GESD();
        model.init(new AnomalyDetectionContext());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_MannKendall(){
        double[] data = new double[]{10.0, 12.0, 12.5, 13.0, 55.0, 10.5, 14.0, 15.0, 14.5, 16.0};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_MannKendall();
        model.init(new AnomalyDetectionContext());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_Quantile(){
        double[] data = new double[]{10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        JFreeChartUtil.drawLineChart("testADM_Quantile_Line", indicatorSeries);
        JFreeChartUtil.drawScatterChart("testADM_Quantile_Scatter", indicatorSeries);

        AbstractADM model = new ADM_Quantile();
        model.init(new AnomalyDetectionContext());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @After
    public void afterSleep() throws InterruptedException {
        System.out.println("click Enter to close window...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
