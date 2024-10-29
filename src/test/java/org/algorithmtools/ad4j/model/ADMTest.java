package org.algorithmtools.ad4j.model;

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

    @Test
    public void testADM_ThresholdRule(){
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
        AbstractADM model = new ADM_ThresholdRule();
        AnomalyDetectionContext adContext = AnomalyDetectionContext.createDefault();
        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_USE, true);

        ThresholdRuleBase ruleBase = new ThresholdRule(ThresholdType.CONSTANT, CompareType.GREATER_OR_EQ, 5.0);
        ThresholdRuleBase ruleBase2 = new ThresholdRule(ThresholdType.CONSTANT, CompareType.LESS_OR_EQ, 1.0);
        ThresholdRuleGroup ruleGroup = new ThresholdRuleGroup(LogicType.OR);
        ruleGroup.addRules(ruleBase);
        ruleGroup.addRules(ruleBase2);
        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_SET, ruleGroup);
        model.init(adContext);
        model.checkCompatibility(indicatorSeries, log);

        IndicatorEvaluateInfo evaluate = model.evaluate(indicatorSeries, log);
        IndicatorSeriesUtil.print(evaluate);
    }

    @Test
    public void testADM_ThresholdRule2(){
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
        AbstractADM model = new ADM_ThresholdRule();
//        AnomalyDetectionContext adContext = AnomalyDetectionContext.createDefault();
//        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_USE, true);
//
//        ThresholdRuleBase ruleBase = new ThresholdRule(ThresholdType.CONSTANT, CompareType.GREATER_OR_EQ, 5.0);
//        ThresholdRuleBase ruleBase2 = new ThresholdRule(ThresholdType.CONSTANT, CompareType.LESS_OR_EQ, 1.0);
//        ThresholdRuleGroup ruleGroup = new ThresholdRuleGroup(LogicType.OR);
//        ruleGroup.addRules(ruleBase);
//        ruleGroup.addRules(ruleBase2);
//        adContext.putConfig(ADMConfigs.ADM_THRESHOLD_RULE_SET, ruleGroup);

        String propertiesJsonStr = "{\"adm.quantile.use\":false,\"adm.threshold_rule.use\":true,\"adm.threshold_rule.set\":{\"logicType\":\"or\",\"ruleGroup\":[{\"factor\":5.0,\"thresholdType\":\"constant\",\"compareType\":\">=\"},{\"factor\":1.0,\"thresholdType\":\"constant\",\"compareType\":\"<=\"}]}}";
        JSONObject propertiesJson = JSONObject.parseObject(propertiesJsonStr);
        AnomalyDetectionContext adContext = AnomalyDetectionContext.create(propertiesJson);

        model.init(adContext);
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
