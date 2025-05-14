package org.algorithmtools.ad4j.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.config.ConfigOption;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.enumtype.CompareType;
import org.algorithmtools.ad4j.enumtype.LogicType;
import org.algorithmtools.ad4j.enumtype.ThresholdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class AnomalyDetectionContext implements Serializable {

    private List<AnomalyDictType> configAnomalyDetectionModelList = new ArrayList<>();

    public List<AnomalyDictType> getConfigAnomalyDetectionModelList() {
        return configAnomalyDetectionModelList;
    }

    public void addConfigAnomalyDetectionModel(AnomalyDictType anomalyDictType) {
        if (this.configAnomalyDetectionModelList == null){
            this.configAnomalyDetectionModelList = new ArrayList<>();
        }

        this.getConfigAnomalyDetectionModelList().add(anomalyDictType);
    }

    private Map<ConfigOption, Object> admConfigMap = new HashMap<>();

    public void putConfig(String key, Object value){
        if (!ADMConfigs.configKeyMap.containsKey(key)) {
            throw new IllegalArgumentException("["+key+"] not exist!");
        }
        putConfig(ADMConfigs.configKeyMap.get(key), value);
    }

    public void putConfig(ConfigOption configOption, Object value){
        if(value != null && value.toString().endsWith("s")){
            // end with "s", then sensitivityConvert
            String sensitivityValue = value.toString();
            admConfigMap.put(configOption,sensitivityConvert(configOption, sensitivityValue.substring(0, sensitivityValue.length() - 1)));
        } else {
            admConfigMap.put(configOption,
                    (value instanceof BigDecimal || value instanceof Double || value instanceof Float || value instanceof Integer) ? value.toString() : value);
        }
    }

    public Object getConfig(String key){
        if (ADMConfigs.configKeyMap.containsKey(key)) {
            return getConfig(ADMConfigs.configKeyMap.get(key));
        } else {
            return null;
        }
    }

    public Object getConfig(ConfigOption configOption){
        return Optional.ofNullable(admConfigMap.get(configOption)).orElseGet(configOption::getDefaultValue);
    }

    public static AnomalyDetectionContext createDefault(){
        AnomalyDetectionContext anomalyDetectionContext = new AnomalyDetectionContext();
        defaultConfig(anomalyDetectionContext);
        return anomalyDetectionContext;
    }

    public static AnomalyDetectionContext create(JSONObject propertiesJson){
        AnomalyDetectionContext anomalyDetectionContext = new AnomalyDetectionContext();
        defaultConfig(anomalyDetectionContext);

        if(Objects.nonNull(propertiesJson)){
            for (Map.Entry<String, Object> e : propertiesJson.entrySet()) {
                if (e.getKey().equalsIgnoreCase(ADMConfigs.ADM_THRESHOLD_RULE_SET.getKey())) {
                    anomalyDetectionContext.putConfig(e.getKey(), generalThresholdRule((JSONObject) e.getValue()));
                } else {
                    anomalyDetectionContext.putConfig(e.getKey(), e.getValue());
                }
            }
        }

        return anomalyDetectionContext;
    }

    /**
     * data case:{"logicType":"and","ruleGroup":[{"factor":100,"thresholdType":"std","compareType":">"}]}
     * @param thresholdRuleSetJson adm.threshold_rule.set config json
     * @return ThresholdRuleBase
     */
    public static ThresholdRuleBase generalThresholdRule(JSONObject thresholdRuleSetJson) {
        if (Objects.isNull(thresholdRuleSetJson)) {
            return null;
        }

        if (thresholdRuleSetJson.containsKey("ruleGroup")) {
            ThresholdRuleGroup ruleGroup = new ThresholdRuleGroup(LogicType.parse(thresholdRuleSetJson.getString("logicType")));
            JSONArray jsonArr = thresholdRuleSetJson.getJSONArray("ruleGroup");
            for (int i = 0; i < jsonArr.size(); i++) {
                ThresholdRuleBase rb = generalThresholdRule(jsonArr.getJSONObject(i));
                if(Objects.nonNull(rb)){
                    ruleGroup.addRules(rb);
                }
            }
            return ruleGroup;
        } else {
            return new ThresholdRule(ThresholdType.parse(thresholdRuleSetJson.getString("thresholdType")), CompareType.parse(thresholdRuleSetJson.getString("compareType")), thresholdRuleSetJson.getDouble("factor"));
        }
    }

    private static void defaultConfig(AnomalyDetectionContext anomalyDetectionContext) {
        for (Map.Entry<String, ConfigOption> entry : ADMConfigs.configKeyMap.entrySet()) {
            anomalyDetectionContext.putConfig(entry.getValue(), entry.getValue().getDefaultValue());
        }
    }

    /**
     * Some anomaly model support the specification of configuration values in the form of sensitivities. If not support, return config default value.
     *
     * @param config
     * @param sensitivity 0.1-> low sensitivity; 0.05-> normal sensitivity; 0.01-> high sensitivity
     * @return config value
     */
    public static String sensitivityConvert(ConfigOption config, String sensitivity) {
        if (config == null) {
            return null;
        }
        if (config.getKey().equals(ADMConfigs.ADM_QUANTILE_IQR_MULTIPLIER.getKey())) {
            if ("0.1".equals(sensitivity)) {
                return "3.0"; // confidence level 90% (more bigger then detection more less)
            } else if ("0.05".equals(sensitivity)) {
                return "1.5"; // confidence level 95%
            } else if ("0.01".equals(sensitivity)) {
                return "1.0"; // confidence level 99%
            }
        }
        if (config.getKey().equals(ADMConfigs.ADM_ZSCORE_THRESHOLD.getKey())) {
            if ("0.1".equals(sensitivity)) {
                return "1.5"; // confidence level 86.6% (more bigger then detection more less)
            } else if ("0.05".equals(sensitivity)) {
                return "2.0"; // confidence level 95%
            } else if ("0.01".equals(sensitivity)) {
                return "3.0"; // confidence level 99.7%
            }
        }
        if (config.getKey().equals(ADMConfigs.ADM_GESD_ALPHA.getKey())) {
            if ("0.1".equals(sensitivity)) {
                return "0.1"; // confidence level 90% (more bigger then detection more less)
            } else if ("0.05".equals(sensitivity)) {
                return "0.05"; // confidence level 95%
            } else if ("0.01".equals(sensitivity)) {
                return "0.01"; // confidence level 99%
            }
        }
        if (config.getKey().equals(ADMConfigs.ADM_2ED_DERIVATION_MBP_THRESHOLD_FACTOR.getKey())) {
            // only support when ADM_2ED_DERIVATION_MBP_EVALUATE_TYPE = 1
            if ("0.1".equals(sensitivity)) {
                return "3.0"; // fitting 99.7%(more bigger then detection more less)
            } else if ("0.05".equals(sensitivity)) {
                return "2.0"; // fitting 95%
            } else if ("0.01".equals(sensitivity)) {
                return "1.5"; // fitting 86.6%
            }
        }
        if (config.getKey().equals(ADMConfigs.ADM_MANNKENDALL_CRITICALZ.getKey())) {
            if ("0.1".equals(sensitivity)) {
                return "1.64"; // confidence level 90% (more bigger then detection more less)
            } else if ("0.05".equals(sensitivity)) {
                return "1.96"; // confidence level 95%
            } else if ("0.01".equals(sensitivity)) {
                return "2.58"; // confidence level 99%
            }
        }

        return String.valueOf(config.getDefaultValue());
    }


}
