package org.algorithmtools.ad4j.engine;

import org.algorithmtools.ad4j.enumtype.AnomalyDictType;
import org.algorithmtools.ad4j.model.adm.*;
import org.algorithmtools.ad4j.pojo.*;
import org.algorithmtools.ad4j.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnomalyDetectionEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnomalyDetectionEngine.class);

    private final LinkedHashMap<AnomalyDictType, AbstractADM> admMap = new LinkedHashMap<>();

    private AnomalyDetectionContext context;

    public AnomalyDetectionEngine() {
        this(null);
    }

    public AnomalyDetectionEngine(AnomalyDetectionContext context) {
        this.context = context == null ? new AnomalyDetectionContext() : context; // TODO default context build

        AbstractADM adm = new ADM_GESD();
        admMap.put(adm.getAnomalyDetectionModel(), adm);
        adm = new ADM_Quantile();
        admMap.put(adm.getAnomalyDetectionModel(), adm);
        adm = new ADM_ZScore();
        admMap.put(adm.getAnomalyDetectionModel(), adm);
        adm = new ADM_2ndDerivationMBP();
        admMap.put(adm.getAnomalyDetectionModel(), adm);
        adm = new ADM_MannKendall();
        admMap.put(adm.getAnomalyDetectionModel(), adm);

        initADM(this.context);
    }

    public void initADM(AnomalyDetectionContext context) {
        admMap.values().forEach(v -> v.init(context));
    }

    public AnomalyDetectionResult detect(IndicatorInfo indicatorInfo) {
        if (CollectionUtil.isEmpty(indicatorInfo.getIndicatorSeries())) {
            return null;
        }

        AnomalyDetectionResult detectionResult = new AnomalyDetectionResult(indicatorInfo);
        for (Map.Entry<AnomalyDictType, AbstractADM> admEntry : admMap.entrySet()) {
            LOGGER.info("anomaly detect, [{}] processing.", admEntry.getKey());
            IndicatorEvaluateInfo evaluateResult = evaluate(admEntry.getValue(), indicatorInfo.getIndicatorSeries());
            if(evaluateResult.isHasAnomaly()){
                detectionResult.addIndicatorEvaluateInfo(evaluateResult.getAnomalyType(), evaluateResult);
            }
            LOGGER.info("anomaly detect, [{}] process over.", admEntry.getKey());
        }

        return detectionResult;
    }

    private IndicatorEvaluateInfo evaluate(AbstractADM adm, List<IndicatorSeries> seriesList) {
        AnomalyDetectionLog log = new AnomalyDetectionLog();
        if (!adm.checkCompatibility(seriesList, log)) {
            log.info("anomaly detect,[{}] indicator not compatibility.");
            return null;
        }

        IndicatorEvaluateInfo evaluate = adm.evaluate(seriesList, log);
        log.print();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("anomaly detect, [{}] evaluate result:{}", adm.getAnomalyDetectionModel(), evaluate);
        }

        return evaluate;
    }

}
