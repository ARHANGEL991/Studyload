package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.enums.GroupType;
import com.ggpk.studyload.service.YearReporterService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
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

    public enum YearReportType {
        BUDGET(" Б"),
        COMMERCE(" К");

        private String value;

        YearReportType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //TODO REFACTOR ALL!! LAST WEEK DIPLOMA
    @SneakyThrows
    public void createYearStatement(Year year,
                                    String exportEntityName,
                                    List<Discipline> disciplines,
                                    String inputTemplatePath,
                                    String exportBookPath) {

        List<Discipline> disciplinesToExport = disciplines.stream().map(
                discipline -> {
                    List<Discipline> discip = new ArrayList<>();

                    if (discipline.getFullGroup().getYearDisciplineAccounting() != null) {
                        Discipline disciplineAdditionalControl = Discipline.builder()
                                .disciplineType(discipline.getDisciplineType())
                                .academicYear(discipline.getAcademicYear())
                                .fullGroup(discipline.getFullGroup())
                                .group(discipline.getGroup())
                                .name(discipline.getName())
                                .build();
                        discip.add(disciplineAdditionalControl);
                    }

                    if (discipline.getFullGroup().getYearDisciplineAccounting() != null
                            && discipline.getFullGroup().getAdditionalControl() > 0) {
                        Discipline disciplineAdditionalControl = Discipline.builder()
                                .disciplineType(discipline.getDisciplineType())
                                .academicYear(discipline.getAcademicYear())
                                .fullGroup(discipline.getFullGroup())
                                .group(discipline.getGroup())
                                .name(discipline.getName() + " ДК")
                                .build();
                        //Fix additional control export
                        discip.add(disciplineAdditionalControl);
                    }

                    return discip;
                }
        ).flatMap(Collection::stream).collect(Collectors.toList());


        log.info("Running  month export ");
        log.info(MessageFormat.format("Count of entity to export {0}", disciplinesToExport.size()));

        try (InputStream templateStream = new FileInputStream(new File(inputTemplatePath))) {
            try (HSSFWorkbook hssfInputWorkbook = new HSSFWorkbook(templateStream)) {
                HSSFSheet sheet = hssfInputWorkbook.cloneSheet(hssfInputWorkbook.getSheetIndex(hssfInputWorkbook.getSheet("ГодовойОтчётШаблон")));
                if (!disciplinesToExport.stream()
                        .filter(discipline ->
                                discipline.getGroup().getGroupType() == GroupType.BUDGET || discipline.getGroup().getGroupType() == GroupType.PTE)
                        .collect(Collectors.toList()).isEmpty()) {

                    doReport(exportEntityName, disciplinesToExport, hssfInputWorkbook, sheet, YearReportType.BUDGET);
                    sheet = hssfInputWorkbook.cloneSheet(hssfInputWorkbook.getSheetIndex(hssfInputWorkbook.getSheet("ГодовойОтчётШаблон")));
                }

                if (!disciplinesToExport.stream()
                        .filter(discipline ->
                                discipline.getGroup().getGroupType() == GroupType.COMMERCE)
                        .collect(Collectors.toList()).isEmpty()) {

                    doReport(exportEntityName, disciplinesToExport, hssfInputWorkbook, sheet, YearReportType.COMMERCE);
                }


                try (OutputStream os = new FileOutputStream(exportBookPath)) {
                    hssfInputWorkbook.write(os);
                }
            }
        }
    }

    private void doReport(String exportEntityName, List<Discipline> disciplinesToExport, HSSFWorkbook hssfInputWorkbook, HSSFSheet sheet, YearReportType type) {
        if (hssfInputWorkbook.getSheet(exportEntityName + type.getValue()) != null) {
            hssfInputWorkbook.removeSheetAt(hssfInputWorkbook.getSheetIndex(hssfInputWorkbook.getSheet(exportEntityName + type.getValue())));
        }

        hssfInputWorkbook.setSheetName(hssfInputWorkbook.getSheetIndex(sheet), exportEntityName + type.getValue());

        if (type == YearReportType.BUDGET) {
            getCell(sheet.getRow(3), 0).setCellValue("в бюджетных группах");
            disciplinesToExport = disciplinesToExport.stream()
                    .filter(discipline ->
                            discipline.getGroup().getGroupType() == GroupType.BUDGET || discipline.getGroup().getGroupType() == GroupType.PTE)
                    .collect(Collectors.toList());
        } else {
            getCell(sheet.getRow(3), 0).setCellValue("в платных группах");
            disciplinesToExport = disciplinesToExport.stream()
                    .filter(discipline ->
                            discipline.getGroup().getGroupType() == GroupType.COMMERCE)
                    .collect(Collectors.toList());
        }
        HSSFCellStyle cellStyle = hssfInputWorkbook.createCellStyle();
        Font font = cellStyle.getFont(hssfInputWorkbook);

        font.setFontHeightInPoints((short) 10);
        font.setBold(true);

        cellStyle.setFont(font);

        AtomicInteger currentColumn = new AtomicInteger(2);

        sheet.getRow(5).createCell(5).setCellValue(exportEntityName);

        if (!disciplinesToExport.isEmpty()) {

            exportGroups(disciplinesToExport, sheet, currentColumn);
            createTotalColumn(sheet, currentColumn, hssfInputWorkbook);
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

            String ref = "C" + valueOf(currentRow + 1) + ":" + this.buildCellRef(currentRow - 1) + valueOf(currentRow + 1);
            log.debug("Total cell {} ", ref);
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
                if (discipline.getFullGroup().getYearDisciplineAccounting() != null && discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(month) > 0 && !discipline.getName().contains("ДК")) {
                    getCell(sheet.getRow(currentRow[0]), currentColumn.get()).setCellValue(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(month));
                }
                if (discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl() != null && discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().getMonthAccountingSum(month) > 0 && discipline.getName().contains("ДК")) {
                    getCell(sheet.getRow(currentRow[0]), currentColumn.get()).setCellValue(discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().getMonthAccountingSum(month));
                }
                currentRow[0]++;
            });


            //Sum
            HSSFCell sumCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            sumCell.setCellType(CellType.FORMULA);
            String ref = this.buildCellRef(currentColumn.get()) + "10:" + this.buildCellRef(currentColumn.get()) + "20";
            log.info("Sum {} ", ref);
            sumCell.setCellFormula("SUM(" + ref + ")");

            //Planed
            getCell(sheet.getRow(currentRow[0]++), currentColumn.get()).setCellValue(discipline.getFullGroup().getTotalSum());
            //Not done
            ref = this.buildCellRef(currentColumn.get()) + "22 - " + this.buildCellRef(currentColumn.get()) + "21";
            log.debug("Not done {} ", ref);
            HSSFCell notDoneCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            notDoneCell.setCellType(CellType.FORMULA);
            notDoneCell.setCellFormula(ref);

            //Over the plan
            ref = this.buildCellRef(currentColumn.get()) + "21 - " + this.buildCellRef(currentColumn.get()) + "22";
            log.debug("Over the plan {} ", ref);
            HSSFCell overThePlanCell = getCell(sheet.getRow(currentRow[0]++), currentColumn.get());
            overThePlanCell.setCellType(CellType.FORMULA);
            overThePlanCell.setCellFormula(ref);

            //DisciplineType
            if (discipline.getDisciplineType() != null) {
                getCell(sheet.getRow(currentRow[0]), currentColumn.get()).setCellValue(discipline.getDisciplineType().getValue());
            }
            //always go to next line
            currentRow[0]++;

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

    private String buildCellRef(int currentColumn) {
        int countOfLaters = currentColumn / 26;
        StringBuilder cellRef = new StringBuilder();
        for (int i = 1; i <= countOfLaters; i++) {
            cellRef.append("A");
        }
        cellRef.append((char) ('A' + Math.abs(countOfLaters * 26 - currentColumn)));
        return cellRef.toString();
    }
}