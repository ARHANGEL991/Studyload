package com.ggpk.studyload.service;

import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.model.additional.AcademicYearImportEntity;

public interface AcademicYearSheetConverter {

    AcademicYear convertImportAcademicEntity(AcademicYearImportEntity academicYearImportEntity);
}
