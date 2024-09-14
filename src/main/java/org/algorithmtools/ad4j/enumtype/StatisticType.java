package org.algorithmtools.ad4j.enumtype;

public enum StatisticType {

    /**
     * fixed value
     */
    value("value"),
    /**
     * sum
     */
    sum("sum"),
    /**
     * mean
     */
    mean("mean"),
    /**
     * standard deviation
     */
    std("std"),
    /**
     * variance
     */
    var("var"),
    /**
     * quantile range
     */
    qr("qr"),

    ;
    private String name;

    StatisticType(String name) {
        this.name = name;
    }


}
