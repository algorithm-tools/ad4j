package org.algorithmtools.ad4j.enumtype;

public enum AnomalyDictType {

    //=================MODEL======================//

    /** Quantile-based absolute value monitoring*/
    MODEL_ADM_Quantile("ADM_Quantile", "ADM_Quantile", "基于分位值的离群点监测"),
    /** GESD-based absolute value detection */
    MODEL_ADM_GESD("ADM_GESD", "ADM_GESD", "基于GESD的离群值检测"),
    /** Volatility-based volatility anomaly detection */
    MODEL_ADM_2ndDerivationMBP("ADM_2ndDerivationMBP", "ADM_2ndDerivationMBP", "基于波动率的波动异常检测"),
    /** Mann-Kendall based trend anomaly detection */
    MODEL_ADM_ManKendall("ADM_ManKendall", "ADM_ManKendall", "基于Mann-Kendall的趋势异常检测"),
    /** Threshold-based anomaly detection */
    MODEL_ADM_Threshold("ADM_Threshold", "ADM_Threshold", "基于阈值的异常检测"),
    /** Z-Score anomaly detection */
    MODEL_ADM_ZScore("ADM_ZScore", "ADM_ZScore", "基于Z-score的异常检测"),


    //=================INFLUENCE======================//
    /** Influence Positive */
    INFLUENCE_POSITIVE("1", "Influence Positive", "正向影响"),
    /** Influence Negative */
    INFLUENCE_NEGATIVE("-1", "Influence Negative", "负向影响"),


    //=================TREND======================//
    /** Trend No Obvious */
    TREND_NO_OBVIOUS("0", "Trend No Obvious", "无明显趋势"),

    /** Trend Up */
    TREND_UP("1", "Trend Up", "向上趋势"),
    /** Trend Down */
    TREND_DOWN("-1", "Trend Down", "向下趋势"),


    //=================TYPE======================//

    /** Outliers Anomaly */
    TYPE_OUTLIERS_VALUE("1", "Outliers Anomaly", "绝对值异常"),
    /** Fluctuation Anomaly */
    TYPE_FLUCTUATION("2", "Fluctuation Anomaly", "波动异常"),
    /** Trend Anomaly */
    TYPE_TREND("3", "Trend Anomaly", "趋势异常"),
    /** Threshold Anomaly */
    TYPE_THRESHOLD("10", "Threshold Anomaly", "阈值异常"),


    ;

    AnomalyDictType(String code, String enName, String zhName) {
        this.code = code;
        this.enName = enName;
        this.zhName = zhName;
    }

    private String code;
    private String enName;
    private String zhName;

    public String getCode() {
        return code;
    }

    public String getEnName() {
        return enName;
    }

    public String getZhName() {
        return zhName;
    }
}
