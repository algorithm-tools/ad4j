package org.algorithm.ad4j.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndicatorEvaluateInfo implements Serializable {

    /**
     * model name
     */
    private String anomalyDetectionModel;

    /**
     * anomaly type
     */
    private int anomalyType;

    /**
     * anomaly influence
     */
    private int anomalyInfluence;

    private boolean hasAnomaly;

    /**
     * anomaly indicator point
     */
    private List<IndicatorSeries> seriesList;
    /**
     * anomaly trend
     */
    private int anomalyTrend;
    /**
     * Normal range：min
     */
    private Double normalRangeMin;
    /**
     * Normal range：max
     */
    private Double normalRangeMax;

    public IndicatorEvaluateInfo(String anomalyDetectionModel, int anomalyType, boolean hasAnomaly) {
        if (hasAnomaly) {
            this.seriesList = new ArrayList<>();
        }
        this.anomalyType = anomalyType;
        this.anomalyDetectionModel = anomalyDetectionModel;
        this.hasAnomaly = hasAnomaly;
    }

    public void add(IndicatorSeries series) {
        getSeriesList().add(series);
    }

    public String getAnomalyDetectionModel() {
        return anomalyDetectionModel;
    }

    public void setAnomalyDetectionModel(String anomalyDetectionModel) {
        this.anomalyDetectionModel = anomalyDetectionModel;
    }

    public int getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(int anomalyType) {
        this.anomalyType = anomalyType;
    }

    public int getAnomalyInfluence() {
        return anomalyInfluence;
    }

    public void setAnomalyInfluence(int anomalyInfluence) {
        this.anomalyInfluence = anomalyInfluence;
    }

    public boolean isHasAnomaly() {
        return hasAnomaly;
    }

    public void setHasAnomaly(boolean hasAnomaly) {
        this.hasAnomaly = hasAnomaly;
    }

    public List<IndicatorSeries> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<IndicatorSeries> seriesList) {
        this.seriesList = seriesList;
    }

    public int getAnomalyTrend() {
        return anomalyTrend;
    }

    public void setAnomalyTrend(int anomalyTrend) {
        this.anomalyTrend = anomalyTrend;
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

    @Override
    public String toString() {
        return "IndicatorEvaluateInfo{" +
                "anomalyDetectionModel='" + anomalyDetectionModel + '\'' +
                ", anomalyType=" + anomalyType +
                ", anomalyInfluence=" + anomalyInfluence +
                ", hasAnomaly=" + hasAnomaly +
                ", seriesList=" + seriesList +
                ", anomalyTrend=" + anomalyTrend +
                ", range=[" + normalRangeMin + ", " + normalRangeMax + "]" +
                '}';
    }
}
