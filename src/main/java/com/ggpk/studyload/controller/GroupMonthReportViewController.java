package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.Group;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.GroupService;
import com.ggpk.studyload.service.MonthReporterService;
import com.ggpk.studyload.service.UserPreferencesService;
import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class GroupMonthReportViewController implements FxInitializable {


    @FXML
    private Text txtPath;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnFolder;

    @FXML
    private ComboBox<String> comboBoxMonth;

    @FXML
    private ComboBox<Group> comboBoxGroup;


    private final MessageSource messageSource;

    private final GroupService groupService;

    private final MonthReporterService monthReporterService;


    private final UserPreferencesService userPreferencesService;

    private final DialogBalloon dialogBalloon;




    @Autowired
    public GroupMonthReportViewController(MessageSource messageSource, MonthReporterService monthReporterService, GroupService groupService, UserPreferencesService userPreferencesService, HomeView homeView, DisciplineService disciplineService, DialogBalloon dialogBalloon) {
        this.messageSource = messageSource;
        this.monthReporterService = monthReporterService;
        this.groupService = groupService;
        this.userPreferencesService = userPreferencesService;

        this.homeView = homeView;
        this.disciplineService = disciplineService;
        this.dialogBalloon = dialogBalloon;
    }


    private final HomeView homeView;
    private final DisciplineService disciplineService;


    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {

        txtPath.setText(userPreferencesService.getGroupReportFolderPath());
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

        comboBoxGroup.getItems().addAll(groupService.getAll());
        comboBoxGroup.setConverter(new StringConverter<Group>() {
            public String toString(Group object) {
                return object.getName();
            }

            public Group fromString(String string) {
                return null;
            }
        });

        comboBoxMonth.getSelectionModel().selectFirst();
    }


    @FXML
    void doReport(ActionEvent event) throws IOException {

        Map<String, String> exportGroupSettings = new HashMap<>();
        exportGroupSettings.put("xlsArea", "ВедомостьМесяцГруппа!A1:AJ10");
        exportGroupSettings.put("disciplineArea", "ВедомостьМесяцГруппа!A9:AJ9");
        exportGroupSettings.put("disciplineAreaEachArea", "A9:AJ9");


        String fileName = "ОтчётыПоГруппе" + comboBoxMonth.getValue() + ".xls";
        File copiedFile = null;

        File outputFile = new File(userPreferencesService.getGroupReportFolderPath(), fileName);

        String outputFilePath = outputFile.getAbsolutePath();
        String inputFilePath = userPreferencesService.getGroupReportTemplateFilePath();

        //If template is in output file it need to copy because one file for 2 streams it's too hard
        if (inputFilePath.equals(outputFilePath) || outputFile.exists() && isWorksheetContainsReports(outputFilePath)) {

            copiedFile = new File(userPreferencesService.getGroupReportFolderPath(), "Template.xls");
            Files.copy(outputFile.toPath(), copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            inputFilePath = copiedFile.getAbsolutePath();
        }

        monthReporterService.createMonthStatement(Month.of(comboBoxMonth.getItems().indexOf(comboBoxMonth.getValue()) + 1), Year.now(),
                comboBoxGroup.getSelectionModel().getSelectedItem().getName(),
                disciplineService.getDisciplinesByGroupName(comboBoxGroup.getSelectionModel().getSelectedItem().getName()),
                exportGroupSettings,
                inputFilePath,
                outputFilePath);

        monthReporterService.clearAllZeroCell(outputFilePath, comboBoxGroup.getSelectionModel().getSelectedItem().getName(), 8, 4);

        if (copiedFile != null && !copiedFile.delete()) {
            log.error(MessageFormat.format("File {0} is delete", copiedFile.getPath()));

        }
        dialogBalloon.succeed(LangProperties.SUCESSED_EXPORTED.getValue());
    }

    @FXML
    void openFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setGroupReportPath(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    void openFolderFileTemplate(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        final File selectedDirectory = fileChooser.showOpenDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setGroupReportTemplateFilePath(selectedDirectory.getAbsolutePath());
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
