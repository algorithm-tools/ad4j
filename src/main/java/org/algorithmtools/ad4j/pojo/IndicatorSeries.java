package org.algorithmtools.ad4j.pojo;

import java.io.Serializable;


public class IndicatorSeries implements Serializable {
    private final long time;
    private final double value;
    private final String logicalIndex;

    public IndicatorSeries(long time, double value, String logicalIndex) {
        this.time = time;
        this.value = value;
        this.logicalIndex = logicalIndex;
    }

    public long getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }

    public String getLogicalIndex() {
        return logicalIndex;
    }

    @Override
    public String toString() {
        return "["+time+", "+value+", "+logicalIndex+"]";
    }
}
