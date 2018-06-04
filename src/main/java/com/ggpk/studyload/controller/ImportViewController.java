package com.ggpk.studyload.controller;

import com.ggpk.studyload.service.*;
import com.ggpk.studyload.ui.HomeView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class ImportViewController implements FxInitializable {


    @FXML
    private TextField txtPath;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnFile;

    @FXML
    private Spinner<Integer> spinnerSheetNumber;


    private final UserPreferencesService userPreferencesService;

    private final AcademicYearSheetConverter academicYearSheetConverter;

    private final HomeView homeView;

    private final AcademicPlanReader academicPlanReader;

    private final DisciplineService disciplineService;

    private final AcademicYearService academicYearService;

    private final GroupService groupService;

    private final TeacherService teacherService;

    private final Environment environment;

    private final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;


    @Autowired
    public ImportViewController(UserPreferencesService userPreferencesService,
                                AcademicYearSheetConverter academicYearSheetConverter,
                                HomeView homeView, AcademicPlanReader academicPlanReader,
                                DisciplineService disciplineService,
                                AcademicYearService academicYearService,
                                GroupService groupService,
                                TeacherService teacherService,
                                Environment environment,
                                JdbcTemplate jdbcTemplate,
                                DataSource dataSource) {
        this.userPreferencesService = userPreferencesService;
        this.academicYearSheetConverter = academicYearSheetConverter;
        this.homeView = homeView;
        this.academicPlanReader = academicPlanReader;
        this.disciplineService = disciplineService;
        this.academicYearService = academicYearService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }


    public void doClose(ActionEvent event) {

    }

    public void initialize(URL location, ResourceBundle resources) {
        txtPath.setText(userPreferencesService.getImportPath());
        spinnerSheetNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100000, 1));
    }

    @FXML
    void doImport(ActionEvent event) {

        backupDB();

        disciplineService.deleteAll();
        groupService.deleteAll();
        academicYearService.deleteAll();
        teacherService.deleteAll();
        academicYearService.save(academicYearSheetConverter
                .convertImportAcademicEntity(
                        academicPlanReader.getAcademicYearFromXls(
                                userPreferencesService.getImportPath(),
                                spinnerSheetNumber.getValue()
                        )
                ));


    }

    private void backupDB() {
        String backupFile = "studyLoad" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss")) + ".zip";
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.execute("BACKUP TO '" + backupFile + "'");
    }

    @FXML
    void openFile(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        final File selectedDirectory = fileChooser.showOpenDialog(homeView.getView().getScene().getWindow());
        if (selectedDirectory != null) {
            txtPath.setText(selectedDirectory.getAbsolutePath());
            userPreferencesService.setImportPath(selectedDirectory.getAbsolutePath());
        }
    }
}
