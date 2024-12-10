package org.algorithmtools.ad4j.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.enumtype.CompareType;
import org.algorithmtools.ad4j.enumtype.LogicType;
import org.algorithmtools.ad4j.enumtype.ThresholdType;
import org.algorithmtools.ad4j.model.adm.*;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;
import org.algorithmtools.chart.JFreeChartUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        double[] data = {10.0, 12.0, 85.0, 70, 100.0, 14.0, 14.0, 12.0, 40, 20, 20};
//        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        List<IndicatorSeries> indicatorSeries = parseData("[{\"logicalIndex\":\"0\",\"time\":0,\"value\":1104.0},{\"logicalIndex\":\"1\",\"time\":1,\"value\":976.0},{\"logicalIndex\":\"2\",\"time\":2,\"value\":949.0},{\"logicalIndex\":\"3\",\"time\":3,\"value\":895.0},{\"logicalIndex\":\"4\",\"time\":4,\"value\":810.0},{\"logicalIndex\":\"5\",\"time\":5,\"value\":975.0},{\"logicalIndex\":\"6\",\"time\":6,\"value\":1152.0},{\"logicalIndex\":\"7\",\"time\":7,\"value\":818.0},{\"logicalIndex\":\"8\",\"time\":8,\"value\":766.0},{\"logicalIndex\":\"9\",\"time\":9,\"value\":502.0},{\"logicalIndex\":\"10\",\"time\":10,\"value\":396.0},{\"logicalIndex\":\"11\",\"time\":11,\"value\":468.0},{\"logicalIndex\":\"12\",\"time\":12,\"value\":592.0},{\"logicalIndex\":\"13\",\"time\":13,\"value\":769.0}]");
        JFreeChartUtil.drawLineChart("testADM_2ndDerivationMBP", indicatorSeries);

        AbstractADM model = new ADM_2ndDerivationMBP();
        model.init(AnomalyDetectionContext.createDefault());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_GESD(){
        double[] data = new double[]{10.0,12.0,12.0,13.0,12.0,11.0,50.0};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_GESD();
        model.init(AnomalyDetectionContext.createDefault());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_MannKendall(){
        double[] data = new double[]{10.0, 12.0, 12.5, 13.0, 55.0, 10.5, 14.0, 15.0, 14.5, 16.0};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_MannKendall();
        model.init(AnomalyDetectionContext.createDefault());
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
        model.init(AnomalyDetectionContext.createDefault());
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
        model.init(AnomalyDetectionContext.createDefault());
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_ThresholdRule(){
        double[] data = new double[]{1.0,2.0,3.0,4.0};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_ThresholdRule();
        AnomalyDetectionContext adContext = AnomalyDetectionContext.createDefault();
        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_USE, true);

        ThresholdRuleGroup ruleGroupL2 = new ThresholdRuleGroup(LogicType.AND);
        ThresholdRuleBase ruleBaseL2_1 = new ThresholdRule(ThresholdType.MIN, CompareType.GREATER, 1.0);
        ThresholdRuleBase ruleBaseL2_2 = new ThresholdRule(ThresholdType.MAX, CompareType.LESS, 1.0);
        ruleGroupL2.addRules(ruleBaseL2_1);
        ruleGroupL2.addRules(ruleBaseL2_2);
        ThresholdRuleBase ruleBaseL1_1 = new ThresholdRule(ThresholdType.CONSTANT, CompareType.GREATER_OR_EQ, 5.0);
        ThresholdRuleBase ruleBaseL1_2 = new ThresholdRule(ThresholdType.CONSTANT, CompareType.LESS_OR_EQ, 1.0);
        ThresholdRuleGroup ruleGroupL1 = new ThresholdRuleGroup(LogicType.OR);
        ruleGroupL1.addRules(ruleBaseL1_1);
        ruleGroupL1.addRules(ruleBaseL1_2);
        ruleGroupL1.addRules(ruleGroupL2);
        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_SET, ruleGroupL1);
        model.init(adContext);
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_ThresholdRule2(){
        double[] data = new double[]{1.0,2.0,3.0,4.0};
        List<IndicatorSeries> indicatorSeries = IndicatorSeriesUtil.transferFromArray(data);
        AbstractADM model = new ADM_ThresholdRule();

        String propertiesJsonStr = "{\"adm.quantile.use\":false,\"adm.threshold_rule.use\":true,\"adm.threshold_rule.set\":{\"logicType\":\"or\",\"ruleGroup\":[{\"factor\":5.0,\"thresholdType\":\"constant\",\"compareType\":\">=\"},{\"factor\":1.0,\"thresholdType\":\"constant\",\"compareType\":\"<=\"}]}}";
        JSONObject propertiesJson = JSONObject.parseObject(propertiesJsonStr);
        AnomalyDetectionContext adContext = AnomalyDetectionContext.create(propertiesJson);

        model.init(adContext);
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    public List<IndicatorSeries> parseData(String s){
        s = s != null ? s :
                "[{\"logicalIndex\":\"0\",\"time\":0,\"value\":45.29},{\"logicalIndex\":\"1\",\"time\":1,\"value\":30.85},{\"logicalIndex\":\"2\",\"time\":2,\"value\":40.23},{\"logicalIndex\":\"3\",\"time\":3,\"value\":15.57},{\"logicalIndex\":\"4\",\"time\":4,\"value\":13.14},{\"logicalIndex\":\"5\",\"time\":5,\"value\":32.53},{\"logicalIndex\":\"6\",\"time\":6,\"value\":44.34},{\"logicalIndex\":\"7\",\"time\":7,\"value\":33.92},{\"logicalIndex\":\"8\",\"time\":8,\"value\":25.31},{\"logicalIndex\":\"9\",\"time\":9,\"value\":31.12},{\"logicalIndex\":\"10\",\"time\":10,\"value\":33.23},{\"logicalIndex\":\"11\",\"time\":11,\"value\":40.65},{\"logicalIndex\":\"12\",\"time\":12,\"value\":32.88},{\"logicalIndex\":\"13\",\"time\":13,\"value\":31.14}]";
        JSONArray jsonArray = JSONArray.parseArray(s);
        List<IndicatorSeries> indicatorSeries = new ArrayList<>();
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            indicatorSeries.add(new IndicatorSeries(json.getLong("time"), json.getDoubleValue("value"), json.getString("logicalIndex")));
        }
        return indicatorSeries;
    }

    @After
    public void afterSleep() throws InterruptedException {
        System.out.println("click Enter to close window...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
