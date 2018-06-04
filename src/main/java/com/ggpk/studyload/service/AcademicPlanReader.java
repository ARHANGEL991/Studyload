package com.ggpk.studyload.service;

import com.ggpk.studyload.model.additional.AcademicYearImportEntity;

public interface AcademicPlanReader {

    AcademicYearImportEntity getAcademicYearFromXls(String inputXls, int sheetNumber);
}
