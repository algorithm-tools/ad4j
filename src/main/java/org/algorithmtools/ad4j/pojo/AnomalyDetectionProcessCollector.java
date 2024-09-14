package org.algorithmtools.ad4j.pojo;

import org.algorithmtools.ad4j.model.adm.AbstractADM;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class AnomalyDetectionProcessCollector {
    private AnomalyDetectionLog log;
    private AnomalyDetectionContext context;
    private LinkedHashMap<AbstractADM, AnomalyDetectionResult> processLinked;

    public AnomalyDetectionProcessCollector(AnomalyDetectionContext context) {
        this.log = new AnomalyDetectionLog();
        this.context = context;
        this.processLinked = new LinkedHashMap<>(8);
    }

    public void addProcess(AbstractADM anomalyDetection){
        this.processLinked.put(anomalyDetection, null);
    }

    public boolean hasAnomaly(){
        if(Objects.isNull(processLinked) || processLinked.isEmpty()){
            return false;
        }
        for (Map.Entry<AbstractADM, AnomalyDetectionResult> entry : processLinked.entrySet()) {
            if(entry.getValue() != null){
                return true;
            }
        }
        return false;
    }

    public AnomalyDetectionLog getLog() {
        return log;
    }

    public AnomalyDetectionContext getContext() {
        return context;
    }

    public LinkedHashMap<AbstractADM, AnomalyDetectionResult> getProcessLinked() {
        return processLinked;
    }
}
