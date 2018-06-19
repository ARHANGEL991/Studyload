package com.ggpk.studyload.service;

import com.ggpk.studyload.model.Discipline;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Deprecated
public interface TeacherMonthReporterService {

    void createTeacherMonthStatement(Month month, Year year, String teacherName, List<Discipline> groupDisciplines);
}
