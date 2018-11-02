package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.additional.DisciplineExportEntity;
import com.ggpk.studyload.service.MonthReporterService;
import com.ggpk.studyload.util.MonthsRu;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

import java.io.*;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class MonthReporterServiceImpl implements MonthReporterService {


    public static final String INPUT_MONTH_GROUP_STATEMENT_XLSX = "MonthGroupStatment.xls";
    public static final String OUTPUT_MONTH_GROUP_STATEMENT_XLS = "result.xls";


    /**
     * @param month              on which to sample
     * @param year               current academic year
     * @param exportEntityName
     * @param groupDisciplines
     * @param exportBookSettings map with setting for Jxls
     *                           key "xlsArea" - for main area  example ( "SheetName!A1:AJ10" )
     *                           key "disciplineArea" - for discipline area. Example ( "SheetName!A9:AJ9" )
     *                           key "disciplineAreaEachArea" - link ref for each discipline area.  Example ("A9:AJ9")
     * @param inputTemplatePath
     * @param exportBookPath
     */
    @SneakyThrows(IOException.class)
    @Transactional
    public void createMonthStatement(Month month, Year year, String exportEntityName,
                                     List<Discipline> groupDisciplines,
                                     Map<String, String> exportBookSettings,
                                     String inputTemplatePath,
                                     String exportBookPath) {

        /*
         Convert to export entity

         */
        List<DisciplineExportEntity> disciplines = groupDisciplines.stream().map(
                discipline -> {
                    List<DisciplineExportEntity> discip = new ArrayList<>();

                    if (discipline.getFullGroup().getYearDisciplineAccounting() != null
                            && discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccounting(month) != null
                            && discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccountingSum(month) != 0) {

                        discip.add(DisciplineExportEntity.builder().group(discipline.getGroup())
                                .name(discipline.getName())
                                .month(discipline.getFullGroup().getYearDisciplineAccounting().getMonthAccounting(month))
                                .teacher(discipline.getFullGroup().getTeacher())
                                .build());
                    }

                    if (discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl() != null
                            && discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().getMonthAccounting(month) != null
                            && discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().getMonthAccountingSum(month) != 0) {

                        discip.add(DisciplineExportEntity.builder().group(discipline.getGroup())
                                .name(discipline.getName() + " ДК")
                                .month(discipline.getFullGroup().getYearDisciplineAccountingAdditionalControl().getMonthAccounting(month))
                                .teacher(discipline.getFullGroup().getTeacher())
                                .build());
                    }

                    return discip;
                }
        ).flatMap(Collection::stream).collect(Collectors.toList());


        log.info("Running  month export ");
        log.info(MessageFormat.format("Count of entity to export {0}", disciplines.size()));

        try (InputStream is = new java.io.FileInputStream(new File(inputTemplatePath))) {

            try (OutputStream os = new FileOutputStream(exportBookPath)) {


//                Context context = new Context();
//                context.putVar("disciplines", disciplines);
//                context.putVar("month", symbols.getMonths()[month.getValue()-1]);
//                context.putVar("year", year.toString());
//                context.putVar("group",disciplines.get(0));
//                JxlsHelper.getInstance().processTemplate(is, os, context);
//                Transformer transformer = JxlsHelper.getInstance().createTransformer(is, os);
//                JxlsHelper.getInstance().processTemplate(context,transformer);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                XlsArea xlsArea = new XlsArea(exportBookSettings.get("xlsArea")   /*"ВедомостьМесяцГруппа!A1:AJ10"*/, transformer);
                XlsArea disciplineArea = new XlsArea(exportBookSettings.get("disciplineArea") /*"ВедомостьМесяцГруппа!A9:AJ9"*/, transformer);
                EachCommand disciplineEachCommand = new EachCommand("discipline", "disciplines", disciplineArea);
                xlsArea.addCommand(exportBookSettings.get("disciplineAreaEachArea")/*"A9:AJ9"*/, disciplineEachCommand);


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
                context.putVar("month", MonthsRu.getMonthRu(month.getValue() - 1));
                context.putVar("year", year.toString());
                context.putVar("exportEntityName", exportEntityName);
                xlsArea.applyAt(new CellRef(exportEntityName + "!A1"), context);
                xlsArea.processFormulas();
                transformer.write();
            }
        }


    }

    @SneakyThrows({IOException.class, InvalidFormatException.class})
    public void clearAllZeroCell(String workbookPathname, String sheetName, int startRow, int startColumn) {
        try (InputStream workbookStream = new FileInputStream(new File(workbookPathname))) {

            try (Workbook hssfInputWorkbook = WorkbookFactory.create(workbookStream)) {
                Sheet sheet = hssfInputWorkbook.getSheet(sheetName);
                sheet.autoSizeColumn(startColumn - 1);

                CellStyle style = hssfInputWorkbook.createCellStyle();
                style.setWrapText(true);                                     //auto height


                for (int rowCounter = startRow; rowCounter <= sheet.getLastRowNum(); rowCounter++) {

                    Row row = CellUtil.getRow(rowCounter, sheet);
                    row.setRowStyle(style);
                    row.setHeight((short) -1);
                    if (row.getCell(2) != null) {
                        row.getCell(2).setCellStyle(style);
                    }

                    for (int i = 0; i < 31; i++) { //if 0 cell
                        Cell cell = CellUtil.getCell(row, startColumn + i);


                        if (cell.getNumericCellValue() == 0) {
                            if (cell.getCellTypeEnum() == CellType.FORMULA) {
                                continue;
                            }
                            cell.setCellValue((String) null);
                        }


                    }
                }
                try (OutputStream os = new FileOutputStream(workbookPathname)) {
                    hssfInputWorkbook.write(os);
                }
            }
        }
    }
}

