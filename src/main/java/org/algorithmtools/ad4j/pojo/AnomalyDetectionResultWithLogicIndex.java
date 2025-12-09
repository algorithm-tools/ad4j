package org.algorithmtools.ad4j.pojo;


import org.algorithmtools.ad4j.enumtype.AnomalyDictType;

import java.util.HashMap;
import java.util.Map;

/**
 * 日期监测结果
 */
public class AnomalyDetectionResultWithLogicIndex {

    private String logicIndex;
    private Double value;
    private Map<AnomalyDictType, AnomalyDictType> anomalyTypeAndInfluenceMap;

    public AnomalyDetectionResultWithLogicIndex(String logicIndex, Double value) {
        this.logicIndex = logicIndex;
        this.value = value;
        this.anomalyTypeAndInfluenceMap = new HashMap<>();
    }

    public String getLogicIndex() {
        return logicIndex;
    }

    public void setLogicIndex(String logicIndex) {
        this.logicIndex = logicIndex;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Map<AnomalyDictType, AnomalyDictType> getAnomalyTypeAndInfluenceMap() {
        return anomalyTypeAndInfluenceMap;
    }

    public void setAnomalyTypeAndInfluenceMap(Map<AnomalyDictType, AnomalyDictType> anomalyTypeAndInfluenceMap) {
        this.anomalyTypeAndInfluenceMap = anomalyTypeAndInfluenceMap;
    }
}
