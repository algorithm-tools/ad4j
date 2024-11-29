package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.CollectionUtil;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;

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
        super(AnomalyDictType.MODEL_ADM_Quantile, AnomalyDictType.TYPE_OUTLIERS_VALUE);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.iqrMultiplier = Double.parseDouble((String) context.getConfig(ADMConfigs.ADM_QUANTILE_IQR_MULTIPLIER));
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> data, AnomalyDetectionLog log) {
        // calculate bound
        double[] quantileBound = IndicatorCalculateUtil.quantileBound(data, iqrMultiplier, 0.25, 0.75);
        double lowerBound = quantileBound[0];
        double upperBound = quantileBound[1];

        // find anomaly indicator series
        List<AnomalyIndicatorSeries> anomalyList = data.stream()
                .filter(v -> v.getValue() > upperBound || v.getValue() < lowerBound)
                .map(v -> new AnomalyIndicatorSeries(v.getValue() > upperBound ? AnomalyDictType.INFLUENCE_POSITIVE : AnomalyDictType.INFLUENCE_NEGATIVE, v))
                .collect(Collectors.toList());

        // build evaluate info
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (CollectionUtil.isNotEmpty(anomalyList)) {
            result.setHasAnomaly(true);
            result.setNormalRangeMin(lowerBound);
            result.setNormalRangeMax(upperBound);
            result.setAnomalySeriesList(anomalyList);
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

        if (lowerIndex == upperIndex) {
            return sortedData.get(lowerIndex).getValue();
        }

        double weight = index - lowerIndex;
        return (1 - weight) * sortedData.get(lowerIndex).getValue() + weight * sortedData.get(upperIndex).getValue();
    }

}


