package org.algorithmtools.ad4j.model;

import com.alibaba.fastjson.JSONObject;
import org.algorithmtools.ad4j.engine.AnomalyDetectionEngine;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;
import org.algorithmtools.chart.JFreeChartUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class ADEngineTest {

    public AnomalyDetectionLog log;

    @Before
    public void before(){
        log = new AnomalyDetectionLog();
    }

    @Test
    public void testEngin(){
        double[] data = new double[]{10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        JFreeChartUtil.drawLineChart("TestAD_Engin", indicatorSeries);

        IndicatorInfo info = new IndicatorInfo("Test", "Test-name", indicatorSeries);
        AnomalyDetectionEngine engine = new AnomalyDetectionEngine();
        AnomalyDetectionResult detect = engine.detect(info);
        IndicatorSeriesUtil.print(detect);
    }

    @Test
    public void testEnginByConfig(){
        double[] data = new double[]{11.5, 12, 10, 36, 13.0, 10.5, 5, 14.0, 15.0, 14.5, 15.5};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        JFreeChartUtil.drawLineChart("TestAD_Engin", indicatorSeries);

        IndicatorInfo info = new IndicatorInfo("Test", "Test-name", indicatorSeries);

        JSONObject propertiesJson = new JSONObject();
        propertiesJson.put("adm.zscore.use", false);
        propertiesJson.put("adm.gesd.use", false);
        propertiesJson.put("adm.gesd.alpha", 0.1);
        propertiesJson.put("adm.quantile.use", false);
        propertiesJson.put("adm.quantile.iqr.multiplier", 2);
        propertiesJson.put("adm.2nd_derivation_mbp.use", true);
        propertiesJson.put("adm.2nd_derivation_mbp.threshold_factor", "0.1s");
        propertiesJson.put("adm.mannkendall.use", false);
        propertiesJson.put("adm.mannkendall.criticalz", "0.01s");
        propertiesJson.put("adm.threshold_rule.use", false);

        AnomalyDetectionContext anomalyDetectionContext = AnomalyDetectionContext.create(propertiesJson);
        AnomalyDetectionEngine engine = new AnomalyDetectionEngine(anomalyDetectionContext);

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
