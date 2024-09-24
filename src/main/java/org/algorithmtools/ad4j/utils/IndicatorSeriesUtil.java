package org.algorithmtools.ad4j.utils;

import com.alibaba.fastjson.JSONObject;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionResult;
import org.algorithmtools.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndicatorSeriesUtil {

    public static List<IndicatorSeries> transferFromArray(double[] array){
        List<IndicatorSeries> list = new ArrayList<IndicatorSeries>();
        for (int i = 0; i < array.length; i++) {
            list.add(i, new IndicatorSeries(i, array[i], String.valueOf(i)));
        }
        return list;
    }

    public static double[] transferToArray(List<IndicatorSeries> series){
        double[] resultArray = new double[series.size()];
        for (int i = 0; i < series.size(); i++) {
            resultArray[i] = series.get(i).getValue();
        }
        return resultArray;
    }

    public static void print(IndicatorEvaluateInfo evaluateInfo){
        System.out.println(evaluateInfo);
    }

    public static void print(AnomalyDetectionResult result){
        System.out.println("==============Anomaly Detection Result=============");
        StringBuilder printString = new StringBuilder();
        printString.append("1.Anomaly detection original data:").append(result.getIndicatorInfo().getIndicatorSeries());
        printString.append("\n");
        printString.append("2.Overview: has ").append(result.getIndicatorEvaluateMap().size()).append(" types anomaly detected.");
        int i = 1;
        for (Map.Entry<AnomalyDictType, ArrayList<IndicatorEvaluateInfo>> entry : result.getIndicatorEvaluateMap().entrySet()) {
            printString.append("\n");
            printString.append("3.").append(i).append(".[Anomaly: ").append(entry.getKey().getEnName()).append("] ").append(JSONObject.toJSONString(entry.getValue()));
            i++;
        }

        System.out.println(printString);

        System.out.println("==============Anomaly Detection Result End=========");
    }

}
