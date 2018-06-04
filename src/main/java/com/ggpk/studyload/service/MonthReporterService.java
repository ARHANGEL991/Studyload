package com.ggpk.studyload.service;


import com.ggpk.studyload.model.Discipline;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;


public interface MonthReporterService {

    void createMonthStatement(Month month, Year year, String exportEntityName,
                              List<Discipline> groupDisciplines,
                              Map<String, String> exportBookSettings,
                              String inputTemplatePath,
                              String exportBookPath);

    void clearAllZeroCell(String workbookPathname, String sheetName, int startRow, int startColumn);

}
