package org.algorithmtools.ad4j.model;

import org.algorithmtools.ad4j.model.adm.*;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionContext;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionLog;
import org.algorithmtools.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;
import org.algorithmtools.chart.JFreeChartUtil;
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
        JFreeChartUtil.drawLineChart("TestADM_Quantile_Line", indicatorSeries);
        JFreeChartUtil.drawScatterChart("TestADM_Quantile_Scatter", indicatorSeries);

        AbstractADM model = new ADM_Quantile();
        model.init(new AnomalyDetectionContext());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_ZScore(){
        double[] data = new double[]{10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};
        data = new double[]{
                1.26
                ,1.10
                ,1.54
                ,2.58
                ,3.48
                ,1.64
                ,1.74
                ,1.36
                ,2.53
                ,2.47
                ,1.56
                ,0.91
                ,2.00
        };
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_ZScore();
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
