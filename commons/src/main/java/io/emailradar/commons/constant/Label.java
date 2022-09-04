package io.emailradar.commons.constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Label {
    SPAM("spam"),
    HAM("ham"),
    DEFAULT("default");

    private final String labelName;

    Label(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public static Label getLabelOrDefault(String labelName) {
        Label label;
        try {
            label = Label.valueOf(labelName.toUpperCase());
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
            log.error("Using default label: {}", Label.DEFAULT);
            label = Label.DEFAULT;
        }
        return label;
    }
}
