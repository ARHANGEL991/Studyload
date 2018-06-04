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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.File;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
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


    /**
     * todo
     */
    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {

        txtPath.setText(userPreferencesService.getTeacherReportPath());
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

        comboBoxTeacher.getItems().addAll(teacherService.getAll());
        comboBoxTeacher.setConverter(new StringConverter<Teacher>() {
            public String toString(Teacher object) {
                return object.getName();
            }

            public Teacher fromString(String string) {
                return null;
            }
        });

    }


    @FXML
    void doReport(ActionEvent event) {

        Map<String, String> exportGroupSettings = new HashMap<>();
        exportGroupSettings.put("xlsArea", "ВедомостьМесяцПреподаватель!A1:AI10");
        exportGroupSettings.put("disciplineArea", "ВедомостьМесяцПреподаватель!A9:AI9");
        exportGroupSettings.put("disciplineAreaEachArea", "A9:AI9");

        String fileName = "TeachersReports.xls";

        doMonthReport(exportGroupSettings, fileName);
    }

    private void doMonthReport(Map<String, String> exportGroupSettings, String fileName) {
        File outputFile = new File(userPreferencesService.getTeacherReportPath(), fileName);

        monthReporterService.createMonthStatement(Month.of(comboBoxMonth.getItems().indexOf(comboBoxMonth.getValue()) + 1), Year.now(),
                comboBoxTeacher.getSelectionModel().getSelectedItem().getName(),
                disciplineService.getDisciplinesByTeacherName(comboBoxTeacher.getSelectionModel().getSelectedItem().getName()),
                exportGroupSettings,
                userPreferencesService.getTeacherReportTemplateFilePath(),
                outputFile.getAbsolutePath());

        monthReporterService.clearAllZeroCell(outputFile.getAbsolutePath(), comboBoxTeacher.getSelectionModel().getSelectedItem().getName(), 8, 3);
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
}
