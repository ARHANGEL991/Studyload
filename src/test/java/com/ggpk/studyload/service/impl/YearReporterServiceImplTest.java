package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class YearReporterServiceImplTest {


    public static final String INPUT_YEAR_GROUP_STATMENT_XLSX = "YearStatment.xls";
    public static final String OUTPUT_YEAR_GROUP_STATMENT_XLS = "YearResult.xls";


    @Autowired
    private AcademicYearSheetConverter academicYearSheetConverter;

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private AcademicPlanReader academicPlanReader;

    @Autowired
    private YearReporterService yearReporterService;


    @Test
    public void createYearStatement() {

        yearReporterService.createYearStatement(null,
                "Орехво В.Д.",
                disciplineService.getDisciplinesByTeacherName("Орехво В.Д."),
                INPUT_YEAR_GROUP_STATMENT_XLSX,
                OUTPUT_YEAR_GROUP_STATMENT_XLS);
    }
}