package com.ggpk.studyload.model.additional;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AcademicYearImportEntity {

    List<DisciplineImportEntity> disciplines = new ArrayList<>();


}