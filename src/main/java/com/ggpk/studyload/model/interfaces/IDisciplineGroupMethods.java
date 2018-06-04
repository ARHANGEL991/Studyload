package com.ggpk.studyload.model.interfaces;

public interface IDisciplineGroupMethods<T extends ITeacher, D extends IDisciplineTerm> {
    T getTeacher();

    D getFirstTerm();

    D getSecondTerm();
}
