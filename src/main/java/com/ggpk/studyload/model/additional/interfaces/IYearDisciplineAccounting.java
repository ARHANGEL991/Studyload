package com.ggpk.studyload.model.additional.interfaces;

import java.time.Month;

public interface IYearDisciplineAccounting {

    double[] getMonthAccounting(Month month);

    double getYearAccountingSum();

    void setMonthAccounting(Month month, double[] daysAccounting);

    double getMonthAccountingSum(Month month);

}
