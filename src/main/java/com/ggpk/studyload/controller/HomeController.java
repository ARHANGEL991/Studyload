package com.ggpk.studyload.controller;

import com.ggpk.studyload.StudyLoadApplication;
import com.ggpk.studyload.ui.ImportView;
import com.ggpk.studyload.ui.event.ShowViewEvent;
import com.ggpk.studyload.ui.masterdata.PlanDataView;
import com.ggpk.studyload.ui.masterdata.ProofReaderView;
import com.ggpk.studyload.ui.report.MonthGroupReportView;
import com.ggpk.studyload.ui.report.MonthTeacherReportView;
import com.ggpk.studyload.ui.report.YearReportView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class HomeController implements FxInitializable {

    private final StudyLoadApplication application;


    @FXML
    private Menu menuSettings;

    @FXML
    private TitledPane menuMasterData;

    @FXML
    private Button btnPlan;

    @FXML
    private Button btnTeacherHours;

    @FXML
    private TitledPane menuImport;

    @FXML
    private Button btnImportPlan;

    @FXML
    private TitledPane menuReports;

    @FXML
    private Button btnReportMonthTeacher;

    @FXML
    private Button btnReportMonthGroup;

    @FXML
    private Button btnYearReport;

    @FXML
    private BorderPane content;

    private final PlanDataView planDataView;

    private final ProofReaderView proofReaderView;

    private final MonthTeacherReportView monthTeacherReportView;

    private final ImportView importView;


    private final ProofReaderViewController proofReaderViewController;

    private final PlanViewController planViewController;

    private final MonthGroupReportView monthGroupReportView;

    private final YearReportView yearReportView;

    @Autowired
    public HomeController(StudyLoadApplication application,
                          PlanDataView planDataView,
                          PlanViewController planViewController,
                          ProofReaderView proofReaderView,
                          ProofReaderViewController proofReaderViewController,
                          MonthTeacherReportView monthTeacherReportView,
                          ImportView importView,
                          MonthGroupReportView monthGroupReportView,
                          YearReportView yearReportView) {
        this.application = application;
        this.planDataView = planDataView;
        this.planViewController = planViewController;
        this.proofReaderView = proofReaderView;
        this.proofReaderViewController = proofReaderViewController;
        this.monthTeacherReportView = monthTeacherReportView;
        this.importView = importView;
        this.monthGroupReportView = monthGroupReportView;
        this.yearReportView = yearReportView;
    }

    @FXML
    public void doClose(ActionEvent event) {
        try {
            Platform.exit();
        } catch (Exception e) {
            System.exit(0);
            log.error("App exit failed", e);

        }
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMessageSource(MessageSource messageSource) {

    }


    public void showSceneInMenu(Parent stage) {
        this.content.setCenter(stage);
        this.content.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    @EventListener
    public void showSceneInMenuEventListener(ShowViewEvent event) {
        showSceneInMenu(event.getFxmlView().getView());
    }


    @FXML
    void showImportMenu(ActionEvent event) {
        showSceneInMenu(importView.getView());
    }

    @FXML
    void showPlan(ActionEvent event) {
        showSceneInMenu(planDataView.getView());
        planViewController.loadData();

    }

    @FXML
    void showReportGroupStatement(ActionEvent event) {
        showSceneInMenu(monthGroupReportView.getView());
    }

    @FXML
    void showReportTeacherStatement(ActionEvent event) {
        showSceneInMenu(monthTeacherReportView.getView());
    }

    @FXML
    void showReportYearStatement(ActionEvent event) {
        showSceneInMenu(yearReportView.getView());
    }

    @FXML
    void showTeacherHours(ActionEvent event) {
        showSceneInMenu(proofReaderView.getView());
        proofReaderViewController.loadData();
    }


}
