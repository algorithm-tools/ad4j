package org.algorithmtools.ad4j.enumtype;

public enum CompareType {
    /**
     * =
     */
    EQ,
    /**
     * >
     */
    GREATER,
    /**
     * >=
     */
    GREATER_OR_EQ,
    /**
     * <
     */
    LESS,
    /**
     * <=
     */
    LESS_OR_EQ;

    public static CompareType parse(String signal) {
        if (">".equals(signal)) {
            return GREATER;
        } else if (">=".equals(signal) || "GREATER_OR_EQUAL".equalsIgnoreCase(signal) || "GREATER_OR_EQ".equalsIgnoreCase(signal)) {
            return GREATER_OR_EQ;
        } else if ("=".equals(signal) || "==".equals(signal) || "EQUAL".equalsIgnoreCase(signal) || "EQ".equalsIgnoreCase(signal)) {
            return EQ;
        } else if ("<".equals(signal) || "LESS".equalsIgnoreCase(signal)) {
            return LESS;
        } else if ("<=".equals(signal) || "LESS_THAN_OR_EQUAL".equalsIgnoreCase(signal) || "LESS_THAN_OR_EQ".equalsIgnoreCase(signal)) {
            return LESS_OR_EQ;
        }
        throw new IllegalArgumentException("[" + signal + "] signal illegal! Please input >,>=,=,==,<,<=.");
    }

    public boolean compare(Double current, Double signal) {
        if (this == EQ) {
            return current.compareTo(signal) == 0;
        } else if (this == GREATER) {
            return current.compareTo(signal) > 0;
        } else if (this == GREATER_OR_EQ) {
            return current.compareTo(signal) >= 0;
        } else if (this == LESS) {
            return current.compareTo(signal) < 0;
        } else if (this == LESS_OR_EQ) {
            return current.compareTo(signal) <= 0;
        }
        return false;
    }
}
