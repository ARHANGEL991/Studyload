package com.ggpk.studyload.model.enums;

public enum GroupStudyForm {

    EXTRAMURAL("заоч"),
    FULL_TIME("");


    private String value;


    GroupStudyForm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static GroupStudyForm fromString(String text) {
        for (GroupStudyForm en : GroupStudyForm.values()) {
            if (en.value.equalsIgnoreCase(text)) {
                return en;
            }
        }
        return null;
    }

    public static boolean isExtramular(GroupStudyForm studyForm) {
        return studyForm.equals(GroupStudyForm.EXTRAMURAL);
    }

}
