package org.algorithmtools.ad4j.utils;

import java.util.Arrays;

public class BandwidthUtil {

    /**
     * calculate bandwidth with Silverman's Rule of Thumb
     * @param data data
     * @return bandwidth h
     */
    public static double calculateBandwidth(double[] data) {
        int n = data.length;

        // calculate std
        double mean = Arrays.stream(data).average().orElse(0.0);
        double variance = Arrays.stream(data)
                .map(x -> (x - mean) * (x - mean))
                .sum() / n;
        double stdDev = Math.sqrt(variance);

        // calculate IQR
        Arrays.sort(data);
        double q1 = getPercentile(data, 25); // 25%
        double q3 = getPercentile(data, 75); // 75%
        double iqr = q3 - q1; // IQR = Q3 - Q1

        // calculate h
        double factor = Math.min(stdDev, iqr / 1.34);
        return 0.9 * factor * Math.pow(n, -1.0 / 5.0);
    }

    /**
     * Calculate array percentile value
     * @param data after sorted array
     * @param percentile percentile（0-100）
     * @return percentile value
     */
    private static double getPercentile(double[] data, double percentile) {
        double index = (percentile / 100.0) * (data.length - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);
        if (lower == upper) {
            return data[lower];
        }
        double weight = index - lower;
        return data[lower] * (1 - weight) + data[upper] * weight;
    }

    public static void main(String[] args) {
//        double[] data = {1.2, 1.5, 2.0, 2.1, 3.3, 3.9, 4.8, 10.0, 10.5, 12.0};
//        double[] data = {10.0, 12.0, 85.0, 70.0, 100.0, 14.0, 14.0, 12.0, 40.0, 20.0};
//        double[] data = {100, 102, 101, 180, 105, 108, 182, 110, 110,182,121};
        double[] data = {45.29, 30.85, 40.23, 15.57, 13.14, 32.53, 44.34, 33.92, 25.31, 31.12, 33.23, 40.65, 32.88, 31.14};
        double bandwidth = calculateBandwidth(data);
        System.out.printf("Optimal Bandwidth: %.4f%n", bandwidth);
    }

}

