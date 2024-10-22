package org.algorithmtools.ad4j.pojo;

import java.io.Serializable;
import java.util.List;

public interface ThresholdRuleBase extends Serializable {

    List<ThresholdRuleBase> getThresholdRules();

}
