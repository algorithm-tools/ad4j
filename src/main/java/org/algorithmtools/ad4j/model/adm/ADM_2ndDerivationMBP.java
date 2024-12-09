package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.BandwidthUtil;
import org.algorithmtools.ad4j.utils.CollectionUtil;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;
import smile.stat.distribution.KernelDensity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private double thresholdFactor;

    public ADM_2ndDerivationMBP() {
        super(AnomalyDictType.MODEL_ADM_2ndDerivationMBP, AnomalyDictType.TYPE_FLUCTUATION);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.minPoints = 3;
        this.thresholdFactor = Double.parseDouble((String)context.getConfig(ADMConfigs.ADM_2ED_DERIVATION_MBP_THRESHOLD_FACTOR));
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        List<IndicatorSeries> anomalyList = evaluateWithVolatilityThreshold(indicatorSeries);

        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (CollectionUtil.isNotEmpty(anomalyList)) {
            double mean = indicatorSeries.stream().mapToDouble(IndicatorSeries::getValue).sum() / indicatorSeries.size();
            List<AnomalyIndicatorSeries> mbpSeries = anomalyList.stream()
                    .map(v -> new AnomalyIndicatorSeries(v.getValue() > mean ? AnomalyDictType.INFLUENCE_POSITIVE : AnomalyDictType.INFLUENCE_NEGATIVE, v))
                    .collect(Collectors.toList());
            result.setHasAnomaly(true);
            result.setAnomalySeriesList(mbpSeries);
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

    /** Through volatility threshold evaluate anomaly points
     * @param data
     * @return {@link List}<{@link IndicatorSeries}>
     */
    private List<IndicatorSeries> evaluateWithVolatilityThreshold(List<IndicatorSeries> data) {
        // volatility
        double[] volatility = calculateVolatility(data.stream().mapToDouble(IndicatorSeries::getValue).toArray());

        // volatility to distribution points
        double[][] distributionPoints = transferToDistributionPoints(volatility, 100);
        double[] x = distributionPoints[0];
        double[] y = distributionPoints[1];

        // secondDerivatives
        double[] secondDerivatives = calculateSecondDerivatives(x, y);

        // find inflection point from 0 axis left and right, as min and max volatility threshold
        int[] inflectionPoints = calculateVolatilityThreshold(secondDerivatives, x, y);
        double minBound = x[inflectionPoints[0]];
        double maxBound = x[inflectionPoints[1]];

        // lowerBound-upperBound filter
        List<IndicatorSeries> anomalyList = new ArrayList<>();
        for (int i = 0; i < volatility.length; i++) {
            if(volatility[i] > maxBound || volatility[i] < minBound){
                anomalyList.add(data.get(i));
            }
        }
        return anomalyList;
    }

    /** Through volatility quantile to evaluate anomaly points
     * @param data
     * @return {@link List}<{@link IndicatorSeries}>
     */
    private List<IndicatorSeries> evaluateWithQuantile(List<IndicatorSeries> data) {
        double[] volatility = calculateVolatility(data.stream().mapToDouble(IndicatorSeries::getValue).toArray());
        // lowerBound-upperBound filter
        double[] quantileIQR = IndicatorCalculateUtil.quantileBound(IndicatorSeriesUtil.transferFromArray(volatility), this.thresholdFactor, 0.25, 0.75);
        double lowerBound = quantileIQR[0];
        double upperBound = quantileIQR[1];
        List<IndicatorSeries> anomalyList = new ArrayList<>();
        for (int i = 0; i < volatility.length; i++) {
            if(volatility[i] > upperBound || volatility[i] < lowerBound){
                anomalyList.add(data.get(i));
            }
        }
        return anomalyList;
    }

    public double[] calculateVolatility(double[] data) {
        double[] volatility = new double[data.length];
        volatility[0] = 0;
        for (int i = 1; i < data.length; i++) {
            volatility[i] = (data[i] - data[i - 1]) / data[i - 1];
        }
        return volatility;
    }

    public double[][] transferToDistributionPoints(double[] data, int points) {
        double[] copyData = Arrays.copyOf(data, data.length);
        KernelDensity volatilityDistribution = new KernelDensity(copyData, BandwidthUtil.calculateBandwidth(copyData));

        double min = Arrays.stream(copyData).min().orElse(0.0);
        double max = Arrays.stream(copyData).max().orElse(1.0);
        double step = (max - min) / points;

        double[] x = new double[points];
        double[] y = new double[points];
        for (int i = 0; i < points; i++) {
            x[i] = min + i * step;
            y[i] = volatilityDistribution.p(x[i]); // KDE estimate
        }

        return new double[][]{x, y};
    }

    public double[] calculateSecondDerivatives(double[] x, double[] y) {
        int n = x.length;
        if (n < 3) {
            throw new IllegalArgumentException("At least three points are required to compute second derivatives.");
        }

        double h;
        double[] secondDerivatives = new double[n];
        // Compute second derivatives
        for (int i = 0; i < n; i++) {
            h = x[1] - x[0];
            if (i == 0) {
                // Left boundary
                secondDerivatives[i] = (y[i + 2] - 2 * y[i + 1] + y[i]) / (h * h);
            } else if (i == n - 1) {
                // Right boundary
                secondDerivatives[i] = (y[i] - 2 * y[i - 1] + y[i - 2]) / (h * h);
            } else {
                // Internal points
                secondDerivatives[i] = (y[i + 1] - 2 * y[i] + y[i - 1]) / (h * h);
            }
        }

        return secondDerivatives;
    }

    // find inflection point from 0 axis left and right, as min and max volatility threshold
    public static int[] calculateVolatilityThreshold(double[] secondDerivative, double[] distributionX, double[] distributionY) {
        // MBP is max bend point
        int xAxisLeftMBPIndex = 0;
        double xAxisLeftMBPMaxDistance = -1;
        int xAxisRightMBPIndex = distributionX.length - 1;
        double xAxisRightMBPMaxDistance = -1;
        for (int i = 1; i < secondDerivative.length; i++) {
            // A change in sign could be an inflection point
            if (secondDerivative[i - 1] * secondDerivative[i] < 0) {
                double distance = Math.sqrt(Math.pow(distributionX[i], 2) + Math.pow(distributionY[i], 2));
                if(distributionX[i] >= 0){
                    // zero axis right
                    if(distance >= xAxisRightMBPMaxDistance){
                        xAxisRightMBPIndex = i;
                        xAxisRightMBPMaxDistance = distance;
                    }
                } else {
                    // zero axis left
                    if(distance >= xAxisLeftMBPMaxDistance){
                        xAxisLeftMBPIndex = i;
                        xAxisLeftMBPMaxDistance = distance;
                    }
                }
            }
        }

        return new int[]{xAxisLeftMBPIndex, xAxisRightMBPIndex};
    }

}
