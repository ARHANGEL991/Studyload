package com.ggpk.studyload.util;

public final class CellsUtils {

    public static final int OFFSET = -1;


    public static final int GROUP_NAME_COLUMN = 1 + OFFSET;
    public static final int GROUP_TYPE_COLUMN = 27 + OFFSET;
    public static final int GROUP_STUDY_FORM_COLUMN = 29 + OFFSET;

    public static final int DISCIPLINE_NAME_COLUMN = 2 + OFFSET;
    public static final int DISCIPLINE_TYPE_COLUMN = 26 + OFFSET;
    public static final int DISCIPLINE_GROUP_STUDENT_COUNT_COLUMN = 3 + OFFSET;
    public static final int DISCIPLINE_GROUP_ADDITIONAL_CONTROL_COLUMN = 25 + OFFSET;

    public static final int DISCIPLINE_TERM1_TOTAL_HOURS = 4 + OFFSET;
    public static final int DISCIPLINE_TERM1_TOTAL_HOURS_WITH_REMOVAL = 5 + OFFSET;
    public static final int DISCIPLINE_TERM1_LAB_COLUMN = 6 + OFFSET;
    public static final int DISCIPLINE_TERM1_COURSE_DESIGN_COLUMN = 7 + OFFSET;
    public static final int DISCIPLINE_TERM1_CONSULTATION_COLUMN = 8 + OFFSET;
    public static final int DISCIPLINE_TERM1_TEST_COUNT_COLUMN = 11 + OFFSET;
    public static final int DISCIPLINE_TERM1_WEEKS_IN_TERM_COLUMN = 12 + OFFSET;
    public static final int DISCIPLINE_TERM1_EXAM_VALUE = 10 + OFFSET;

    public static final int DISCIPLINE_TERM2_TOTAL_HOURS = 14 + OFFSET;
    public static final int DISCIPLINE_TERM2_TOTAL_HOURS_WITH_REMOVAL = 15 + OFFSET;
    public static final int DISCIPLINE_TERM2_LAB_COLUMN = 16 + OFFSET;
    public static final int DISCIPLINE_TERM2_COURSE_DESIGN_COLUMN = 17 + OFFSET;
    public static final int DISCIPLINE_TERM2_CONSULTATION_COLUMN = 18 + OFFSET;
    public static final int DISCIPLINE_TERM2_TEST_COUNT_COLUMN = 21 + OFFSET;
    public static final int DISCIPLINE_TERM2_WEEKS_IN_TERM_COLUMN = 22 + OFFSET;
    public static final int DISCIPLINE_TERM2_EXAM_VALUE = 20 + OFFSET;

    public static final int TEACHER_NAME_COLUMN = 28 + OFFSET;


    public static int getRowFromCellAddress(String address) {

        return Integer.parseInt(address.substring(getMiddleIndex(address)));
    }


    private static int getMiddleIndex(String address) {

        char[] charArray = address.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 47 && charArray[i] < 58) {
                return i;
            }
        }
        return 1;
    }

    public static String getColumnFromCellAddress(String address) {
        return address.substring(0, getMiddleIndex(address));
    }

    private CellsUtils() {
    }
}
