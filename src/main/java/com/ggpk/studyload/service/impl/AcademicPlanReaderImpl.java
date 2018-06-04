package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.additional.AcademicYearImportEntity;
import com.ggpk.studyload.model.additional.DisciplineImportEntity;
import com.ggpk.studyload.service.AcademicPlanReader;
import com.ggpk.studyload.util.CellsUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.reader.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ggpk.studyload.util.CellsUtils.*;

@Slf4j
@Service
public class AcademicPlanReaderImpl implements AcademicPlanReader {

    public static final String INPUT_XLSX = "/input/plan.xlsm";
    public static final String LEFT_START_CELL = "A9";
    public static final String RIGHT_START_CELL = "AB";


    public static final String DISCIPLINE = "discipline";


    @SneakyThrows
    public AcademicYearImportEntity getAcademicYearFromXls(String inputXls, int sheetNumber) {

        AcademicYearImportEntity academicYearImportEntity = new AcademicYearImportEntity();
        log.debug("Open excel file {0}", inputXls);
        try (InputStream inputXLS = new FileInputStream(inputXls)) {
            try (Workbook hssfInputWorkbook = WorkbookFactory.create(inputXLS)) {
                Sheet sheet = hssfInputWorkbook.getSheetAt(sheetNumber - 1);
                ReaderConfig.getInstance().setSkipErrors(true);

                DisciplineImportEntity discipline = new DisciplineImportEntity();


                Map<String, Object> beans = new HashMap<>();
                beans.put("academicYear", academicYearImportEntity);
                beans.put(DISCIPLINE, discipline);


                int startRowNum = CellsUtils.getRowFromCellAddress(LEFT_START_CELL) - 1;

                ReaderConfig.getInstance().setSkipErrors(true);

                List<BeanCellMapping> blockMappings = new ArrayList<>();

                blockMappings.add(new BeanCellMapping(startRowNum, (short) GROUP_NAME_COLUMN, DISCIPLINE, "groupName"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) GROUP_TYPE_COLUMN, DISCIPLINE, "groupType"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) GROUP_STUDY_FORM_COLUMN, DISCIPLINE, "groupStudyForm"));

                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_NAME_COLUMN, DISCIPLINE, "name"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TYPE_COLUMN, DISCIPLINE, "disciplineType"));


                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_GROUP_STUDENT_COUNT_COLUMN, DISCIPLINE, "groupStudentCount"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_GROUP_ADDITIONAL_CONTROL_COLUMN, DISCIPLINE, "additionalControl"));

                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_TOTAL_HOURS, DISCIPLINE, "firstTermTotalHours"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_TOTAL_HOURS_WITH_REMOVAL, DISCIPLINE, "firstTermTotalHoursWithRemoval"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_LAB_COLUMN, DISCIPLINE, "firstTermLaboratoryPracticalExercises"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_COURSE_DESIGN_COLUMN, DISCIPLINE, "firstTermCourseDesign"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_CONSULTATION_COLUMN, DISCIPLINE, "firstTermConsultations"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_TEST_COUNT_COLUMN, DISCIPLINE, "firstTermTestsCount"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_WEEKS_IN_TERM_COLUMN, DISCIPLINE, "firstTermWeeksInTerm"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM1_EXAM_VALUE, DISCIPLINE, "firstTermExamValue"));

                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_TOTAL_HOURS, DISCIPLINE, "secondTermTotalHours"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_TOTAL_HOURS_WITH_REMOVAL, DISCIPLINE, "secondTermTotalHoursWithRemoval"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_LAB_COLUMN, DISCIPLINE, "secondTermLaboratoryPracticalExercises"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_COURSE_DESIGN_COLUMN, DISCIPLINE, "secondTermCourseDesign"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_CONSULTATION_COLUMN, DISCIPLINE, "secondTermConsultations"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_TEST_COUNT_COLUMN, DISCIPLINE, "secondTermTestsCount"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_WEEKS_IN_TERM_COLUMN, DISCIPLINE, "secondTermWeeksInTerm"));
                blockMappings.add(new BeanCellMapping(startRowNum, (short) DISCIPLINE_TERM2_EXAM_VALUE, DISCIPLINE, "secondTermExamValue"));

                blockMappings.add(new BeanCellMapping(startRowNum, (short) TEACHER_NAME_COLUMN, DISCIPLINE, "teacherName"));


                XLSBlockReader lineReader = new SimpleBlockReaderImpl(0, 0, blockMappings);
                XLSLoopBlockReader eachLineReader = new XLSForEachBlockReaderImpl(startRowNum, startRowNum, "academicYear.disciplines", DISCIPLINE, DisciplineImportEntity.class);


                SectionCheck loopBreakCheck = getLoopBreakCheck();

                eachLineReader.addBlockReader(lineReader);
                eachLineReader.setLoopBreakCondition(loopBreakCheck);

                XLSSheetReader sheetReader = new XLSSheetReaderImpl();

                sheetReader.addBlockReader(eachLineReader);

                log.info("Start read Excel file");
                sheetReader.read(sheet, beans);

                log.info("Excel file reading complete");


                log.debug("Excel file connection close {0}", INPUT_XLSX);
            }
        }
        return academicYearImportEntity;
    }


    private SectionCheck getLoopBreakCheck() {
        OffsetRowCheck rowCheck = new OffsetRowCheckImpl(0);
        rowCheck.addCellCheck(new OffsetCellCheckImpl((short) 0, "КОНЕЦ"));
        SectionCheck sectionCheck = new SimpleSectionCheck();
        sectionCheck.addRowCheck(rowCheck);
        return sectionCheck;
    }


}
