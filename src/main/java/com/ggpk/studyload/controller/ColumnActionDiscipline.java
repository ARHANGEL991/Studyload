package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.TableViewColumnAction;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
import com.ggpk.studyload.service.ui.notifications.DialogWindow;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ColumnActionDiscipline<T> extends TableCell<T, String> {

    private TableView table;
    private TableViewColumnAction tableViewColumnAction;
    private DialogBalloon dialogBalloon;
    private DialogWindow dialogWindow;
    private DisciplineService disciplineService;
    private Runnable loadData;

    public ColumnActionDiscipline(TableView table,
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
                        LangProperties.DATA_DISCIPLINE_DELETE.getValue(),
                        discipline.getName(), LangProperties.ID.getValue(), discipline.getId()
                ).get() == ButtonType.OK) {
                    try {
                        disciplineService.delete(discipline);
                        loadData.run();
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

