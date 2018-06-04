package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.additional.YearDisciplineAccounting;
import com.ggpk.studyload.service.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MonthReporterServiceImplTest {


    public static final String INPUT_MONTH_GROUP_STATMENT_XLSX = "MonthGroupStatment.xls";
    public static final String OUTPUT_MONTH_GROUP_STATMENT_XLS = "result.xls";

    private static final String INPUT_TEACHER_GROUP_STATEMENT_XLSX = "MonthTeacherStatement.xls";
    private static final String OUTPUT_TEACHER_GROUP_STATEMENT_XLS = "MonthTeacherStatement_Result.xls";

    @Autowired
    private MonthReporterService reporterService;

    @Autowired
    private AcademicYearSheetConverter academicYearSheetConverter;

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private AcademicPlanReader academicPlanReader;


    private static boolean needSetup = true;

    @Before
    public void setUpDB() throws Exception {

        final String GROUP_NAME = "ПЗТ-27";
        final Month MONTH = Month.DECEMBER;
        final double[] daysAccounting = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1};
        final double[] daysAccountingAdditionalControl = new double[]{0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0};


        AcademicYear academicYear = academicYearSheetConverter.convertImportAcademicEntity(academicPlanReader.getAcademicYearFromXls(AcademicPlanReaderImpl.INPUT_XLSX, 2));

        academicYearService.save(academicYear);

        List<Discipline> disciplines = disciplineService.getDisciplinesByGroupName(GROUP_NAME).parallelStream()
                .peek(discipline -> {
                    discipline.getFullGroup().setYearDisciplineAccounting(new YearDisciplineAccounting());
                    discipline.getFullGroup().setYearDisciplineAccountingAdditionalControl(new YearDisciplineAccounting());
                    discipline.getFullGroup().getYearDisciplineAccounting().setMonthAccounting(MONTH, daysAccounting);
                    discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().setMonthAccounting(MONTH, daysAccountingAdditionalControl);//initialize month daysAccounting
                })
                .collect(Collectors.toList());

        disciplineService.saveAll(disciplines);
        needSetup = false;

    }

    @Test
    public void createMonthGroupStatement() throws Exception {
        final String groupName = "ПЗТ-27";
        final Month month = Month.DECEMBER;

        Map<String, String> exportGroupSettings = new HashMap<>();
        exportGroupSettings.put("xlsArea", "ВедомостьМесяцГруппа!A1:AJ10");
        exportGroupSettings.put("disciplineArea", "ВедомостьМесяцГруппа!A9:AJ9");
        exportGroupSettings.put("disciplineAreaEachArea", "A9:AJ9");

        reporterService.createMonthStatement(month, Year.now(),
                groupName,
                disciplineService.getDisciplinesByGroupName(groupName),
                exportGroupSettings,
                INPUT_MONTH_GROUP_STATMENT_XLSX,
                OUTPUT_MONTH_GROUP_STATMENT_XLS);

        reporterService.clearAllZeroCell(OUTPUT_MONTH_GROUP_STATMENT_XLS, groupName, 8, 4);

        Workbook hssfInputWorkbook = WorkbookFactory.create(new File(OUTPUT_MONTH_GROUP_STATMENT_XLS));
        Sheet sheet = hssfInputWorkbook.getSheet(groupName);
        CellReference cellReference = new CellReference("A9");
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());

        assertEquals(groupName, cell.getStringCellValue());
        hssfInputWorkbook.close();
    }

    @Test
    public void createMonthTeacherStatement() throws Exception {
        final String teacherName = "Орехво В.Д.";
        final Month month = Month.DECEMBER;

        Map<String, String> exportGroupSettings = new HashMap<>();
        exportGroupSettings.put("xlsArea", "ВедомостьМесяцПреподаватель!A1:AI10");
        exportGroupSettings.put("disciplineArea", "ВедомостьМесяцПреподаватель!A9:AI9");
        exportGroupSettings.put("disciplineAreaEachArea", "A9:AI9");

        reporterService.createMonthStatement(month, Year.now(),
                teacherName,
                disciplineService.getDisciplinesByTeacherName(teacherName),
                exportGroupSettings,
                INPUT_TEACHER_GROUP_STATEMENT_XLSX,
                OUTPUT_TEACHER_GROUP_STATEMENT_XLS);

        reporterService.clearAllZeroCell(OUTPUT_TEACHER_GROUP_STATEMENT_XLS, teacherName, 8, 3);

        Workbook hssfInputWorkbook = WorkbookFactory.create(new File(OUTPUT_TEACHER_GROUP_STATEMENT_XLS));
        Sheet sheet = hssfInputWorkbook.getSheet(teacherName);
        CellReference cellReference = new CellReference("A9");
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());

        assertEquals("ПЗТ-27", cell.getStringCellValue());
        hssfInputWorkbook.close();

    }

}