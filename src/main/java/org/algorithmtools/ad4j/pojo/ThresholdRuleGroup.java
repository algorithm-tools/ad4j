package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.enumtype.LogicType;

import java.util.ArrayList;
import java.util.List;

/**
 * Threshold Rule Group
 */
public class ThresholdRuleGroup implements ThresholdRuleBase {

    List<ThresholdRuleBase> thresholdRules;

    LogicType logicType;

    public ThresholdRuleGroup(List<ThresholdRuleBase> thresholdRules, LogicType logicType) {
        this.thresholdRules = thresholdRules;
        this.logicType = logicType;
    }

    public ThresholdRuleGroup(LogicType logicType) {
        this.logicType = logicType;
    }

    public void addRules(ThresholdRuleBase rule) {
        if (this.thresholdRules == null) {
            thresholdRules = new ArrayList<>();
        }
        thresholdRules.add(rule);
    }

    @Override
    public List<ThresholdRuleBase> getThresholdRules() {
        return thresholdRules;
    }

    public LogicType getLogicType() {
        return logicType;
    }
}
