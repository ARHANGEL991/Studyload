package com.ggpk.studyload.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ComboBoxAutoComplete<T> {

    public interface AutoCompleteComparator<T> {
        boolean matches(String typedText, T objectToCompare);
    }

    private ComboBox<T> cmb;
    private ObservableList<T> originalItems;
    private StringProperty filter = new SimpleStringProperty("");
    private AutoCompleteComparator comparator = (typedText, objectToCompare) -> objectToCompare.toString().contains(typedText);

    public ComboBoxAutoComplete(ComboBox<T> cmb, AutoCompleteComparator comparator) {
        this.cmb = cmb;
        this.comparator = comparator;
        this.originalItems = FXCollections.observableArrayList(cmb.getItems());

        cmb.setTooltip(new Tooltip());
        cmb.getTooltip().textProperty().bind(filter);

        filter.addListener((observable, oldValue, newValue) -> handleFilterChanged(newValue));

        cmb.setOnKeyPressed(this::handleOnKeyPressed);
        cmb.setOnHidden(this::handleOnHiding);
    }


//    public void handleOnKeyPressed(KeyEvent e) {
//        ObservableList<T> filteredList = FXCollections.observableArrayList();
//        KeyCode code = e.getCode();
//
//        if (code.isLetterKey()) {
//            filter += e.getText();
//        }
//        if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
//            filter = filter.substring(0, filter.length() - 1);
//            cmb.getItems().setAll(originalItems);
//        }
//        if (code == KeyCode.ESCAPE) {
//            filter = "";
//        }
//        if (filter.length() == 0) {
//            filteredList = originalItems;
//            cmb.getTooltip().hide();
//        } else {
//            Stream<T> itens = cmb.getItems().stream();
//            String txtUsr = filter.toString().toLowerCase();
//            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
//            cmb.getTooltip().setText(txtUsr);
//            Window stage = cmb.getScene().getWindow();
//            double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
//            double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
//            cmb.getTooltip().show(stage, posX, posY);
//            cmb.show();
//        }
//        cmb.getItems().setAll(filteredList);
//    }

//    public void handleOnHiding(KeyEvent e) {
//        filter = "";
//        cmb.getTooltip().hide();
//        T s = cmb.getSelectionModel().getSelectedItem();
//        cmb.getItems().setAll(originalItems);
//        cmb.getSelectionModel().select(s);
//    }

    private void handleOnKeyPressed(KeyEvent keyEvent) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9А-Яа-я]");
        KeyCode code = keyEvent.getCode();
        String filterValue = filter.get();
        if (pattern.matcher(keyEvent.getText()).matches()) {
            filterValue += keyEvent.getText();
        } else if (code == KeyCode.BACK_SPACE && filterValue.length() > 0) {
            filterValue = filterValue.substring(0, filterValue.length() - 1);
        } else if (code == KeyCode.ESCAPE) {
            filterValue = "";
        } else if (code == KeyCode.DOWN || code == KeyCode.UP) {
            cmb.show();
        }
        filter.setValue(filterValue);
    }


    private void handleFilterChanged(String newValue) {
        if (!newValue.equals("")) {
            cmb.show();
            if (filter.get().equals("")) {
                restoreOriginalItems();
            } else {
                showTooltip();
                cmb.getItems().setAll(filterItems());
            }
        } else {
            cmb.getTooltip().hide();
        }
    }

    private void showTooltip() {
        if (!cmb.getTooltip().isShowing()) {
            Window stage = cmb.getScene().getWindow();
            double posX = stage.getX() + cmb.localToScene(cmb.getBoundsInLocal()).getMinX() + 4;
            double posY = stage.getY() + cmb.localToScene(cmb.getBoundsInLocal()).getMinY() - 29;
            cmb.getTooltip().show(stage, posX, posY);
        }
    }

    private ObservableList filterItems() {
        List filteredList = originalItems.stream().filter(el -> comparator.matches(filter.get().toLowerCase(), el)).collect(Collectors.toList());
        return FXCollections.observableArrayList(filteredList);
    }

    private void handleOnHiding(Event e) {
        filter.setValue("");
        cmb.getTooltip().hide();
        restoreOriginalItems();
    }

    private void restoreOriginalItems() {
        T s = cmb.getSelectionModel().getSelectedItem();
        cmb.getItems().setAll(originalItems);
        cmb.getSelectionModel().select(s);
    }
}
