package com.ggpk.studyload.model.interfaces;

import com.ggpk.studyload.model.enums.GroupType;

import java.util.List;

public interface IGroup<A extends IAcademicYear, D extends IDiscipline> {

    A getCurrentAcademicYear();

    String getName();

    int getStudentCount();

    GroupType getGroupType();

    List<D> getDisciplines();

}
