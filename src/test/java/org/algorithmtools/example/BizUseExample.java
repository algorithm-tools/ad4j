package org.algorithmtools.example;

import org.algorithmtools.ad4j.engine.AnomalyDetectionEngine;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionResult;
import org.algorithmtools.ad4j.pojo.IndicatorInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;

import java.util.ArrayList;
import java.util.List;

public class BizUseExample {

    public static void main(String[] args) {
        indicatorDetect();
    }

    public static void indicatorDetect(){
        // 1. Transfer biz data to indicator series info
        long currentTime = System.currentTimeMillis();
        List<IndicatorSeries> indicatorSeries = new ArrayList<>();
        indicatorSeries.add(new IndicatorSeries(currentTime + 1, 1d, "logicalIndex-1"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 2, 2d, "logicalIndex-2"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 3, 3d, "logicalIndex-3"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 4, 4d, "logicalIndex-4"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 5, 40d, "logicalIndex-5"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 6, 6d, "logicalIndex-6"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 7, 7d, "logicalIndex-7"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 8, 8d, "logicalIndex-8"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 9, 9d, "logicalIndex-9"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 10, 10d, "logicalIndex-10"));

        IndicatorInfo info = new IndicatorInfo("Example", "Example-Name", indicatorSeries);

        // 2. New AnomalyDetectionEngine to detect
        AnomalyDetectionEngine engine = new AnomalyDetectionEngine();
        AnomalyDetectionResult detectionResult = engine.detect(info);

        // 3. Business process detect result. Like Records,Alarms,Print
        IndicatorSeriesUtil.print(detectionResult);
    }

}
