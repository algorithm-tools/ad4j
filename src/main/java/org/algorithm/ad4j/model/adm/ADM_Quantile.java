package org.algorithm.ad4j.model.adm;

import org.algorithm.ad4j.constant.AnomalyConstant;
import org.algorithm.ad4j.pojo.AnomalyDetectionContext;
import org.algorithm.ad4j.pojo.AnomalyDetectionLog;
import org.algorithm.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithm.ad4j.pojo.IndicatorSeries;
import org.algorithm.ad4j.utils.IndicatorSeriesComparator;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Anomaly detection model: anomaly detection based on quantile (boxplot)
 * @author mym
 */
public class ADM_Quantile extends AbstractADM {

    /**
     * IQR weight multiplier
     */
    private double iqrMultiplier;

    public ADM_Quantile() {
        super(AnomalyConstant.MODEL_ADM_Threshold, AnomalyConstant.TYPE_THRESHOLD);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.iqrMultiplier = 1.5;
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> data, AnomalyDetectionLog log) {
        // sort
        data.sort(new IndicatorSeriesComparator());

        // calculate Q1,Q3
        double Q1 = quantile(data, 0.25);  // 25%
        double Q3 = quantile(data, 0.75);  // 75%

        // calculate IQR
        double IQR = Q3 - Q1;

        // calculate bound
        double lowerBound = Q1 - iqrMultiplier * IQR;
        double upperBound = Q3 + iqrMultiplier * IQR;

        // find anomaly indicator series
        List<IndicatorSeries> anomalyList = data.stream().filter(v -> v.getValue() > upperBound || v.getValue() < lowerBound).collect(Collectors.toList());

        // build evaluate info
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (CollectionUtils.isNotEmpty(anomalyList)) {
            result.setHasAnomaly(true);
            result.setNormalRangeMin(lowerBound);
            result.setNormalRangeMax(upperBound);
            result.setSeriesList(anomalyList);
            return result;
        }

        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        return true;
    }

    private double quantile(List<IndicatorSeries> sortedData, double quantile) {
        int n = sortedData.size();
        double index = quantile * (n - 1);
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);

        // 如果 index 恰好是整数，直接返回对应的数据点
        if (lowerIndex == upperIndex) {
            return sortedData.get(lowerIndex).getValue();
        }

        // 否则进行线性插值
        double weight = index - lowerIndex;
        return (1 - weight) * sortedData.get(lowerIndex).getValue() + weight * sortedData.get(upperIndex).getValue();
    }

}


