package com.ggpk.studyload.util;

import com.ggpk.studyload.service.impl.AcademicPlanReaderImpl;
import org.junit.Test;

public class AcademicPlanReaderImplTest {

    AcademicPlanReaderImpl academicPlanReader = new AcademicPlanReaderImpl();

    @Test
    public void cellMappingInitialize() throws Exception {
        academicPlanReader.getAcademicYearFromXls(AcademicPlanReaderImpl.INPUT_XLSX, 2);
    }

}