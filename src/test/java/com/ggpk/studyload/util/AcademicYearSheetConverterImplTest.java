package com.ggpk.studyload.util;

import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.service.AcademicPlanReader;
import com.ggpk.studyload.service.AcademicYearService;
import com.ggpk.studyload.service.AcademicYearSheetConverter;
import com.ggpk.studyload.service.impl.AcademicPlanReaderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AcademicYearSheetConverterImplTest {

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private AcademicYearSheetConverter academicYearSheetConverter;

    @Autowired
    private AcademicPlanReader academicPlanReader;

//    @Autowired
//    private DisciplineService disciplineService;
//
//    @Autowired
//    private MonthReporterService reporterService;


    @Test
    @Rollback
    public void getAcademicYearFromSheet() throws Exception {

        AcademicYear academicYear = academicYearSheetConverter.convertImportAcademicEntity(academicPlanReader.getAcademicYearFromXls(AcademicPlanReaderImpl.INPUT_XLSX, 2));

        academicYearService.save(academicYear);

    }

}