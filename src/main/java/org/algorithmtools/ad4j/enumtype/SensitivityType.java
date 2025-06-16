package org.algorithmtools.ad4j.enumtype;

public enum SensitivityType {
    HIGH("0.1"),
    MID("0.05"),
    LOW("0.01"),

    ;

    private String sensitivity;

    public String getSensitivity() {
        return sensitivity;
    }

    SensitivityType(String sensitivity) {
        this.sensitivity = sensitivity;
    }
}
