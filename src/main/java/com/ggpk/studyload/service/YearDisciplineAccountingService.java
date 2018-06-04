package com.ggpk.studyload.service;

import com.ggpk.studyload.model.additional.YearDisciplineAccounting;

import java.util.List;

public interface YearDisciplineAccountingService extends IRepository<YearDisciplineAccounting, Long> {

    <S extends YearDisciplineAccounting> List<S> getYearDisciplineAccountingByGroupName(String groupName);

    <S extends YearDisciplineAccounting> List<S> getYearDisciplineAccountingByTeacherName(String teacherName);

    <S extends YearDisciplineAccounting> List<S> getYearDisciplineAccountingByGroupNameLike(String groupName);

    <S extends YearDisciplineAccounting> List<S> getYearDisciplineAccountingByTeacherNameLike(String teacherName);
}
