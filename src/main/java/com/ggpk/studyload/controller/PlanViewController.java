package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.BaseEntity;
import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.DisciplineTerm;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.IRepository;
import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.TableViewColumnAction;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
import com.ggpk.studyload.service.ui.notifications.DialogWindow;
import com.ggpk.studyload.util.TableCellInitializeUtil;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@FXMLController
@Slf4j
public class PlanViewController implements FxInitializable, TableDataController {
    @FXML
    private TableView<Discipline> tableView;

    @FXML
    private TableColumn<Discipline, String> columnGroupName;

    @FXML
    private TableColumn<Discipline, String> columnDisciplineName;

    @FXML
    private TableColumn<Discipline, Integer> columnGroupStudentCount;

    @FXML
    private TableColumn<Discipline, Double> columnSumHours;

    @FXML
    private TableColumn<Discipline, Integer> columnAdditionalControl;

    @FXML
    private TableColumn<Discipline, String> columnDisciplineType;

    @FXML
    private TableColumn<Discipline, String> columnGroupType;

    @FXML
    private TableColumn<Discipline, String> columnTeacherName;

    @FXML
    private TableColumn<?, ?> columnAction;

    @FXML
    private TextField txtFirstTermHoursWithRemoval;

    @FXML
    private TextField txtFirstTermCourseDesign;

    @FXML
    private TextField txtFirstTermConsultations;

    @FXML
    private TextField txtFirstTermTestsCount;

    @FXML
    private TextField txtFirstTermExamHours;

    @FXML
    private TextField txtFirstTermWeeksInTerm;

    @FXML
    private TextField txtFirstTermHoursInWeek;

    @FXML
    private TextField txtFirstTermTotalHours;

    @FXML
    private TextField txtFirstTermAcceptanceOfCP;

    @FXML
    private TextField txtFirstTermLaboratoryPracticalE;

    @FXML
    private TextField txtSecondTermHoursWithRemoval;

    @FXML
    private TextField txtSecondTermCourseDesign;

    @FXML
    private TextField txtSecondTermConsultations;

    @FXML
    private TextField txtSecondTermTestsCount;

    @FXML
    private TextField txtSecondTermExamHours;

    @FXML
    private TextField txtSecondTermWeeksInTerm;

    @FXML
    private TextField txtSecondTermHoursInWeek;

    @FXML
    private TextField txtSecondTermTotalHours;

    @FXML
    private TextField txtSecondTermAcceptanceOfCP;

    @FXML
    private TextField txtSecondTermLaboratoryPracticalE;

    @FXML
    private TextField txtSearch;


    private List<TextField> firstTermTextFields;
    private List<TextField> secondTermTextFields;

    private final DisciplineService disciplineService;

    private final DialogBalloon dialogBalloon;

    private final DialogWindow dialogWindow;

    private final TableViewColumnAction tableViewColumnAction;


    private Discipline discipline;


    private final ApplicationEventPublisher applicationEventPublisher;

    private ObservableList<Discipline> disciplines;


    @Autowired
    public PlanViewController(DisciplineService disciplineService,
                              DialogBalloon dialogBalloon,
                              DialogWindow dialogWindow,
                              TableViewColumnAction tableViewColumnAction,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.disciplineService = disciplineService;
        this.dialogBalloon = dialogBalloon;
        this.dialogWindow = dialogWindow;
        this.tableViewColumnAction = tableViewColumnAction;
        this.applicationEventPublisher = applicationEventPublisher;

    }


    public void doClose(ActionEvent event) {

    }


