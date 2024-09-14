package org.algorithmtools.ad4j.utils;

import com.sun.istack.internal.NotNull;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IndicatorCalculateUtil {

    /**
     * calculate define range
     * @param data indicator series
     * @param iqrMultiplier iqrMultiplier
     * @param lowerQuantile lowerQuantile
     * @param upperQuantile upperQuantile
     * @return [lowerBound, upperBound]
     */
    public static double[] quantileBound(@NotNull List<IndicatorSeries> data, double iqrMultiplier, double lowerQuantile, double upperQuantile){
        // sort
        List<IndicatorSeries> sortList = data.stream().sorted(new IndicatorSeriesComparator()).collect(Collectors.toList());

        // calculate quantile value
        double Q1 = quantile(sortList, lowerQuantile);
        double Q3 = quantile(sortList, upperQuantile);

        // calculate IQR
        double IQR = Q3 - Q1;

        // calculate bound
        double lowerBound = Q1 - iqrMultiplier * IQR;
        double upperBound = Q3 + iqrMultiplier * IQR;
        return new double[]{lowerBound, upperBound};
    }

    /**
     * interquartile range
     * @param data indicator series
     * @return [lowerBound, upperBound]
     */
    public static double[] quantileIQR(@NotNull List<IndicatorSeries> data){
        return quantileBound(data, 1.5, 0.25, 0.75);
    }

    public static List<IndicatorSeries> excludeOutlier(@NotNull List<IndicatorSeries> data){
        double[] bound = quantileIQR(data);
        double lowerBound = bound[0];
        double upperBound = bound[1];

        // find normal series
        return data.stream().filter(v -> v.getValue() <= upperBound && v.getValue() >= lowerBound).collect(Collectors.toList());
    }

    private static double quantile(List<IndicatorSeries> sortedData, double quantile) {
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

    public static DescriptiveStatistics initStatistic(DescriptiveStatistics stats, List<IndicatorSeries> dataList, List<Integer> excludeIndexList) {
        if (Objects.isNull(stats)) {
            stats = new DescriptiveStatistics();
        } else {
            stats.clear();
        }

        for (int i = 0; i < dataList.size(); i++) {
            if (excludeIndexList == null || !excludeIndexList.contains(i)) {
                stats.addValue(dataList.get(i).getValue());
            }
        }
        return stats;
    }
}
