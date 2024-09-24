package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionContext;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionLog;
import org.algorithmtools.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

/**
 * Anomaly detection model: Z-score
 * <p>Suitable for normal distribution, few outliers, outliers may affect the mean and standard deviation and thus the detection rate,high computational efficiency<p/>
 * @author mym
 */
public class ADM_ZScore extends AbstractADM {
    private double scoreThreshold = 2;
    public ADM_ZScore() {
        super(AnomalyDictType.MODEL_ADM_ZScore, AnomalyDictType.TYPE_OUTLIERS_VALUE);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.scoreThreshold = 2;
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        DescriptiveStatistics stats = IndicatorCalculateUtil.initStatistic(null, indicatorSeries, null);
        // calculate mean and std
        double mean = stats.getMean();
        double stdDev = stats.getStandardDeviation();
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        for (int i = 0; i < indicatorSeries.size(); i++) {
            double zScore = (indicatorSeries.get(i).getValue() - mean) / stdDev;
            if (Math.abs(zScore) > scoreThreshold) {
                result.add(indicatorSeries.get(i));
                result.setHasAnomaly(true);
            }
        }

        // TODO calculate range
        result.setNormalRangeMin(0d);
        result.setNormalRangeMax(0d);
        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        // TODO 正态分布检测
        return true;
    }
}
