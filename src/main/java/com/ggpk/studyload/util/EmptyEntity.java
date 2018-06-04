package com.ggpk.studyload.util;

import com.ggpk.studyload.model.DisciplineTerm;

public final class EmptyEntity {

    public static final DisciplineTerm EMPTY_TERM = new DisciplineTerm();

    private EmptyEntity() {
        throw new IllegalStateException("Utility class");
    }
}
