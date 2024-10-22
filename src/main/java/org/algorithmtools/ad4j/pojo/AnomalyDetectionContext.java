package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.config.ADMConfigs;
import org.algorithmtools.ad4j.config.ConfigOption;
import org.algorithmtools.ad4j.enumtype.AnomalyDictType;

import java.io.Serializable;
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
        admConfigMap.put(ADMConfigs.configKeyMap.get(key), value);
    }

    public void putConfig(ConfigOption configOption, Object value){
        admConfigMap.put(configOption, value);
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
        for (Map.Entry<String, ConfigOption> entry : ADMConfigs.configKeyMap.entrySet()) {
            anomalyDetectionContext.putConfig(entry.getValue(), entry.getValue().getDefaultValue());
        }
        return anomalyDetectionContext;
    }


}
