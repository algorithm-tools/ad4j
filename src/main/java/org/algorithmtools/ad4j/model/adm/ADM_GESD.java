package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionContext;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionLog;
import org.algorithmtools.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.algorithmtools.ad4j.utils.CollectionUtil;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Anomaly detection model: Outlier detection based on GESD
 * @author mym
 */
public class ADM_GESD extends AbstractADM {

    private double alpha;

    public ADM_GESD() {
        super(AnomalyDictType.MODEL_ADM_GESD, AnomalyDictType.TYPE_OUTLIERS_VALUE);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.alpha = 0.05;
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        List<Integer> gesdIndexList = gesd(indicatorSeries, indicatorSeries.size() / 2, alpha);

        // build evaluate info
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (CollectionUtil.isNotEmpty(gesdIndexList)) {
            result.setHasAnomaly(true);
            result.setNormalRangeMin(0d);
            result.setNormalRangeMax(0d);
            result.setSeriesList(gesdIndexList.stream().map(indicatorSeries::get).collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> seriesList, AnomalyDetectionLog log) {
        //todo 小数据量更优的检验方法
        if (seriesList.size() < 50) {
            return true;
        } else {
            //K-S检验
            double[] data = new double[seriesList.size()];
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (int i = 0; i < seriesList.size(); i++) {
                stats.addValue(seriesList.get(i).getValue());
                data[i] = seriesList.get(i).getValue();
            }
            double mean = stats.getMean();
            double standardDeviation = stats.getStandardDeviation();
            NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
            KolmogorovSmirnovTest ksTest = new KolmogorovSmirnovTest();
            return ksTest.kolmogorovSmirnovTest(normalDistribution, data, 0.05);
        }
    }

    protected List<Integer> gesd(List<IndicatorSeries> dataList, int maxOutliers, double alpha) {
        List<Integer> outlierIndexes = new ArrayList<>();
        List<IndicatorSeries> originalData = new ArrayList<>(dataList);

        DescriptiveStatistics stats = null;
        int n = dataList.size();
        int r = maxOutliers;
        for (int i = 1; i <= r; i++) { // i = 1,2,3...r
            // init statistic
            stats = IndicatorCalculateUtil.initStatistic(stats, originalData, outlierIndexes);
            // calculate mean and std
            double mean = stats.getMean();
            double stdDev = stats.getStandardDeviation();

            // calculate Max(Ri)
            double maxRi = -1;
            int maxRiIndex = -1;
            for (int j = 0; j < originalData.size(); j++) {
                if (outlierIndexes.contains(j)) {
                    continue;
                }

                double deviation = Math.abs(originalData.get(j).getValue() - mean);
                if (maxRi < deviation) {
                    maxRi = deviation;
                    maxRiIndex = j;
                }
            }

            // Lambda i
            double lambdaI = calculateLambdaI(n, i, alpha);

            // judge anomaly: where(Ri > LambdaI)
            if ((maxRi / stdDev) > lambdaI) {
                outlierIndexes.add(maxRiIndex);
            } else {
                break;
            }
        }

        return outlierIndexes;
    }

    // calculate lambda (critical-value)
    public double calculateLambdaI(int n, int i, double alpha) {
        TDistribution tDistribution = new TDistribution(n - i - 1);
        double p = 1 - alpha / (2 * (n - i + 1)); // both sid t-distribution
        double tValue = tDistribution.inverseCumulativeProbability(p); // Probability to T
        return (n - i) * tValue / Math.sqrt((n - i - 1 + Math.pow(tValue, 2)) * (n - i + 1));
    }

}
