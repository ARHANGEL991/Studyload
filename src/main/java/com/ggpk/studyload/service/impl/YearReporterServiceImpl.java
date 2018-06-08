package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.service.YearReporterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class YearReporterServiceImpl implements YearReporterService {


    @SneakyThrows
    public void createYearStatement(Month month,
                                    Year year,
                                    String exportEntityName,
                                    List<Discipline> groupDisciplines,
                                    Map<String, String> exportBookSettings,
                                    String inputTemplatePath,
                                    String exportBookPath) {

        List<Discipline> disciplines = groupDisciplines.stream().map(
                discipline -> {
                    List<Discipline> discip = new ArrayList<>();

                    if (discipline.getFullGroup().getAdditionalControl() > 0) {
                        Discipline disciplineAC = Discipline.builder()
                                .disciplineType(discipline.getDisciplineType())
                                .academicYear(discipline.getAcademicYear())
                                .fullGroup(discipline.getFullGroup())
                                .group(discipline.getGroup())
                                .name(discipline.getName() + " ДК")
                                .build();
                        discip.add(disciplineAC);
                    }

                    return discip;
                }
        ).flatMap(Collection::stream).collect(Collectors.toList());


        log.info("Running  month export ");
        log.info(MessageFormat.format("Count of entity to export {0}", disciplines.size()));

        try (InputStream workbookStream = new FileInputStream(new File(inputTemplatePath))) {

            try (HSSFWorkbook hssfInputWorkbook = new HSSFWorkbook(workbookStream)) {
                HSSFSheet sheet = hssfInputWorkbook.cloneSheet(1);
                hssfInputWorkbook.setSheetName(hssfInputWorkbook.getSheetIndex(sheet), exportEntityName);

                AtomicInteger currentRow = new AtomicInteger(7);
                AtomicInteger currentCell = new AtomicInteger(2);

                sheet.getRow(5).createCell(5).setCellValue(exportEntityName);


                groupDisciplines.forEach(discipline -> {

                    HSSFRow row = sheet.getRow(currentRow.getAndIncrement());

                    //Discipline
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getGroup().getName());
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getName());
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.SEPTEMBER));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.OCTOBER));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.NOVEMBER));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.DECEMBER));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.JANUARY));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.FEBRUARY));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.MARCH));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.APRIL));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.MAY));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.JUNE));
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(Month.JULY));

                    //Sum
                    HSSFCell sumCell = row.createCell(currentRow.getAndIncrement());
                    sumCell.setCellType(CellType.FORMULA);
                    String ref = (char) ('A' + currentRow.get()) + "9:" + (char) ('A' + currentRow.get()) + "19";
                    sumCell.setCellFormula("SUM(" + ref + ")");

                    //Planed
                    row.createCell(currentRow.getAndIncrement()).setCellValue(discipline.getFullGroup().getTotalSum());


                });


                try (OutputStream os = new FileOutputStream(inputTemplatePath)) {
                    hssfInputWorkbook.write(os);
                }
            }
        }
    }
}