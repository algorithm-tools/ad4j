package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;

public class AnomalyIndicatorSeries {
    /**
     * anomaly influence
     */
    private AnomalyDictType anomalyInfluence;

    /**
     * indicator series
     */
    private IndicatorSeries indicatorSeries;

    public AnomalyIndicatorSeries() {
    }

    public AnomalyIndicatorSeries(IndicatorSeries indicatorSeries) {
        this.indicatorSeries = indicatorSeries;
    }

    public AnomalyIndicatorSeries(AnomalyDictType anomalyInfluence, IndicatorSeries indicatorSeries) {
        this.anomalyInfluence = anomalyInfluence;
        this.indicatorSeries = indicatorSeries;
    }

    public AnomalyDictType getAnomalyInfluence() {
        return anomalyInfluence;
    }

    public void setAnomalyInfluence(AnomalyDictType anomalyInfluence) {
        this.anomalyInfluence = anomalyInfluence;
    }

    public IndicatorSeries getIndicatorSeries() {
        return indicatorSeries;
    }

    public void setIndicatorSeries(IndicatorSeries indicatorSeries) {
        this.indicatorSeries = indicatorSeries;
    }

    @Override
    public String toString() {
        return "["+anomalyInfluence + ":" + indicatorSeries.getTime()+", "+indicatorSeries.getValue()+", "+indicatorSeries.getLogicalIndex()+"]";
    }
}
