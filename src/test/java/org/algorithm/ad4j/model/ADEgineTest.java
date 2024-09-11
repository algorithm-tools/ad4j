package org.algorithm.ad4j.model;

import org.algorithm.ad4j.engine.AnomalyDetectionEngine;
import org.algorithm.ad4j.pojo.AnomalyDetectionLog;
import org.algorithm.ad4j.pojo.AnomalyDetectionResult;
import org.algorithm.ad4j.pojo.IndicatorInfo;
import org.algorithm.ad4j.pojo.IndicatorSeries;
import org.algorithm.ad4j.utils.IndicatorSeriesUtil;
import org.algorithm.chart.JFreeChartUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class ADEgineTest {

    public AnomalyDetectionLog log;

    @Before
    public void before(){
        log = new AnomalyDetectionLog();
    }

    @Test
    public void testEngin(){
        double[] data = new double[]{10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        JFreeChartUtil.drawLineChart("testAD_Engin", indicatorSeries);

        IndicatorInfo info = new IndicatorInfo("test", "test-name", indicatorSeries);
        AnomalyDetectionEngine engine = new AnomalyDetectionEngine();
        AnomalyDetectionResult detect = engine.detect(info);
        IndicatorSeriesUtil.print(detect);
    }

    @After
    public void afterSleep() throws InterruptedException {
        System.out.println("click Enter to close window...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
