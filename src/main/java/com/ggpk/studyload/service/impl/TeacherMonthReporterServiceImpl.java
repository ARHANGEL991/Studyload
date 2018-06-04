package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.additional.DisciplineExportEntity;
import com.ggpk.studyload.service.TeacherMonthReporterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.jxls.area.XlsArea;
import org.jxls.command.EachCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Deprecated
@Service
@Slf4j
public class TeacherMonthReporterServiceImpl implements TeacherMonthReporterService {


    private static final String INPUT_TEACHER_GROUP_STATEMENT_XLSX = "MonthTeacherStatement.xls";
    private static final String OUTPUT_TEACHER_GROUP_STATEMENT_XLS = "MonthTeacherStatement_Result.xls";


    @Transactional
    @SneakyThrows
    public void createTeacherMonthStatement(Month month, Year year, String teacherName, List<Discipline> groupDisciplines) {

        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("RU", "ru"));


//        List<Discipline> groupDisciplines = disciplineService.getDisciplinesByGroup(groupService.getGroupByName(groupName));


        List<DisciplineExportEntity> disciplines = groupDisciplines.stream().map(discipline -> DisciplineExportEntity.builder().group(discipline.getGroup())
                .name(discipline.getName())
                .month(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccounting(month))
                .teacher(discipline.getFullGroup().getTeacher())
                .build()
        ).collect(Collectors.toList());


        log.info("Running group month export ");


        try (InputStream is = new java.io.FileInputStream(new File(INPUT_TEACHER_GROUP_STATEMENT_XLSX))) {

            try (OutputStream os = new FileOutputStream(OUTPUT_TEACHER_GROUP_STATEMENT_XLS)) {


//                Context context = new Context();
//                context.putVar("disciplines", disciplines);
//                context.putVar("month", symbols.getMonths()[month.getValue()-1]);
//                context.putVar("year", year.toString());
//                context.putVar("group",disciplines.get(0));
//                JxlsHelper.getInstance().processTemplate(is, os, context);
//                Transformer transformer = JxlsHelper.getInstance().createTransformer(is, os);
//                JxlsHelper.getInstance().processTemplate(context,transformer);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                XlsArea xlsArea = new XlsArea("ВедомостьМесяцПреподаватель!A1:AI10", transformer);
                XlsArea disciplineArea = new XlsArea("ВедомостьМесяцПреподаватель!A9:AI9", transformer);
                EachCommand disciplineEachCommand = new EachCommand("discipline", "disciplines", disciplineArea);
                xlsArea.addCommand("A9:AI9", disciplineEachCommand);


//                    Row row = CellUtil.getRow(8, sheet);
//                    int columnIndex = 4;
//                    for (int i = 0; i < 25; i++) {
//                        String address = CellUtil.getCell(row, columnIndex + i).getAddress().formatAsString();
//                        XlsArea ifArea = new XlsArea("ВедомостьМесяцГруппа!" + address + ":" + address, transformer);
//                        IfCommand ifCommand = new IfCommand("discipline.month[" + i + "] > 0", ifArea);
//                        disciplineArea.addCommand("ВедомостьМесяцГруппа!" + address + ":" + address, ifCommand);
//                    }


                Context context = new Context();
                context.putVar("disciplines", disciplines);
                context.putVar("month", symbols.getMonths()[month.getValue() - 1]);
                context.putVar("year", year.toString());
                context.putVar("teacher", teacherName);
                xlsArea.applyAt(new CellRef(teacherName + "!A1"), context);
                xlsArea.processFormulas();
                transformer.write();
            }
        }

        try (InputStream workbokStream = new java.io.FileInputStream(new File(OUTPUT_TEACHER_GROUP_STATEMENT_XLS))) {

            try (Workbook hssfInputWorkbook = WorkbookFactory.create(workbokStream)) {
                Sheet sheet = hssfInputWorkbook.getSheetAt(hssfInputWorkbook.getNumberOfSheets() - 1);


                CellStyle style = hssfInputWorkbook.createCellStyle();
                style.setWrapText(true);                                     //auto height


                for (int rowCounter = 8; rowCounter <= sheet.getLastRowNum(); rowCounter++) {
                    Row row = CellUtil.getRow(rowCounter, sheet);
                    row.setRowStyle(style);
                    row.getCell(1).setCellStyle(style);
                    int columnIndex = 3;
                    for (int i = 0; i < 31; i++) { //if 0 cell
                        Cell cell = CellUtil.getCell(row, columnIndex + i);
                        if (cell.getNumericCellValue() == 0) {
                            cell.setCellValue((String) null);
                        }

                    }
                }
                try (OutputStream os = new FileOutputStream(OUTPUT_TEACHER_GROUP_STATEMENT_XLS)) {
                    hssfInputWorkbook.write(os);
                }
            }
        }


    }
}

