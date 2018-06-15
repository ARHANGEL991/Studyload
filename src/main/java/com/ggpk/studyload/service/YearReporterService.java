package com.ggpk.studyload.service;

import com.ggpk.studyload.model.Discipline;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

public interface YearReporterService {

    void createYearStatement(Year year, String exportEntityName,
                             List<Discipline> groupDisciplines,
                             String inputTemplatePath,
                             String exportBookPath);
}
