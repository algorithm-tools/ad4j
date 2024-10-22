package org.algorithmtools.ad4j.enumtype;

public enum ThresholdType {

    /**
     * constant
     */
    CONSTANT,

    /**
     * standard deviation
     */
    STD,

    /**
     * variance
     */
    VAR,

    /**
     * mean
     */
    MEAN,

    /**
     * min
     */
    MIN,

    /**
     * max
     */
    MAX,
    /**
     * quantile
     */
    QUANTILE
    ;

    public static ThresholdType parse(String signal){
        if("constant".equalsIgnoreCase(signal)){
            return CONSTANT;
        }
        else if("std".equalsIgnoreCase(signal)){
            return STD;
        }
        else if("var".equalsIgnoreCase(signal)){
            return VAR;
        }
        else if("mean".equalsIgnoreCase(signal)){
            return MEAN;
        }
        else if("min".equalsIgnoreCase(signal)){
            return MIN;
        }
        else if("max".equalsIgnoreCase(signal)){
            return MAX;
        }
        else if("quantile".equalsIgnoreCase(signal)){
            return QUANTILE;
        }

        throw new IllegalArgumentException("["+signal+"] signal illegal!");
    }

}
