package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.additional.YearDisciplineAccounting;
import com.ggpk.studyload.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
@Deprecated
public class TeacherMonthReporterServiceImplTest {

    public static final String INPUT_MONTH_GROUP_STATEMENT_XLSX = "MonthTeacherStatement.xls";
    public static final String OUTPUT_MONTH_GROUP_STATEMENT_XLS = "MonthTeacherStatement_Result.xls";

    @Autowired
    private TeacherMonthReporterService reporterService;

    @Autowired
    private AcademicYearSheetConverter academicYearSheetConverter;

    @Autowired
    private AcademicPlanReader academicPlanReader;

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private DisciplineService disciplineService;


    @Test
    @Rollback
    public void createTeacherMonthStatement() throws Exception {

        final String TEACHER_NAME = "Орехво В.Д.";
        final Month MONTH = Month.DECEMBER;
        final double[] daysAccounting = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1};


        AcademicYear academicYear = academicYearSheetConverter.convertImportAcademicEntity(academicPlanReader.getAcademicYearFromXls(AcademicPlanReaderImpl.INPUT_XLSX, 2));

        academicYearService.save(academicYear);

        List<Discipline> disciplines = disciplineService.getDisciplinesByTeacherName(TEACHER_NAME).parallelStream()
                .peek(discipline -> discipline.getFullGroup().setYearDisciplineAccounting(new YearDisciplineAccounting()))
                .peek(discipline -> {
                    discipline.getFullGroup().getYearDisciplineAccounting().setMonthAccounting(MONTH, daysAccounting);      //initialize month daysAccounting
                })
                .collect(Collectors.toList());


        disciplineService.saveAll(disciplines);

        disciplines = disciplineService.getDisciplinesByGroupName("ПЗТ-27");


        reporterService.createTeacherMonthStatement(Month.DECEMBER, Year.now(), TEACHER_NAME, disciplines);

        Workbook hssfInputWorkbook = WorkbookFactory.create(new File(OUTPUT_MONTH_GROUP_STATEMENT_XLS));
        Sheet sheet = hssfInputWorkbook.getSheetAt(1);
        CellReference cellReference = new CellReference("A9");
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());

        assertEquals("ПЗТ-27", cell.getStringCellValue());
    }

}