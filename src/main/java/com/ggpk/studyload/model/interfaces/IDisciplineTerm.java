package com.ggpk.studyload.model.interfaces;

public interface IDisciplineTerm {

    double getExamHours(int studentCount);

    double getTotalHoursWithRemoval();

    double getAcceptanceOfCourseProjects(int studentCount);

    double getHoursInWeek();

    double getTotal(int studentCount, boolean isExtramural);

    boolean isHasExam();

}
