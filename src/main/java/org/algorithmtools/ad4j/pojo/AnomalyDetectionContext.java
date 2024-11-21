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
        admConfigMap.put(configOption,
                (value instanceof BigDecimal || value instanceof Double || value instanceof Float || value instanceof Integer) ? value.toString() : value);
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


}
