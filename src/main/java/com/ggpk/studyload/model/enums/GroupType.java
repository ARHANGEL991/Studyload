package com.ggpk.studyload.model.enums;

public enum GroupType {

    BUDGET("б"),
    COMMERCE("к"),
    PTE("пто");

    private String value;


    GroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static GroupType fromString(String text) {
        GroupType type = BUDGET;

        for (GroupType g : GroupType.values()) {
            if (g.value.equalsIgnoreCase(text)) {
                type = g;
            }

        }

        return type;
    }
}
