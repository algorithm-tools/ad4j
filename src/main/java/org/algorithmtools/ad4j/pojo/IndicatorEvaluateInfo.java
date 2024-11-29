package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndicatorEvaluateInfo implements Serializable {
    private boolean hasAnomaly;
    /**
     * model name
     */
    private AnomalyDictType anomalyDetectionModel;

    /**
     * anomaly type
     */
    private AnomalyDictType anomalyType;

    /**
     * anomaly trend
     */
    private AnomalyDictType anomalyTrend;
    /**
     * anomaly indicator point
     */
    private List<AnomalyIndicatorSeries> anomalySeriesList;
    /**
     * Normal range：min
     */
    private Double normalRangeMin;
    /**
     * Normal range：max
     */
    private Double normalRangeMax;

    public IndicatorEvaluateInfo(AnomalyDictType anomalyDetectionModel, AnomalyDictType anomalyType, boolean hasAnomaly) {
        if (hasAnomaly) {
            this.anomalySeriesList = new ArrayList<>();
        }
        this.anomalyType = anomalyType;
        this.anomalyDetectionModel = anomalyDetectionModel;
        this.hasAnomaly = hasAnomaly;
    }

    public void add(AnomalyIndicatorSeries series) {
        if(getAnomalySeriesList() == null){
            this.anomalySeriesList = new ArrayList<>();
        }
        getAnomalySeriesList().add(series);
    }

    public boolean isHasAnomaly() {
        return hasAnomaly;
    }

    public void setHasAnomaly(boolean hasAnomaly) {
        this.hasAnomaly = hasAnomaly;
    }

    public List<AnomalyIndicatorSeries> getAnomalySeriesList() {
        return anomalySeriesList;
    }

    public void setAnomalySeriesList(List<AnomalyIndicatorSeries> anomalySeriesList) {
        this.anomalySeriesList = anomalySeriesList;
    }

    public Double getNormalRangeMin() {
        return normalRangeMin;
    }

    public void setNormalRangeMin(Double normalRangeMin) {
        this.normalRangeMin = normalRangeMin;
    }

    public Double getNormalRangeMax() {
        return normalRangeMax;
    }

    public void setNormalRangeMax(Double normalRangeMax) {
        this.normalRangeMax = normalRangeMax;
    }

    public AnomalyDictType getAnomalyDetectionModel() {
        return anomalyDetectionModel;
    }

    public void setAnomalyDetectionModel(AnomalyDictType anomalyDetectionModel) {
        this.anomalyDetectionModel = anomalyDetectionModel;
    }

    public AnomalyDictType getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(AnomalyDictType anomalyType) {
        this.anomalyType = anomalyType;
    }

    public AnomalyDictType getAnomalyTrend() {
        return anomalyTrend;
    }

    public void setAnomalyTrend(AnomalyDictType anomalyTrend) {
        this.anomalyTrend = anomalyTrend;
    }

    @Override
    public String toString() {
        return "IndicatorEvaluateInfo{" +
                "anomalyDetectionModel='" + anomalyDetectionModel + '\'' +
                ", anomalyType=" + anomalyType +
                ", hasAnomaly=" + hasAnomaly +
                ", anomalySeriesList=" + anomalySeriesList +
                ", anomalyTrend=" + anomalyTrend +
                ", range=[" + normalRangeMin + ", " + normalRangeMax + "]" +
                '}';
    }
}
