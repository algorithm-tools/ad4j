package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.enumtype.LogicType;
import org.algorithmtools.ad4j.enumtype.ThresholdType;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;
import java.util.Objects;

/**
 * Anomaly detection model: Threshold Rule
 * <p>Threshold rule matching<p/>
 *
 * @author mym
 */
public class ADM_ThresholdRule extends AbstractADM {
    private ThresholdRuleBase thresholdRule = null;

    public ADM_ThresholdRule() {
        super(AnomalyDictType.MODEL_ADM_ThresholdRule, AnomalyDictType.TYPE_THRESHOLD);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        Object config = context.getConfig(ADMConfigs.ADM_THRESHOLD_RULE_SET);
        this.thresholdRule = Objects.isNull(config) ? null : (ThresholdRuleBase) config;
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        if (this.thresholdRule == null) {
            return null;
        }

        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        DescriptiveStatistics stats = IndicatorCalculateUtil.initStatistic(null, indicatorSeries, null);
        double mean = stats.getMean();
        List<IndicatorSeries> sortList = IndicatorCalculateUtil.sortAsc(indicatorSeries);
        for (IndicatorSeries indicator : indicatorSeries) {
            boolean hasAnomaly = judgeHasAnomaly(sortList, stats, this.thresholdRule, indicator);
            if (hasAnomaly) {
                result.setHasAnomaly(hasAnomaly);
                result.add(new AnomalyIndicatorSeries(indicator.getValue() > mean ? AnomalyDictType.INFLUENCE_POSITIVE : AnomalyDictType.INFLUENCE_NEGATIVE, indicator));
            }
        }

        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        return true;
    }

    private boolean judgeHasAnomaly(List<IndicatorSeries> sortList, DescriptiveStatistics stats, ThresholdRuleBase currentRule, IndicatorSeries currentIndicator) {
        if (Objects.isNull(currentRule.getThresholdRules())) {
            ThresholdRule rule = (ThresholdRule) currentRule;
            return thresholdCompare(sortList, stats, rule, currentIndicator);
        } else {
            Boolean ruleGroupResult = null;
            ThresholdRuleGroup ruleGroup = (ThresholdRuleGroup) currentRule;
            for (ThresholdRuleBase rule : ruleGroup.getThresholdRules()) {
                boolean hasAnomaly = judgeHasAnomaly(sortList, stats, rule, currentIndicator);
                if (ruleGroup.getLogicType() == LogicType.AND) {
                    ruleGroupResult = ruleGroupResult == null ? hasAnomaly : ruleGroupResult && hasAnomaly;
                } else if (ruleGroup.getLogicType() == LogicType.OR) {
                    ruleGroupResult = ruleGroupResult == null ? hasAnomaly : ruleGroupResult || hasAnomaly;
                }
            }
            return Boolean.TRUE.equals(ruleGroupResult);
        }
    }

    private boolean thresholdCompare(List<IndicatorSeries> sortList, DescriptiveStatistics stats, ThresholdRule currentRule, IndicatorSeries currentIndicator) {
        return currentRule.getCompareType().compare(currentIndicator.getValue(), calcCompareValue(sortList, stats, currentRule));
    }

    private Double calcCompareValue(List<IndicatorSeries> sortList, DescriptiveStatistics stats, ThresholdRule rule) {
        Double compareValue = null;
        if (rule.getThresholdType() == ThresholdType.CONSTANT) {
            compareValue = rule.getFactor();
        } else if (rule.getThresholdType() == ThresholdType.MIN) {
            compareValue = rule.getFactor() * stats.getMin();
        } else if (rule.getThresholdType() == ThresholdType.MAX) {
            compareValue = rule.getFactor() * stats.getMax();
        } else if (rule.getThresholdType() == ThresholdType.MEAN) {
            compareValue = rule.getFactor() * stats.getMean();
        } else if (rule.getThresholdType() == ThresholdType.STD) {
            compareValue = rule.getFactor() * stats.getStandardDeviation();
        } else if (rule.getThresholdType() == ThresholdType.VAR) {
            compareValue = rule.getFactor() * stats.getVariance();
        } else if (rule.getThresholdType() == ThresholdType.QUANTILE) {
            compareValue = IndicatorCalculateUtil.quantile(sortList, rule.getFactor());
        }

        return compareValue;
    }
}
