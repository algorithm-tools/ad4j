package org.algorithm.ad4j.model.adm;

import org.algorithm.ad4j.constant.AnomalyConstant;
import org.algorithm.ad4j.pojo.AnomalyDetectionContext;
import org.algorithm.ad4j.pojo.AnomalyDetectionLog;
import org.algorithm.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithm.ad4j.pojo.IndicatorSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Anomaly detection model: Based on abnormal detection of volatility fluctuations
 * @author mym
 */
public class ADM_2ndDerivationMBP extends AbstractADM {

    /**
     * min point size
     */
    private int minPoints;
    /**
     * 2nd derivation threshold, filter then get significance anomaly candidate points.
     */
    private double threshold;

    public ADM_2ndDerivationMBP() {
        super(AnomalyConstant.MODEL_ADM_2ndDerivationMBP, AnomalyConstant.TYPE_FLUCTUATION);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.minPoints = 3;
        this.threshold = 0.9; // TODO 动态
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        // get x,y
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (int i = 0; i < indicatorSeries.size(); i++) {
            x.add(i, (double)i);
            y.add(i, indicatorSeries.get(i).getValue());
        }
        List<Double> secondDerivative = calculateSecondDerivative(y, x);
        List<Integer> candidatePoints = findCandidatePoints(secondDerivative, threshold);
        int maxBendingPointIndex = findMaxBendingPoint(y, candidatePoints);

        boolean hasAnomaly = maxBendingPointIndex >= 0; // TODO 不一定都有异常，阈值要调整

        // build evaluate info
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (hasAnomaly) {
            List<IndicatorSeries> mbpSeries = new ArrayList<>();
            mbpSeries.add(indicatorSeries.get(maxBendingPointIndex));

            result.setHasAnomaly(true);
            result.setNormalRangeMin(0d);
            result.setNormalRangeMax(0d);
            result.setSeriesList(mbpSeries);
            return result;
        }
        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        if (indicatorSeries.size() < minPoints) {
            log.warn(this.anomalyDetectionModel + ": indicator series size less then " + minPoints);
            return false;
        }
        return true;
    }

    // Calculate the first derivative
    public List<Double> calculateFirstDerivative(List<Double> data, List<Double> x) {
        List<Double> firstDerivative = new ArrayList<>();
        for (int i = 1; i < data.size() - 1; i++) {
            double derivative = (data.get(i + 1) - data.get(i - 1)) / (x.get(i + 1) - x.get(i - 1));
            firstDerivative.add(derivative);
        }
        return firstDerivative;
    }

    // Calculate the second derivative
    public List<Double> calculateSecondDerivative(List<Double> data, List<Double> x) {
        List<Double> secondDerivative = new ArrayList<>();
        for (int i = 1; i < data.size() - 1; i++) {
            double derivative = (data.get(i + 1) - 2 * data.get(i) + data.get(i - 1))
                    / Math.pow(x.get(i + 1) - x.get(i), 2);
            secondDerivative.add(derivative);
        }
        return secondDerivative;
    }

    // Filter candidate inflection points based on threshold
    public static List<Integer> findCandidatePoints(List<Double> secondDerivative, double threshold) {
        List<Integer> candidatePoints = new ArrayList<>();
        for (int i = 0; i < secondDerivative.size(); i++) {
            if (Math.abs(secondDerivative.get(i)) > threshold) {
                candidatePoints.add(i + 1);  // i + 1 is data index
            }
        }
        return candidatePoints;
    }

    // Calculate the distance between the candidate inflection point and the two endpoints of the curve
    public static int findMaxBendingPoint(List<Double> data, List<Integer> candidatePoints) {
        if (candidatePoints.isEmpty()) {
            return -1;
        }
        double maxDistance = -1;
        int maxBendingPointIndex = -1;
        for (int idx : candidatePoints) {
            double distance = Math.abs(data.get(idx) - data.get(0)) + Math.abs(data.get(idx) - data.get(data.size() - 1));
            if (distance > maxDistance) {
                maxDistance = distance;
                maxBendingPointIndex = idx;
            }
        }
        return maxBendingPointIndex;
    }

}
