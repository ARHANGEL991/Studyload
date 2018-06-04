package com.ggpk.studyload.util;

import org.junit.Test;

import static com.ggpk.studyload.util.CellsUtils.getColumnFromCellAddress;
import static com.ggpk.studyload.util.CellsUtils.getRowFromCellAddress;
import static org.junit.Assert.assertEquals;

public class CellsUtilsTest {


    public static final String LEFT_START_CELL = "ABG99";
    public static final String COLUMN = "ABG";
    public static final int ROW = 99;


    @Test
    public void getRowFromCellAddressTest() throws Exception {

        assertEquals(ROW, getRowFromCellAddress(LEFT_START_CELL));
    }

    @Test
    public void getColumnFromCellAddressTest() throws Exception {

        assertEquals(COLUMN, getColumnFromCellAddress(LEFT_START_CELL));
    }

}