    public void initialize(URL location, ResourceBundle resources) {


        initColumns();
        initTxtFields();
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> doSearch(null));


    }

    private void initColumns() {

        TableCellInitializeUtil.columnGroupNameInitialize(columnGroupName);

        columnDisciplineName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableCellInitializeUtil.columnSumHoursInitialize(columnSumHours);

        TableCellInitializeUtil.columnAdditionalControlInitialize(columnAdditionalControl);

        TableCellInitializeUtil.columnGroupStudentCountInitialize(columnGroupStudentCount);

        TableCellInitializeUtil.columnDisciplineTypeInitialize(columnDisciplineType);

        TableCellInitializeUtil.columnGroupTypeInitialize(columnGroupType);

        TableCellInitializeUtil.columnTeacherNameInitialize(columnTeacherName);

        columnAction.setCellFactory(param -> new com.ggpk.studyload.controller.ColumnActionDiscipline(tableView,
                tableViewColumnAction,
                dialogBalloon,
                dialogWindow,
                disciplineService,
                this::loadData));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                discipline = newValue;
                showData(newValue);
            } else {
                discipline = null;
                clearAllFields();
            }

        });

    }

    public void searchData() {
        //TODO
    }

    @FXML
    void doSearch(ActionEvent event) {
        tableView.setItems(disciplines);

        if (!txtSearch.getText().isEmpty()) {

            tableView.setItems(tableView.getItems().stream()
                    .filter(discip ->
                            discip.getGroup().getName().contains(txtSearch.getText())
                                    || discip.getFullGroup().getTeacher().getName().contains(txtSearch.getText())
                                    || discip.getName().contains(txtSearch.getText()))
                    .collect(Collectors.collectingAndThen(toList(), FXCollections::observableArrayList))
            );
        }
    }

    private void initTxtFields() {

        if (firstTermTextFields == null)
            firstTermTextFields = new ArrayList<>(Arrays.asList(txtFirstTermAcceptanceOfCP,
                    txtFirstTermConsultations,
                    txtFirstTermCourseDesign,
                    txtFirstTermExamHours,
                    txtFirstTermHoursInWeek,
                    txtFirstTermHoursWithRemoval,
                    txtFirstTermLaboratoryPracticalE,
                    txtFirstTermTestsCount,
                    txtFirstTermTotalHours,
                    txtFirstTermWeeksInTerm));

        if (secondTermTextFields == null)
            secondTermTextFields = new ArrayList<>(Arrays.asList(txtSecondTermAcceptanceOfCP,
                    txtSecondTermConsultations,
                    txtSecondTermCourseDesign,
                    txtSecondTermExamHours,
                    txtSecondTermHoursInWeek,
                    txtSecondTermHoursWithRemoval,
                    txtSecondTermLaboratoryPracticalE,
                    txtSecondTermTestsCount,
                    txtSecondTermTotalHours,
                    txtSecondTermWeeksInTerm));
    }

    private void clearAllFields() {
        clearTextFields(firstTermTextFields);
        clearTextFields(secondTermTextFields);
    }

    private void showData(Discipline newValue) {

        if (newValue.getFullGroup().getFirstTerm() != null) {
            showFirstTermTxt(discipline.getFullGroup().getFirstTerm(), discipline.getFullGroup().getStudentCount());
        } else {
            clearTextFields(firstTermTextFields);
        }

        if (newValue.getFullGroup().getSecondTerm() != null) {
            showSecondTermTxt(discipline.getFullGroup().getSecondTerm(), discipline.getFullGroup().getStudentCount());
        } else {
            clearTextFields(secondTermTextFields);
        }
    }

    private void showFirstTermTxt(DisciplineTerm term, int studentsCount) {

        if (term != null) {
            txtFirstTermAcceptanceOfCP.setText(Double.toString(term.getAcceptanceOfCourseProjects(studentsCount)));
            txtFirstTermConsultations.setText(Integer.toString(term.getConsultations()));
            txtFirstTermCourseDesign.setText(Integer.toString(term.getCourseDesign()));
            txtFirstTermExamHours.setText(Double.toString(term.getExamHours(studentsCount)));
            txtFirstTermHoursInWeek.setText(Double.toString(term.getHoursInWeek()));
            txtFirstTermHoursWithRemoval.setText(Double.toString(term.getTotalHoursWithRemoval()));
            txtFirstTermLaboratoryPracticalE.setText(Integer.toString(term.getLaboratoryPracticalExercises()));
            txtFirstTermTestsCount.setText(Integer.toString(term.getTestsCount()));
            txtFirstTermTotalHours.setText(Double.toString(term.getTotalHours()));
            txtFirstTermWeeksInTerm.setText(Double.toString(term.getWeeksInTerm()));
        }

    }

    private void showSecondTermTxt(DisciplineTerm term, int studentsCount) {
        if (term != null) {
            txtSecondTermConsultations.setText(Integer.toString(term.getConsultations()));
            txtSecondTermCourseDesign.setText(Integer.toString(term.getCourseDesign()));
            txtSecondTermExamHours.setText(Double.toString(term.getExamHours(studentsCount)));
            txtSecondTermHoursInWeek.setText(Double.toString(term.getHoursInWeek()));
            txtSecondTermHoursWithRemoval.setText(Double.toString(term.getTotalHoursWithRemoval()));
            txtSecondTermLaboratoryPracticalE.setText(Integer.toString(term.getLaboratoryPracticalExercises()));
            txtSecondTermTestsCount.setText(Integer.toString(term.getTestsCount()));
            txtSecondTermTotalHours.setText(Double.toString(term.getTotalHours()));
            txtSecondTermWeeksInTerm.setText(Double.toString(term.getWeeksInTerm()));
            txtSecondTermAcceptanceOfCP.setText(Double.toString(term.getAcceptanceOfCourseProjects(studentsCount)));
        }

    }

    private void clearTextFields(List<TextField> textField) {
        textField.forEach(TextInputControl::clear);

    }


    @FXML
    public void loadData() {

        try {
            tableView.getItems().clear();

            dialogWindow.loading(tableView.getItems(), disciplineService::getAll, LangProperties.LIST_OF_DISCIPLINES.getValue());
            tableView.requestFocus();
            disciplines = tableView.getItems();
        } catch (Exception e) {
            dialogWindow.errorLoading(LangProperties.LIST_OF_DISCIPLINES.getValue(), e);
            log.error("Load data error", e);
        }
    }

    //todo
    @FXML
    void newItem(ActionEvent event) {

    }

    //todo
    @FXML
    void saveData(ActionEvent event) {

    }


    @FXML
    void tableViewClearSelection(ActionEvent event) {
        tableView.getSelectionModel().clearSelection();

    }


    class ColumnAction<T extends BaseEntity> extends TableCell<T, String> {

        private TableView<T> table;
        private TableViewColumnAction actionColumn;
        private final HomeController homeController;
        private IRepository<T, Long> service;
        private DialogWindow dialogWindow;
        private DialogBalloon dialogBalloon;
        private ActionEvent actionEvent;
        private DataEditController<T> action;
        private LangProperties propertyName;
        private AbstractFxmlView fxmlView;


        public ColumnAction(TableView<T> tableView,
                            TableViewColumnAction actionColumn,
                            HomeController homeController,
                            IRepository<T, Long> service,
                            DialogWindow dialogWindow,
                            DialogBalloon dialogBalloon,
                            ActionEvent actionEvent, DataEditController<T> action,
                            LangProperties propertyName, AbstractFxmlView fxmlView) {
            this.table = tableView;
            this.actionColumn = actionColumn;
            this.homeController = homeController;
            this.service = service;
            this.dialogWindow = dialogWindow;
            this.dialogBalloon = dialogBalloon;
            this.actionEvent = actionEvent;
            this.action = action;
            this.propertyName = propertyName;
            this.fxmlView = fxmlView;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty)
                setGraphic(null);
            else {
                T entity = table.getItems().get(getIndex());
                setGraphic(actionColumn.getDefaultTableModel());
                actionColumn.getUpdateLink().setOnAction(event -> {
                    homeController.showSceneInMenu(fxmlView.getView());
                    action.updateData(entity);
                });
                actionColumn.getDeleteLink().setOnAction((ActionEvent event) -> {
                    if (dialogWindow.confirmDelete(propertyName.getValue(), entity.getClass().getFields()[1].toString(),
                            LangProperties.ID.getValue(), entity.getId())
                            .get() == ButtonType.OK) {
                        try {
                            service.delete(entity);
                            loadData();
                            dialogBalloon.succeedRemoved(propertyName.getValue(), entity.getId());
                        } catch (Exception e) {
                            dialogWindow.errorRemoved(propertyName.getValue(), LangProperties.ID.getValue(), entity.getId(), e);
                            log.error("Delete fail", e);
                        }
                    }
                });
            }

        }
    }


    class ColumnActionDiscipline<T> extends TableCell<T, String> {

        private TableView table;

        public ColumnActionDiscipline(TableView table) {
            this.table = table;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Discipline discipline = (Discipline) table.getItems().get(getIndex());
                setGraphic(tableViewColumnAction.getDefaultTableModel());
//                tableViewColumnAction.getUpdateLink().setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        CustomerDataAction exitsData = springContext.getBean(CustomerDataAction.class);
//                        homeController.showSceneInMenu();
//                        exitsData.exitsData(discipline);
//                    }
//                });
                tableViewColumnAction.getDeleteLink().setOnAction((ActionEvent event) -> {

                    if (dialogWindow.confirmDelete(
                            LangProperties.DATA_DISCIPLINE.getValue(),
                            discipline.getName(), LangProperties.ID.getValue(), discipline.getId()
                    ).get() == ButtonType.OK) {
                        try {
                            disciplineService.delete(discipline);
                            tableView.getItems().remove(discipline);
                            dialogBalloon.succeedRemoved(LangProperties.DATA_DISCIPLINE.getValue(), discipline.getName());
                        } catch (Exception e) {
                            dialogWindow.errorRemoved(LangProperties.DATA_DISCIPLINE.getValue(),
                                    LangProperties.ID.getValue(), discipline.getId(), e);
                            log.error("Delete error", e);
                        }
                    }
                });
            }
        }
    }

}
