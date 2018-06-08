package com.ggpk.studyload.util;

import com.ggpk.studyload.model.Discipline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public final class TableCellInitializeUtil {


    public static void columnGroupNameInitialize(TableColumn<Discipline, String> columnGroupName) {
        columnGroupName.setCellValueFactory(param -> {
            ObservableValue<String> retVal;
            if (param != null && param.getValue().getGroup() != null) {

                retVal = new SimpleObjectProperty<>(param.getValue().getGroup().getName());
            } else {

                retVal = new SimpleObjectProperty<>();
            }


            return retVal;
        });
    }


    public static void columnSumHoursInitialize(TableColumn<Discipline, Double> columnSumHours) {
        columnSumHours.setCellValueFactory(param -> {
            if (param != null && param.getValue().getFullGroup() != null) {
                double totalSum = param.getValue().getFullGroup().getTotalSum();
                if (totalSum != 0) {
                    return new SimpleObjectProperty<>(totalSum);
                } else return new SimpleObjectProperty<>();
            } else return null;
        });
    }

    public static void columnAdditionalControlInitialize(TableColumn<Discipline, Integer> columnAdditionalControl) {
        columnAdditionalControl.setCellValueFactory(param -> {

            SimpleObjectProperty<Integer> retVal;
            if (param != null && param.getValue().getFullGroup() != null && param.getValue().getFullGroup().getAdditionalControl() != 0) {
                retVal = new SimpleObjectProperty<>(param.getValue().getFullGroup().getAdditionalControl());
            } else {
                retVal = new SimpleObjectProperty<>();
            }
            return retVal;
        });
    }

    public static void columnGroupStudentCountInitialize(TableColumn<Discipline, Integer> columnGroupStudentCount) {

        columnGroupStudentCount.setCellValueFactory(param ->

        {
            SimpleObjectProperty<Integer> retVal;
            if (param != null && param.getValue().getFullGroup() != null) {
                retVal = new SimpleObjectProperty<>(param.getValue().getFullGroup().getStudentCount());
            } else {
                retVal = new SimpleObjectProperty<>();
            }
            return retVal;
        });
    }

    public static void columnDisciplineTypeInitialize(TableColumn<Discipline, String> columnDisciplineType) {
        columnDisciplineType.setCellValueFactory(param ->

        {
            SimpleObjectProperty<String> retVal;
            if (param != null) {
                if (param.getValue().getDisciplineType() != null)
                    retVal = new SimpleObjectProperty<>(param.getValue().getDisciplineType().getValue());

                else {
                    retVal = new SimpleObjectProperty<>();
                }

            } else {
                retVal = new SimpleObjectProperty<>();
            }
            return retVal;
        });
    }

    public static void columnGroupTypeInitialize(TableColumn<Discipline, String> columnGroupType) {
        columnGroupType.setCellValueFactory(param ->

        {
            SimpleObjectProperty<String> retVal;
            if (param != null && param.getValue().getGroup().getGroupType() != null) {
                retVal = new SimpleObjectProperty<>(param.getValue().getGroup().getGroupType().getValue());
            } else {
                retVal = new SimpleObjectProperty<>();
            }

            return retVal;
        });
    }

    public static void columnTeacherNameInitialize(TableColumn<Discipline, String> columnTeacherName) {
        columnTeacherName.setCellValueFactory(param ->

        {
            SimpleObjectProperty<String> retVal;
            if (param != null && param.getValue().getFullGroup().getTeacher() != null) {
                retVal = new SimpleObjectProperty<>(param.getValue().getFullGroup().getTeacher().getName());
            } else {
                retVal = new SimpleObjectProperty<>();
            }

            return retVal;
        });
    }

}
