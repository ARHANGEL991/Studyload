package com.ggpk.studyload.model.additional;

import com.ggpk.studyload.model.BaseEntity;
import com.ggpk.studyload.model.additional.interfaces.IYearDisciplineAccounting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.time.Month;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;


@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class YearDisciplineAccounting extends BaseEntity implements IYearDisciplineAccounting {


    @ElementCollection(fetch = FetchType.EAGER)
    @Lob
    Map<Month, double[]> yearAccounting = new EnumMap<>(Month.class);


    public double[] getMonthAccounting(Month month) {

        yearAccounting.putIfAbsent(month, new double[31]);

        return yearAccounting.get(month);
    }

    /**
     * @return sum of arrays in map
     */
    public double getYearAccountingSum() {
        return yearAccounting.values().stream().flatMapToDouble(Arrays::stream).sum();
    }

    public void setMonthAccounting(Month month, double[] daysAccounting) {

        yearAccounting.remove(month);
        yearAccounting.put(month, daysAccounting);
    }

    public double getMonthAccountingSum(Month month) {
        return Arrays.stream(yearAccounting.get(month)).sum();
    }

}
