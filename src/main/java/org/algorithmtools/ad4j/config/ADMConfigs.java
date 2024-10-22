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
    public static final ConfigOption<Double> ADM_QUANTILE_IQR_MULTIPLIER = new ConfigOption<>("adm.quantile.iqr.multiplier", 1.0, "Quantile detection IQR multiplier.");

    //==================ADM Z-score Options=================//
    public static final ConfigOption<Boolean> ADM_ZSCORE_USE = new ConfigOption<>("adm.zscore.use", true, "Open Z-score detection function.");
    public static final ConfigOption<Double> ADM_ZSCORE_THRESHOLD = new ConfigOption<>("adm.zscore.threshold", 2.0, "Z-score detection threshold.");

    //==================ADM GESD Options=================//
    public static final ConfigOption<Boolean> ADM_GESD_USE = new ConfigOption<>("adm.gesd.use", true, "Open GESD detection function.");
    public static final ConfigOption<Double> ADM_GESD_ALPHA = new ConfigOption<>("adm.gesd.alpha", 0.05, "GESD detection alpha. Indicate abnormal significance.");

    //==================ADM 2ndDerivationMBP Options=================//
    public static final ConfigOption<Boolean> ADM_2ED_DERIVATION_MBP_USE = new ConfigOption<>("adm.2nd_derivation_mbp.use", true, "Open 2ndDerivationMBP detection function.");
    public static final ConfigOption<Double> ADM_2ED_DERIVATION_MBP_THRESHOLD = new ConfigOption<>("adm.2nd_derivation_mbp.threshold", 0.9, "2nd derivation threshold, filter then get significance anomaly candidate points.");

    //==================ADM MannKendall Options=================//
    public static final ConfigOption<Boolean> ADM_MANNKENDALL_USE = new ConfigOption<>("adm.mannkendall.use", true, "Open 2ndDerivationMBP detection function.");
    public static final ConfigOption<Double> ADM_MANNKENDALL_CRITICALZ = new ConfigOption<>("adm.mannkendall.criticalz", 1.96, "The Z-critical value corresponding to significance level P=0.05 (refer to the standard normal distribution table).");

    //==================ADM Threshold-Rule Options=================//
    public static final ConfigOption<Boolean> ADM_THRESHOLD_RULE_USE = new ConfigOption<>("adm.threshold_rule.use", false, "Open threshold rule detection function.");
    public static final ConfigOption<ThresholdRuleBase> ADM_THRESHOLD_RULE_SET = new ConfigOption<>("adm.threshold_rule.set", null, "Threshold rule detection sets.");


    static {
        ADMConfigs admConfigs = new ADMConfigs();
        Arrays.stream(FieldUtils.getAllFields(ADMConfigs.class)).filter((f) -> {
            return ConfigOption.class.isAssignableFrom(f.getType());
        }).forEach((f) -> {
            try {
                ConfigOption co = (ConfigOption)f.get(admConfigs);
                configKeyMap.put(co.getKey(), co);
            } catch (IllegalAccessException var4) {
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(configKeyMap);
    }

}
