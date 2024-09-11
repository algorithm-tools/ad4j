package org.algorithm.ad4j.constant;


/**
 * AnomalyConstant
 * @author mym
 */
public class AnomalyConstant {
    //=================MODEL======================//

    /** 基于Quantitle的绝对值监测 */
    public static final String MODEL_ADM_Quantile = "ADM_Quantile";
    /** 基于GESD的绝对值检测 */
    public static final String MODEL_ADM_GESD = "ADM_GESD";
    /** 基于波动率波动异常检测 */
    public static final String MODEL_ADM_2ndDerivationMBP = "ADM_2ndDerivationMBP";
    /** 基于Man-Kendall趋势异常检测 */
    public static final String MODEL_ADM_ManKendall = "ADM_ManKendall";
    /** 基于阈值比较的异常检测 */
    public static final String MODEL_ADM_Threshold = "ADM_Threshold";


    //=================INFLUENCE======================//
    /** 正向 */
    public static final int INFLUENCE_POSITIVE = 1;
    /** 负向 */
    public static final int INFLUENCE_NEGATIVE = 2;


    //=================TREND======================//
    /** 无明显趋势 */
    public static final int TREND_NO = 0;
    /** 向上趋势 */
    public static final int TREND_UP = 1;
    /** 向下趋势 */
    public static final int TREND_DOWN = -1;


    //=================TYPE======================//

    /** 绝对值异常 */
    public static final int TYPE_ABSOLUTE_VALUE = 1;
    /** 波动异常 */
    public static final int TYPE_FLUCTUATION = 2;
    /** 趋势异常 */
    public static final int TYPE_TREND = 3;
    /** 阈值异常 */
    public static final int TYPE_THRESHOLD = 10;


}
