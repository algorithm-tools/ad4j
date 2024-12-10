package org.algorithmtools.ad4j.model;

import org.algorithmtools.ad4j.utils.BandwidthUtil;
import org.algorithmtools.ad4j.utils.IndicatorSeriesUtil;
import org.algorithmtools.chart.JFreeChartUtil;
import smile.stat.distribution.KernelDensity;

import java.util.*;

/**
 * Test Volatility Anomaly
 */
public class VolatilityAnalysisTest {

    public static void main(String[] args) {
//        double[] data = {10.0, 12.0, 85.0, 70.0, 100.0, 14.0, 14.0, 12.0, 40.0, 20.0};
//        double[] data = {45.29, 30.85, 40.23, 15.57, 13.14, 32.53, 44.34, 33.92, 25.31, 31.12, 33.23, 40.65, 32.88, 31.14};
        double[] data = {1104.0, 976.0, 949.0, 895.0, 810.0, 975.0, 1152.0, 818.0, 766.0, 502.0, 396.0, 468.0, 592.0, 769.0};
        JFreeChartUtil.drawLineChart("data", IndicatorSeriesUtil.transferFromArray(data));

        // volatility
        double[] volatility = calculateVolatility(data);
        JFreeChartUtil.drawLineChart("volatility", IndicatorSeriesUtil.transferFromArray(volatility));

        // volatility to distribution points
        double[][] distributionPoints = transferToDistributionPoints(calculateVolatility(data), 100);
        double[] x = distributionPoints[0];
        double[] y = distributionPoints[1];
        JFreeChartUtil.drawLineChart("KDE", x, y);

        double[] secondDerivatives = calculateSecondDerivatives(x, y);
        JFreeChartUtil.drawLineChart("secondDerivative", x, secondDerivatives);

        // find MBP
        int[] inflectionPoints = findInflectionPoints(secondDerivatives, x, y);
        double minBound = x[inflectionPoints[0]];
        double maxBound = x[inflectionPoints[1]];
        System.out.println("0-axis left inflection point threshold:" + minBound + "\t 0-axis right inflection point threshold：" + maxBound);

        // check anomaly
        checkAnomalies(data, volatility, minBound, maxBound);
    }

    public static double[] calculateVolatility(double[] data) {
        double[] volatility = new double[data.length];
        volatility[0] = 0;
        for (int i = 1; i < data.length; i++) {
            volatility[i] = (data[i] - data[i - 1]) / data[i - 1];
        }
        return volatility;
    }

    public static double[][] transferToDistributionPoints(double[] data, int points) {
        KernelDensity volatilityDistribution = new KernelDensity(data, BandwidthUtil.calculateBandwidth(data) * 1);

        double min = Arrays.stream(data).min().orElse(0.0);
        double max = Arrays.stream(data).max().orElse(1.0);
        double step = (max - min) / points;

        double[] x = new double[points];
        double[] y = new double[points];
        for (int i = 0; i < points; i++) {
            x[i] = min + i * step;
            y[i] = volatilityDistribution.p(x[i]); // KDE estimate
        }

        return new double[][]{x, y};
    }

    public static double[] calculateSecondDerivatives(double[] x, double[] y) {
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


    // find max bend point
    public static int[] findInflectionPoints(double[] secondDerivative, double[] distributionX, double[] distributionY) {
        int zeroLeftInflectionPointIndex = 0;
        double zeroLeftInflectionPointMaxDistance = -1;
        int zeroRightInflectionPointIndex = distributionX.length - 1;
        double zeroRightInflectionPointMaxDistance = -1;
        for (int i = 1; i < secondDerivative.length; i++) {
            // A change in sign could be an inflection point
            if (secondDerivative[i - 1] * secondDerivative[i] < 0) {
                double distance = Math.sqrt(Math.pow(distributionX[i], 2) + Math.pow(distributionY[i], 2));
                if(distributionX[i] >= 0){
                    // zero right
                    if(distance >= zeroRightInflectionPointMaxDistance){
                        zeroRightInflectionPointIndex = i;
                        zeroRightInflectionPointMaxDistance = distance;
                    }
                } else {
                    // zero left
                    if(distance >= zeroLeftInflectionPointMaxDistance){
                        zeroLeftInflectionPointIndex = i;
                        zeroLeftInflectionPointMaxDistance = distance;
                    }
                }
            }
        }

        return new int[]{zeroLeftInflectionPointIndex, zeroRightInflectionPointIndex};
    }

    public static void checkAnomalies(double[] data, double[] volatility, double minBound, double maxBound) {
        Map<Integer, Double> anomalyList = new HashMap<>();
        int continualMaxIndex = -1;
        int continualLastIndex = -1;
        for (int i = 0; i < volatility.length; i++) {
            if(volatility[i] > maxBound || volatility[i] < minBound){
                // continual isotropic volatilises, select the largest
                if(continualLastIndex < 0){
                    continualMaxIndex = i;
                    continualLastIndex = i;
                } else if(i - 1 == continualLastIndex && volatility[i] * volatility[continualLastIndex] > 0){
                    if(Math.abs(volatility[i]) > Math.abs(volatility[continualMaxIndex])){
                        continualMaxIndex = i;
                    }
                    continualLastIndex = i;
                } else {
                    anomalyList.put(continualMaxIndex, data[continualMaxIndex]);
                    continualLastIndex = i;
                    continualMaxIndex = i;
                }
            }
        }
        if(continualMaxIndex >= 0){
            anomalyList.put(continualMaxIndex, data[continualMaxIndex]);
        }

        anomalyList.forEach((k,v) -> System.out.println("anomaly:" + k + " --> " + v));
    }
}
