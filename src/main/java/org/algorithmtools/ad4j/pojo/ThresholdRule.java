package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.enumtype.CompareType;
import org.algorithmtools.ad4j.enumtype.ThresholdType;

import java.util.List;

/**
 * Threshold Rule: Indicator compareType (factor * thresholdType)
 */
public class ThresholdRule implements ThresholdRuleBase{

    ThresholdType thresholdType;

    CompareType compareType;

    Double factor;

    public ThresholdRule(ThresholdType thresholdType, CompareType compareType, Double factor) {
        this.thresholdType = thresholdType;
        this.compareType = compareType;
        this.factor = factor;
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public CompareType getCompareType() {
        return compareType;
    }

    public Double getFactor() {
        return factor;
    }

    @Override
    public List<ThresholdRuleBase> getThresholdRules() {
        return null;
    }
}
