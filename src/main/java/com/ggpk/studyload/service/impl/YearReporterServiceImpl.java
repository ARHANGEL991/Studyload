package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.enums.GroupType;
import com.ggpk.studyload.service.YearReporterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Service
@Slf4j
public class YearReporterServiceImpl implements YearReporterService {


    @SneakyThrows
    public void createYearStatement(Year year,
                                    String exportEntityName,
                                    List<Discipline> disciplines,
                                    Map<String, String> exportBookSettings,
                                    String inputTemplatePath,
                                    String exportBookPath) {

        List<Discipline> disciplinesToExport = disciplines.stream().map(
                discipline -> {
                    List<Discipline> discip = new ArrayList<>();

                    if (discipline.getFullGroup().getAdditionalControl() > 0) {
                        Discipline disciplineAdditionalControl = Discipline.builder()
                                .disciplineType(discipline.getDisciplineType())
                                .academicYear(discipline.getAcademicYear())
                                .fullGroup(discipline.getFullGroup())
                                .group(discipline.getGroup())
                                .name(discipline.getName() + " ДК")
                                .build();
                        discip.add(disciplineAdditionalControl);
                    }

                    return discip;
                }
        ).flatMap(Collection::stream).collect(Collectors.toList());


        log.info("Running  month export ");
        log.info(MessageFormat.format("Count of entity to export {0}", disciplinesToExport.size()));

        try (InputStream workbookStream = new FileInputStream(new File(inputTemplatePath))) {

            try (HSSFWorkbook hssfInputWorkbook = new HSSFWorkbook(workbookStream)) {
                HSSFSheet sheet = hssfInputWorkbook.cloneSheet(1);

                if (hssfInputWorkbook.getSheet(exportEntityName) != null) {
                    hssfInputWorkbook.removeSheetAt(hssfInputWorkbook.getSheetIndex(hssfInputWorkbook.getSheet(exportEntityName)));
                }

                hssfInputWorkbook.setSheetName(hssfInputWorkbook.getSheetIndex(sheet), exportEntityName);

                HSSFCellStyle cellStyle = hssfInputWorkbook.createCellStyle();
                Font font = cellStyle.getFont(hssfInputWorkbook);

                font.setFontHeightInPoints((short) 10);
                font.setBold(true);

                cellStyle.setFont(font);

                AtomicInteger currentColumn = new AtomicInteger(2);

                sheet.getRow(5).createCell(5).setCellValue(exportEntityName);

                if (!disciplinesToExport.isEmpty()) {
                    exportGroups(disciplinesToExport, sheet, currentColumn);
                }

                createTotalColumn(sheet, currentColumn, hssfInputWorkbook);


                try (OutputStream os = new FileOutputStream(inputTemplatePath)) {
                    hssfInputWorkbook.write(os);
                }
            }
        }
    }

    private void createTotalColumn(HSSFSheet sheet, AtomicInteger currentCell, HSSFWorkbook workbook) {


        HSSFCellStyle cellStyle = workbook.createCellStyle();
        Font font = cellStyle.getFont(workbook);

        font.setFontHeightInPoints((short) 10);
        font.setBold(true);

        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        getCell(sheet.getRow(8), currentCell.get()).setCellValue("Итого");
        getCell(sheet.getRow(8), currentCell.get()).setCellStyle(cellStyle);

        for (int currentRow = 9; currentRow < 24; currentRow++) {
            HSSFRow row = sheet.getRow(currentRow);

            String ref = "C" + valueOf(currentRow + 1) + ":" + (char) ('A' + currentCell.get() - 1) + valueOf(currentRow + 1);
            log.info("Total cell {} ", ref);
            HSSFCell totalCell = getCell(row, currentCell.get());
            totalCell.setCellType(CellType.FORMULA);
            totalCell.setCellFormula("SUM(" + ref + ")");
        }
    }

    private void exportGroups(List<Discipline> disciplinesToExport, HSSFSheet sheet, AtomicInteger currentColumn) {
        final int[] currentRow = {7};
        disciplinesToExport.forEach(discipline -> {


            //Discipline
            getCell(sheet.getRow(currentRow[0]++), currentColumn.get()).setCellValue(discipline.getGroup().getName());
            getCell(sheet.getRow(currentRow[0]++), currentColumn.get()).setCellValue(discipline.getName());
            List<Month> months = new ArrayList<>(
                    Arrays.asList(
                            Month.SEPTEMBER,
                            Month.OCTOBER,
                            Month.NOVEMBER,
                            Month.DECEMBER,
                            Month.JANUARY,
                            Month.FEBRUARY,
                            Month.MARCH,
                            Month.APRIL,
                            Month.MAY,
                            Month.JUNE,
                            Month.JULY
                    )
            );
            months.forEach(month -> {
                if (discipline.getFullGroup().getYearDisciplineAccounting() != null && discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(month) > 0) {
                    getCell(sheet.getRow(currentRow[0]), currentColumn.get()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(month));
                }
                currentRow[0]++;
            });


            //Sum
            HSSFCell sumCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            sumCell.setCellType(CellType.FORMULA);
            String ref = (char) ('A' + currentColumn.get()) + "10:" + (char) ('A' + currentColumn.get()) + "20";
            log.info("Sum {} ", ref);
            sumCell.setCellFormula("SUM(" + ref + ")");

            //Planed
            getCell(sheet.getRow(currentRow[0]++), currentColumn.get()).setCellValue(discipline.getFullGroup().getTotalSum());
            //Not done
            ref = (char) ('A' + currentColumn.get()) + "21 - " + (char) ('A' + currentColumn.get()) + "20";
            log.info("Not done {} ", ref);
            HSSFCell notDoneCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            notDoneCell.setCellType(CellType.FORMULA);
            notDoneCell.setCellFormula(ref);

            //Over the plan
            ref = (char) ('A' + currentColumn.get()) + "20 - " + (char) ('A' + currentColumn.get()) + "21";
            log.info("Over the plan {} ", ref);
            HSSFCell overThePlanCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            overThePlanCell.setCellType(CellType.FORMULA);
            overThePlanCell.setCellFormula(ref);

            //DisciplineType
            getCell(sheet.getRow(currentRow[0]++), currentColumn.get()).setCellValue(discipline.getDisciplineType().getValue());

            //GroupType
            getCell(sheet.getRow(currentRow[0]), currentColumn.getAndIncrement()).setCellValue(discipline.getGroup().getGroupType() == GroupType.PTE ? discipline.getGroup().getGroupType().getValue() : "ССО");

            currentRow[0] = 7;
        });
    }

    private HSSFCell getCell(HSSFRow row, int cellNum) {
        HSSFCell cell;
        if (row.getCell(cellNum) == null) {
            cell = row.createCell(cellNum);
        } else {
            cell = row.getCell(cellNum);
        }
        return cell;
    }
}