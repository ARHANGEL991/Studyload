package com.ggpk.studyload.model.enums;

public enum DisciplineType {

    PROF_EDU_COMPONENT("пк"),
    GENERAL_EDU_COMPONENT("ок");

    private String value;

    DisciplineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static DisciplineType fromString(String text) {
        for (DisciplineType en : DisciplineType.values()) {
            if (en.value.equalsIgnoreCase(text)) {
                return en;
            }
        }
        return null;
    }
}
