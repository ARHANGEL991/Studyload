package com.ggpk.studyload.model.interfaces;

import com.ggpk.studyload.model.enums.DisciplineType;


public interface IDiscipline<A extends IAcademicYear, G extends IGroup, S extends ISubGroup> {

    DisciplineType getDisciplineType();

    String getName();

    A getAcademicYear();

    G getGroup();

    boolean isSubGroup();


}
