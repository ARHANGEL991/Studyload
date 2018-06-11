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
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.File;
import java.net.URL;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.ggpk.studyload.controller.GroupMonthReportViewController.toSingleton;

@FXMLController
public class YearReportViewController implements FxInitializable {

    @FXML
    private ComboBox<Teacher> comboBoxTeacher;

    @FXML
    private Text txtPath;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnFolderFileTemplate;

    @FXML
    private Spinner<Integer> templateSheetIndexSpinner;

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

        List<Discipline> disciplines = disciplineService.getDisciplinesByTeacherName(comboBoxTeacher.getValue().getName());
        yearReporterService.createYearStatement(
                Year.now(),
                comboBoxTeacher.getValue().getName(),
                disciplines,
                templateSheetIndexSpinner.getValue(),
                userPreferencesService.getYearReportTemplateFilePath(),
                userPreferencesService.getYearReportTemplateFilePath()
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

    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        templateSheetIndexSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100000, 1));
        txtPath.setText(userPreferencesService.getTeacherReportFolderPath());

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
        comboBoxTeacher.getSelectionModel().selectFirst();
    }
}
