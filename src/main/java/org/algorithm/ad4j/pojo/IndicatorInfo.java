package org.algorithm.ad4j.pojo;

import java.io.Serializable;
import java.util.List;

public class IndicatorInfo implements Serializable {

    /**
     * indicator en
     */
    private String indicator;
    /**
     * indicator name
     */
    private String indicatorName;
    /**
     * indicator series
     */
    private List<IndicatorSeries> indicatorSeries;

    public IndicatorInfo() {
    }

    public IndicatorInfo(String indicator, String indicatorName, List<IndicatorSeries> indicatorSeries) {
        this.indicator = indicator;
        this.indicatorName = indicatorName;
        this.indicatorSeries = indicatorSeries;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public List<IndicatorSeries> getIndicatorSeries() {
        return indicatorSeries;
    }

    public void setIndicatorSeries(List<IndicatorSeries> indicatorSeries) {
        this.indicatorSeries = indicatorSeries;
    }
}
