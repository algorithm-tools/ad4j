package org.algorithmtools.ad4j.utils;

import org.algorithmtools.ad4j.pojo.IndicatorSeries;

import java.util.Comparator;

public class IndicatorSeriesComparator implements Comparator<IndicatorSeries> {
    @Override
    public int compare(IndicatorSeries o1, IndicatorSeries o2) {
        if (o1.getValue() > o2.getValue()) {
            return 1;
        } else if (o1.getValue() < o2.getValue()) {
            return -1;
        } else {
            return Long.compare(o1.getTime(), o2.getTime());
        }
    }
}