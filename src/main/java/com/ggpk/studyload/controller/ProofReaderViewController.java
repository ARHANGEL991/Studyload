package com.ggpk.studyload.controller;


import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.additional.YearDisciplineAccounting;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.ui.TableViewColumnAction;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
import com.ggpk.studyload.service.ui.notifications.DialogWindow;
import com.ggpk.studyload.ui.event.ShowViewEvent;
import com.ggpk.studyload.ui.masterdata.ProofReaderAddView;
import com.ggpk.studyload.util.EditCell;
import com.ggpk.studyload.util.TableCellInitializeUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.DoubleStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@FXMLController
@Slf4j
public class ProofReaderViewController implements FxInitializable, TableDataController, DataEditController<Discipline> {
    @FXML
    private TableView<Discipline> tableView;

    @FXML
    private TableColumn<Discipline, String> columnGroupType;

    @FXML
    private TableColumn<Discipline, String> columnGroupName;

    @FXML
    private TableColumn<Discipline, Integer> columnAdditionalControl;

    @FXML
    private TableColumn<Discipline, String> columnTeacherName;

    @FXML
    private TableColumn<Discipline, String> columnDisciplineName;

    @FXML
    private TableColumn<Discipline, Integer> columnGroupStudentCount;

    @FXML
    private TableColumn<Discipline, Double> column1;

    @FXML
    private TableColumn<Discipline, Double> column2;

    @FXML
    private TableColumn<Discipline, Double> column3;

    @FXML
    private TableColumn<Discipline, Double> column4;

    @FXML
    private TableColumn<Discipline, Double> column5;

    @FXML
    private TableColumn<Discipline, Double> column6;

    @FXML
    private TableColumn<Discipline, Double> column7;

    @FXML
    private TableColumn<Discipline, Double> column8;

    @FXML
    private TableColumn<Discipline, Double> column9;

    @FXML
    private TableColumn<Discipline, Double> column10;

    @FXML
    private TableColumn<Discipline, Double> column11;

    @FXML
    private TableColumn<Discipline, Double> column12;

    @FXML
    private TableColumn<Discipline, Double> column13;

    @FXML
    private TableColumn<Discipline, Double> column14;

    @FXML
    private TableColumn<Discipline, Double> column15;

    @FXML
    private TableColumn<Discipline, Double> column16;

    @FXML
    private TableColumn<Discipline, Double> column17;

    @FXML
    private TableColumn<Discipline, Double> column18;

    @FXML
    private TableColumn<Discipline, Double> column19;

    @FXML
    private TableColumn<Discipline, Double> column20;

    @FXML
    private TableColumn<Discipline, Double> column21;

    @FXML
    private TableColumn<Discipline, Double> column22;

    @FXML
    private TableColumn<Discipline, Double> column23;

    @FXML
    private TableColumn<Discipline, Double> column24;

    @FXML
    private TableColumn<Discipline, Double> column25;

    @FXML
    private TableColumn<Discipline, Double> column26;

    @FXML
    private TableColumn<Discipline, Double> column27;

    @FXML
    private TableColumn<Discipline, Double> column28;

    @FXML
    private TableColumn<Discipline, Double> column29;

    @FXML
    private TableColumn<Discipline, Double> column30;

    @FXML
    private TableColumn<Discipline, Double> column31;

    @FXML
    private TableColumn<Discipline, Double> columnMonthSum;

    @FXML
    private TableColumn<Discipline, Double> columnYearSum;

    @FXML
    private TableColumn<?, ?> columnAction;

    @FXML
    private TextField txtSearch;

    @FXML
    private ChoiceBox<String> choiseBoxMonth;

    @FXML
    private RadioButton radioBtnWithoutDK;

    @FXML
    private ToggleGroup termGroup;


    private ChoiceBox<?> choiseBoxMonthSaved;

    private TextField txtSearchSaved;


    private List<TableColumn<Discipline, Double>> daysColumns;

    private List<Discipline> disciplines;

    private Discipline discipline;

    private final DialogBalloon dialogBalloon;
    private final DialogWindow dialogWindow;
    private final TableViewColumnAction tableViewColumnAction;
    private final ApplicationEventPublisher applicationEventPublisher;


    private final DisciplineService disciplineService;
    private boolean isEdited;

    private final ProofReaderAddView proofReaderAddView;

    private final MessageSource messageSource;


    @Autowired
    public ProofReaderViewController(DisciplineService disciplineService, DialogBalloon dialogBalloon, DialogWindow dialogWindow, TableViewColumnAction tableViewColumnAction, ApplicationEventPublisher applicationEventPublisher, ProofReaderAddView proofReaderAddView, MessageSource messageSource) {
        this.disciplineService = disciplineService;
        this.dialogBalloon = dialogBalloon;
        this.dialogWindow = dialogWindow;
        this.tableViewColumnAction = tableViewColumnAction;
        this.applicationEventPublisher = applicationEventPublisher;
        this.proofReaderAddView = proofReaderAddView;
        this.messageSource = messageSource;
    }


