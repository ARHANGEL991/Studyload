package com.ggpk.studyload.model.additional;

import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class YearDisciplineAccountingTest {

    private static final double DELTA = 0.01;
    private YearDisciplineAccounting yearDisciplineAccounting = new YearDisciplineAccounting();
    double[] monthAcc = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};


    @Before
    public void setUp() throws Exception {
        yearDisciplineAccounting.yearAccounting.put(Month.DECEMBER, monthAcc);
        yearDisciplineAccounting.yearAccounting.put(Month.AUGUST, monthAcc);
        yearDisciplineAccounting.yearAccounting.put(Month.SEPTEMBER, monthAcc);
    }

    @Test
    public void getYearAccountingSumTest() throws Exception {

        assertEquals(30, yearDisciplineAccounting.getYearAccountingSum(), DELTA);
    }

    @Test
    public void getMonthArrayTest() throws Exception {
        assertArrayEquals(monthAcc, yearDisciplineAccounting.getMonthAccounting(Month.DECEMBER), DELTA);
    }

    @Test
    public void getMonthSumTest() {
        assertEquals(Arrays.stream(monthAcc).sum(), yearDisciplineAccounting.getMonthAccountingSum(Month.DECEMBER), DELTA);
    }
}