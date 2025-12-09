package org.algorithmtools.ad4j.config;

import org.algorithmtools.ad4j.pojo.ThresholdRuleBase;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ADMConfigs {

    public static final Map<String, ConfigOption> configKeyMap = new HashMap<>();

    //==================ADM Quantile Options=================//
    public static final ConfigOption<Boolean> ADM_QUANTILE_USE = new ConfigOption<>("adm.quantile.use", true, "Open Quantile detection function.");
    public static final ConfigOption<Double> ADM_QUANTILE_IQR_MULTIPLIER = new ConfigOption<>("adm.quantile.iqr.multiplier", 1.5, "Quantile detection IQR multiplier. Support config with SensitivityType.");

    //==================ADM Z-score Options=================//
    public static final ConfigOption<Boolean> ADM_ZSCORE_USE = new ConfigOption<>("adm.zscore.use", true, "Open Z-score detection function.");
    public static final ConfigOption<Double> ADM_ZSCORE_THRESHOLD = new ConfigOption<>("adm.zscore.threshold", 2.0, "Z-score detection threshold.  Support config with SensitivityType.");

    //==================ADM GESD Options=================//
    public static final ConfigOption<Boolean> ADM_GESD_USE = new ConfigOption<>("adm.gesd.use", true, "Open GESD detection function.");
    public static final ConfigOption<Double> ADM_GESD_ALPHA = new ConfigOption<>("adm.gesd.alpha", 0.05, "GESD detection alpha. Indicate abnormal significance. Support config with SensitivityType.");

    //==================ADM 2ndDerivationMBP Options=================//
    public static final ConfigOption<Boolean> ADM_2ED_DERIVATION_MBP_USE = new ConfigOption<>("adm.2nd_derivation_mbp.use", true, "Open 2ndDerivationMBP detection function.");
    public static final ConfigOption<Double> ADM_2ED_DERIVATION_MBP_THRESHOLD_FACTOR = new ConfigOption<>("adm.2nd_derivation_mbp.threshold_factor", 1.0, "2nd derivation threshold factor, Regulates the robustness of the evaluation algorithm, the smaller it is, the less robust it is and the more sensitive it is.  Support config with SensitivityType.");
    public static final ConfigOption<Integer> ADM_2ED_DERIVATION_MBP_EVALUATE_TYPE = new ConfigOption<>("adm.2nd_derivation_mbp.evaluate_type", 1, "2nd derivation evaluate type, Evaluate algorithm types:" +
            "1. Distribution by volatility. That is, looking for anomalies based on the volatility distribution of the data" +
            ";2. By absolute volatility. That is, the point at which volatility outliers are found is the volatility point.1 finer and more sensitive, 2. more robust. Both can be adjusted with adm.2nd_derivation_mbp.threshold_factor");

    //==================ADM MannKendall Options=================//
    public static final ConfigOption<Boolean> ADM_MANNKENDALL_USE = new ConfigOption<>("adm.mannkendall.use", true, "Open 2ndDerivationMBP detection function.");
    /*  计算Z-critical值的代码如下
        from scipy.stats import norm
        # 设置显著性水平（默认α=0.05）0.05->1.9600；0.01-> 2.5758;0.1->1.6449
        alpha = 0.01
        # 计算标准正态分布的双侧临界值
        z_critical = norm.ppf(1 - alpha/2)
        print(f"显著性水平α={alpha}时，双侧检验的Z临界值为：{z_critical:.4f}")
    * */
    public static final ConfigOption<Double> ADM_MANNKENDALL_CRITICALZ = new ConfigOption<>("adm.mannkendall.criticalz", 1.96, "The Z-critical value corresponding to significance level P=0.05 (refer to the standard normal distribution table). Support config with SensitivityType.");

    //==================ADM Threshold-Rule Options=================//
    public static final ConfigOption<Boolean> ADM_THRESHOLD_RULE_USE = new ConfigOption<>("adm.threshold_rule.use", false, "Open threshold rule detection function.");
    public static final ConfigOption<ThresholdRuleBase> ADM_THRESHOLD_RULE_SET = new ConfigOption<>("adm.threshold_rule.set", null, "Threshold rule detection sets.");
    /*
     * Relation to the outer layer(GESD\Quantile\Z-score...)
     */
    public static final ConfigOption<String> ADM_THRESHOLD_RULE_OUTER_LAYER_LOGIC = new ConfigOption<>("adm.threshold_rule.outer_layer_logic", "OR", "Threshold rule with relation to the outer layer.");


    static {
        ADMConfigs admConfigs = new ADMConfigs();
        Arrays.stream(FieldUtils.getAllFields(ADMConfigs.class)).filter((f) -> {
            return ConfigOption.class.isAssignableFrom(f.getType());
        }).forEach((f) -> {
            try {
                ConfigOption co = (ConfigOption) f.get(admConfigs);
                configKeyMap.put(co.getKey(), co);
            } catch (IllegalAccessException var4) {
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(configKeyMap);
    }

}