    public void newData() {

    }

    public void updateData(Discipline obj) {

    }

    public void doClose(ActionEvent event) {
        if (isEdited) {
            log.warn("U can lost all data");
        }

    }

    public void initialize(URL location, ResourceBundle resources) {

        initializeDaysColumns();

        setColumnsValueFactory();


        setMonthDayColumnsCellFactory();

        choiseBoxMonth.getItems().addAll(
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

        choiseBoxMonth.getSelectionModel().selectFirst();
        setSumColumnsCellFactory();


    }

    private void setSumColumnsCellFactory() {
        columnMonthSum.setCellValueFactory(param -> {
            SimpleObjectProperty<Double> retVal = new SimpleObjectProperty<>();
            if (param != null) {
                double sum;

                if (radioBtnWithoutDK.isSelected()) {
                    sum = param.getValue()
                            .getFullGroup()
                            .getYearDisciplineAccounting()
                            .getMonthAccountingSum(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1));
                } else {
                    sum = param.getValue()
                            .getFullGroup()
                            .getYearDisciplineAccountingAdditionalControl()
                            .getMonthAccountingSum(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1));
                }

                if (sum > 0) {
                    retVal = new SimpleObjectProperty<>(sum);
                }

            }
            return retVal;
        });

        columnYearSum.setCellValueFactory(param -> {
            SimpleObjectProperty<Double> retVal = new SimpleObjectProperty<>();
            if (param != null) {
                double sum;

                if (radioBtnWithoutDK.isSelected()) {
                    sum = param.getValue()
                            .getFullGroup()
                            .getYearDisciplineAccounting()
                            .getYearAccountingSum();
                } else {
                    sum = param.getValue()
                            .getFullGroup()
                            .getYearDisciplineAccountingAdditionalControl()
                            .getYearAccountingSum();
                }

                if (sum > 0) {
                    retVal = new SimpleObjectProperty<>(sum);
                }

            }
            return retVal;
        });
    }

    private void setMonthDayColumnsCellFactory() {
        int i = 0;
        for (TableColumn<Discipline, Double> column : daysColumns) {
            //necessary for lambda
            int finalI = i;


            column.setCellValueFactory(param -> {

                SimpleObjectProperty<Double> retVal = new SimpleObjectProperty<>();
                if (param != null) {
                    double hoursInDay;
                    if (radioBtnWithoutDK.isSelected()) {


                        hoursInDay = param.getValue()
                                .getFullGroup()
                                .getYearDisciplineAccounting()
                                .getMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1))[finalI];//get index of selected month

                    } else {
                        hoursInDay = param.getValue()
                                .getFullGroup()
                                .getYearDisciplineAccountingAdditionalControl()
                                .getMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1))[finalI];  //get index of selected month and then get monthacc

                    }

                    if (hoursInDay > 0) {
                        retVal = new SimpleObjectProperty<>(hoursInDay);
                    }
                }


                return retVal;

            });

            column.setCellFactory(EditCell.forTableColumn(new DoubleStringConverter()));

            column.setOnEditCommit(event -> {
                //for saving

                isEdited = true;
                double[] monthAccounting;
                if (event.getTablePosition() != null) {
                    if (radioBtnWithoutDK.isSelected()) {
                        if (event.getNewValue() != null) {
                            monthAccounting = event.getTableView().getItems().get(event.getTablePosition().getRow())
                                    .getFullGroup()
                                    .getYearDisciplineAccounting()
                                    .getMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1));

                            monthAccounting[finalI] = event.getNewValue();

                            event.getTableView().getItems().get(event.getTablePosition().getRow())
                                    .getFullGroup()
                                    .getYearDisciplineAccounting().setMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1), monthAccounting);
                            event.getTableView().getColumns().get(0).setVisible(false);
                            event.getTableView().getColumns().get(0).setVisible(true);
                        }

                    } else {
                        if (event.getNewValue() != null) {
                            monthAccounting = event.getTableView().getItems().get(event.getTablePosition().getRow())
                                    .getFullGroup()
                                    .getYearDisciplineAccountingAdditionalControl()
                                    .getMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1));

                            monthAccounting[finalI] = event.getNewValue();

                            event.getTableView().getItems().get(event.getTablePosition().getRow())
                                    .getFullGroup()
                                    .getYearDisciplineAccountingAdditionalControl().setMonthAccounting(Month.of(choiseBoxMonth.getItems().indexOf(choiseBoxMonth.getValue()) + 1), monthAccounting);
                            event.getTableView().getColumns().get(0).setVisible(false);
                            event.getTableView().getColumns().get(0).setVisible(true);
                        }

                    }


                }

            });


            i++;
        }
    }

    private void setColumnsValueFactory() {
        TableCellInitializeUtil.columnGroupNameInitialize(columnGroupName);

        columnDisciplineName.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableCellInitializeUtil.columnAdditionalControlInitialize(columnAdditionalControl);

        TableCellInitializeUtil.columnGroupStudentCountInitialize(columnGroupStudentCount);


        TableCellInitializeUtil.columnGroupTypeInitialize(columnGroupType);

        TableCellInitializeUtil.columnTeacherNameInitialize(columnTeacherName);


        columnAction.setCellFactory(param -> new ColumnActionHideDiscipline(tableView, tableViewColumnAction, dialogBalloon, dialogWindow, disciplineService, this::searchData));

    }

    private void initializeDaysColumns() {
        if (daysColumns == null) {

            daysColumns = new ArrayList<>();
            daysColumns.add(column1);
            daysColumns.add(column2);
            daysColumns.add(column3);
            daysColumns.add(column4);
            daysColumns.add(column5);
            daysColumns.add(column6);
            daysColumns.add(column7);
            daysColumns.add(column8);
            daysColumns.add(column9);
            daysColumns.add(column10);
            daysColumns.add(column11);
            daysColumns.add(column12);
            daysColumns.add(column13);
            daysColumns.add(column14);
            daysColumns.add(column15);
            daysColumns.add(column16);
            daysColumns.add(column17);
            daysColumns.add(column18);
            daysColumns.add(column19);
            daysColumns.add(column20);
            daysColumns.add(column21);
            daysColumns.add(column22);
            daysColumns.add(column23);
            daysColumns.add(column24);
            daysColumns.add(column25);
            daysColumns.add(column26);
            daysColumns.add(column27);
            daysColumns.add(column28);
            daysColumns.add(column29);
            daysColumns.add(column30);
            daysColumns.add(column31);
        }
    }

    @FXML
    public void loadData() {

    }

    @FXML
    public void searchData() {

        disciplines = disciplineService.getDisciplinesByGroupNameLike(txtSearch.getText().trim());

        if (disciplines.isEmpty()) {
            disciplines = disciplineService.getDisciplinesByTeacherNameLike(txtSearch.getText());
        }
        if (radioBtnWithoutDK.isSelected()) {

            disciplines = disciplines.stream().peek(discipline1 -> {
                if (discipline1.getFullGroup().getYearDisciplineAccounting() == null) {
                    discipline1.getFullGroup().setYearDisciplineAccounting(new YearDisciplineAccounting());
                }

            }).collect(Collectors.toList());

        } else {
            //delete all if addControl is less 1
            disciplines = disciplines.stream()
                    .filter(discipline1 -> discipline1.getFullGroup().getAdditionalControl() > 0)
                    .peek(discipline1 -> {
                        if (discipline1.getFullGroup().getYearDisciplineAccountingAdditionalControl() == null) {
                            discipline1.getFullGroup().setYearDisciplineAccountingAdditionalControl(new YearDisciplineAccounting());
                        }
                    })
                    .collect(Collectors.toList());
        }

        tableView.getItems().clear();
        tableView.getItems().addAll(disciplines);
        log.info(String.format("TableView items count %1$s", tableView.getItems().size()));
    }

    @FXML
    public void newItem(ActionEvent event) {
        applicationEventPublisher.publishEvent(new ShowViewEvent<>(this, proofReaderAddView));
    }


    @FXML
    public void saveData(ActionEvent event) {
        if (!tableView.getItems().isEmpty()) {

            disciplineService.saveAll(tableView.getItems());
            isEdited = false;
        }
    }

    @FXML
    public void tableViewClearSelection(ActionEvent event) {
        tableView.getSelectionModel().clearSelection();
    }

    class ColumnActionHideDiscipline<T> extends TableCell<T, String> {

        private TableView table;
        private TableViewColumnAction tableViewColumnAction;
        private DialogBalloon dialogBalloon;
        private DialogWindow dialogWindow;
        private DisciplineService disciplineService;
        private Runnable loadData;

        public ColumnActionHideDiscipline(TableView table,
                                          TableViewColumnAction tableViewColumnAction,
                                          DialogBalloon dialogBalloon,
                                          DialogWindow dialogWindow,
                                          DisciplineService disciplineService,
                                          Runnable loadData) {

            this.table = table;
            this.tableViewColumnAction = tableViewColumnAction;
            this.dialogBalloon = dialogBalloon;
            this.dialogWindow = dialogWindow;
            this.disciplineService = disciplineService;
            this.loadData = loadData;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Discipline discipline = (Discipline) table.getItems().get(getIndex());
                setGraphic(tableViewColumnAction.getDefaultHideTableModel());

                tableViewColumnAction.getHideLink().setOnAction((ActionEvent event) -> table.getItems().remove(discipline));
            }
        }
    }


}
