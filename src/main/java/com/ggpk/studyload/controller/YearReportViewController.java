package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.Teacher;
import com.ggpk.studyload.service.*;
import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
import com.ggpk.studyload.ui.HomeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import lombok.SneakyThrows;
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
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.ggpk.studyload.controller.GroupMonthReportViewController.toSingleton;

@FXMLController
public class YearReportViewController implements FxInitializable {

    @FXML
    private ComboBox<String> comboBoxTeacher;

    @FXML
    private Text txtPath;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnFolderFileTemplate;

    @FXML
    private Button btnFolder;

    private final UserPreferencesService userPreferencesService;
    private final MessageSource messageSource;
    private final DialogBalloon dialogBalloon;
    private final DisciplineService disciplineService;
    private final TeacherService teacherService;

    private final YearReporterService yearReporterService;

    private final HomeView homeView;


    @Autowired
    public YearReportViewController(UserPreferencesService userPreferencesService,
                                    MessageSource messageSource,
                                    DialogBalloon dialogBalloon,
                                    DisciplineService disciplineService,
                                    TeacherService teacherService,
                                    YearReporterService yearReporterService,
                                    HomeView homeView) {
        this.userPreferencesService = userPreferencesService;
        this.messageSource = messageSource;
        this.dialogBalloon = dialogBalloon;
        this.disciplineService = disciplineService;
        this.teacherService = teacherService;
        this.yearReporterService = yearReporterService;
        this.homeView = homeView;
    }


    @FXML
    void doReport(ActionEvent event) {

        String fileName = "ГодовойОтчёт.xls";
        File outputFile = new File(userPreferencesService.getTeacherReportFolderPath(), fileName);

        List<Discipline> disciplines = disciplineService.getDisciplinesByTeacherName(comboBoxTeacher.getValue());
        if (disciplines.isEmpty()) {
            dialogBalloon.warningMessage(LangProperties.ERROR.getValue(), LangProperties.ERROR_EXPORTING_EMPTY_DISCIPLINE.getValue(), null);
            //Todo exception
            return;
        }
        String yearReportTemplateFilePath = userPreferencesService.getYearReportTemplateFilePath();
        String yearReportPath = outputFile.getAbsolutePath();


        if (outputFile.exists() && isWorksheetContainsReports(yearReportPath)) {

            yearReportTemplateFilePath = yearReportPath;
        }

        yearReporterService.createYearStatement(
                Year.now(),
                comboBoxTeacher.getValue(),
                disciplines,
                yearReportTemplateFilePath,
                yearReportPath
        );
        dialogBalloon.succeed(LangProperties.SUCESSED_EXPORTED.getValue());

    }

    @FXML
    void openFolderFileTemplate(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        final File selectedFile = fileChooser.showOpenDialog(homeView.getView().getScene().getWindow());
        if (selectedFile != null) {
            txtPath.setText(selectedFile.getAbsolutePath());
            userPreferencesService.setYearReportTemplateFilePath(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void openFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setYearReportPath(selectedDirectory.getAbsolutePath());
        }
    }

    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        txtPath.setText(userPreferencesService.getYearReportTemplateFilePath());


        comboBoxTeacher.getItems().addAll(teacherService.getAll().stream().map(Teacher::getName).collect(Collectors.toList()));
        comboBoxTeacher.setEditable(true);
        TextFields.bindAutoCompletion(comboBoxTeacher.getEditor(), comboBoxTeacher.getItems());
//        comboBoxTeacher.getSelectionModel().selectFirst();
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
