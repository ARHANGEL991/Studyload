package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.Teacher;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.MonthReporterService;
import com.ggpk.studyload.service.TeacherService;
import com.ggpk.studyload.service.UserPreferencesService;
import com.ggpk.studyload.ui.HomeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static com.ggpk.studyload.controller.GroupMonthReportViewController.toSingleton;

@FXMLController
@Slf4j
public class TeacherMonthReportViewController implements FxInitializable {
    @FXML
    private Text txtPath;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnFolder;

    @FXML
    private ComboBox<String> comboBoxMonth;

    @FXML
    private ComboBox<Teacher> comboBoxTeacher;


    private final MessageSource messageSource;

    private final TeacherService teacherService;

    private final MonthReporterService monthReporterService;


    private final UserPreferencesService userPreferencesService;


    @Autowired
    public TeacherMonthReportViewController(MessageSource messageSource, MonthReporterService monthReporterService, TeacherService teacherService, UserPreferencesService userPreferencesService, HomeView homeView, DisciplineService disciplineService) {
        this.messageSource = messageSource;
        this.monthReporterService = monthReporterService;
        this.teacherService = teacherService;
        this.userPreferencesService = userPreferencesService;

        this.homeView = homeView;
        this.disciplineService = disciplineService;
    }


    private final HomeView homeView;
    private final DisciplineService disciplineService;


    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {

        txtPath.setText(userPreferencesService.getTeacherReportFolderPath());
        comboBoxMonth.getItems().addAll(
                messageSource.getMessage("scene.month.january", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.february", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.march", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.april", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.may", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.june", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.july", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.august", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.september", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.october", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.november", null, Locale.getDefault()),
                messageSource.getMessage("scene.month.december", null, Locale.getDefault())
        );

        comboBoxTeacher.setConverter(new StringConverter<Teacher>() {
            public String toString(Teacher object) {
                String retVal = "";

                if (object != null) {
                    retVal = object.getName();
                }

                return retVal;
            }

            public Teacher fromString(String teacherName) {
                return comboBoxTeacher.getItems().stream()
                        .filter(teacher -> teacher.getName().equalsIgnoreCase(teacherName))
                        .limit(1)
                        .collect(toSingleton());
            }
        });
        comboBoxTeacher.getItems().addAll(teacherService.getAll().stream().sorted(Comparator.comparing(Teacher::getName)).collect(Collectors.toList()));

//        comboBoxTeacher.setEditable(true);
        comboBoxMonth.setEditable(true);
//        TextFields.bindAutoCompletion(comboBoxTeacher.getEditor(), comboBoxTeacher.getItems());
        TextFields.bindAutoCompletion(comboBoxMonth.getEditor(), comboBoxMonth.getItems());

        comboBoxTeacher.getSelectionModel().selectFirst();

        comboBoxMonth.getSelectionModel().selectFirst();

    }


    @FXML
    void doReport(ActionEvent event) {

        Map<String, String> exportTeacherReportSettings = new HashMap<>();
        exportTeacherReportSettings.put("xlsArea", "ВедомостьМесяцПреподаватель!A1:AI10");
        exportTeacherReportSettings.put("disciplineArea", "ВедомостьМесяцПреподаватель!A9:AI9");
        exportTeacherReportSettings.put("disciplineAreaEachArea", "A9:AI9");

        String fileName = "TeachersReports" + comboBoxMonth.getValue() + ".xls";


        doMonthReport(exportTeacherReportSettings, fileName);

    }

    private void doMonthReport(Map<String, String> exportTeacherReportSettings, String fileName) {
        File outputFile = new File(userPreferencesService.getTeacherReportFolderPath(), fileName);

        File copiedFile = null;

        String outputFilePath = outputFile.getAbsolutePath();
        String inputFilePath = userPreferencesService.getTeacherReportTemplateFilePath();

        //If template is in output file it need to copy because one file for 2 streams it's too hard
        if (inputFilePath.equals(outputFilePath) || outputFile.exists() && isWorksheetContainsReports(outputFilePath)) {
            copiedFile = new File(userPreferencesService.getTeacherReportFolderPath(), "Template.xls");
            try {
                Files.copy(outputFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("Error to copy", e);
            }
            inputFilePath = copiedFile.getAbsolutePath();
        }

        monthReporterService.createMonthStatement(Month.of(comboBoxMonth.getItems().indexOf(comboBoxMonth.getValue()) + 1), Year.now(),
                comboBoxTeacher.getSelectionModel().getSelectedItem().getName(),
                disciplineService.getDisciplinesByTeacherName(comboBoxTeacher.getSelectionModel().getSelectedItem().getName()),
                exportTeacherReportSettings,
                inputFilePath,
                outputFilePath);

        monthReporterService.clearAllZeroCell(outputFilePath, comboBoxTeacher.getSelectionModel().getSelectedItem().getName(), 8, 3);

        if (copiedFile != null && !copiedFile.delete()) {
            log.error(MessageFormat.format("File {0} is delete", copiedFile.getPath()));

        }
    }

    @FXML
    void openFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setTeacherReportPath(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    void openFolderFileTemplate(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        final File selectedDirectory = fileChooser.showOpenDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setTeacherReportTemplateFilePath(selectedDirectory.getAbsolutePath());
        }
    }

    @SneakyThrows
    private boolean isWorksheetContainsReports(String worksheetPath) {
        try (InputStream workbookStream = new FileInputStream(new File(worksheetPath))) {

            try (Workbook hssfInputWorkbook = WorkbookFactory.create(workbookStream)) {
                return hssfInputWorkbook.getNumberOfSheets() > 1;
            }
        }
    }
}
