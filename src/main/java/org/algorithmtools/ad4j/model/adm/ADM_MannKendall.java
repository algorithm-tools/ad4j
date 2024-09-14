package org.algorithmtools.ad4j.model.adm;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionContext;
import org.algorithmtools.ad4j.pojo.AnomalyDetectionLog;
import org.algorithmtools.ad4j.pojo.IndicatorEvaluateInfo;
import org.algorithmtools.ad4j.pojo.IndicatorSeries;
import org.algorithmtools.ad4j.utils.IndicatorCalculateUtil;

import java.util.List;
import java.util.Objects;

/**
 * Anomaly detection model: Based on Mann-Kendall trend anomaly detection
 * @author mym
 */
public class ADM_MannKendall extends AbstractADM {

    /** significance level P to Z-critical value */
    private double criticalZ;

    public ADM_MannKendall() {
        super(AnomalyDictType.MODEL_ADM_ManKendall, AnomalyDictType.TYPE_TREND);
    }

    @Override
    public void init(AnomalyDetectionContext context) {
        this.criticalZ = 1.96; // 显著性水平 P=0.05 对应的 Z 临界值（查标准正态分布表）
    }

    @Override
    public IndicatorEvaluateInfo evaluate(List<IndicatorSeries> data, AnomalyDetectionLog log) {
        List<IndicatorSeries> indicatorSeries = IndicatorCalculateUtil.excludeOutlier(data);

        MannKendallResult mannKendallResult = mannKendall(indicatorSeries, log);

        // build evaluate info
        IndicatorEvaluateInfo result = buildDefaultEvaluateInfo();
        if (!Objects.equals(mannKendallResult.getTrend(), AnomalyDictType.TREND_NO_OBVIOUS.getCode())) {
            result.setHasAnomaly(true);
            result.setNormalRangeMin(0d);
            result.setNormalRangeMax(0d);
            result.setAnomalyTrend(Objects.equals(mannKendallResult.getTrend(), AnomalyDictType.TREND_UP.getCode()) ? AnomalyDictType.TREND_UP :
                    AnomalyDictType.TREND_DOWN);
        }
        return result;
    }

    @Override
    public boolean checkCompatibility(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        return true;
    }

    private MannKendallResult mannKendall(List<IndicatorSeries> indicatorSeries, AnomalyDetectionLog log) {
        int n = indicatorSeries.size();

        // calculate S
        int S = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                S += Double.compare(indicatorSeries.get(j).getValue(), indicatorSeries.get(i).getValue());  // sign(X_j - X_i)
            }
        }

        // calculate Var(S)
        double varianceS = n * (n - 1) * (2 * n + 5) / 18.0;

        // calculate Z
        double Z;
        if (S > 0) {
            Z = (S - 1) / Math.sqrt(varianceS);
        } else if (S == 0) {
            Z = 0;
        } else {
            Z = (S + 1) / Math.sqrt(varianceS);
        }

        // judge trend with Z
        String trend;
        double alpha = criticalZ;
        if (Math.abs(Z) > alpha) {
            if (Z > 0) {
                trend = AnomalyDictType.TREND_UP.getCode();
            } else {
                trend = AnomalyDictType.TREND_DOWN.getCode();
            }
        } else {
            trend = AnomalyDictType.TREND_NO_OBVIOUS.getCode();
        }

        return new MannKendallResult(S, varianceS, Z, trend);
    }

    private class MannKendallResult {
        private final int S;
        private final double varianceS;
        private final double Z;
        private final String trend;

        public MannKendallResult(int S, double varianceS, double Z, String trend) {
            this.S = S;
            this.varianceS = varianceS;
            this.Z = Z;
            this.trend = trend;
        }

        public int getS() {
            return S;
        }

        public double getVarianceS() {
            return varianceS;
        }

        public double getZ() {
            return Z;
        }

        public String getTrend() {
            return trend;
        }
    }
}


