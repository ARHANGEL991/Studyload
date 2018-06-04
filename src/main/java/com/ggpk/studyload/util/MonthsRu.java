package com.ggpk.studyload.util;

public final class MonthsRu {

    private MonthsRu() {
    }

    private static final String[] MONTH_RU = new String[]{
            "январь",
            "февраль",
            "март",
            "апрель",
            "май",
            "июнь",
            "июль ",
            "август",
            "сентябрь",
            "октябрь",
            "ноябрь",
            "декабрь",


    };

    public static String getMonthRu(int number) {
        return MONTH_RU[number];
    }

}
