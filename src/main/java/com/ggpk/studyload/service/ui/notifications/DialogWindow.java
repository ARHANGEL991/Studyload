package com.ggpk.studyload.service.ui.notifications;

import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface DialogWindow {
    Optional<ButtonType> showConfirmation();

    void showWarningDialog();

    Optional<ButtonType> confirmDelete(String title, Object value, String propertyColumnName, Object id);

    void loading(List emptyList, Supplier<List> getItems, String title);

    void loading(Task task, String title);

    void errorLoading(String title, Throwable ex);

    void errorSave(String title, Throwable ex);

    void errorSave(String title, Object value, Throwable ex);

    void errorRemoved(String title, String columnProperty, Object key, Throwable ex);

    void errorUpdate(String title, String columnProperty, Object key, Throwable ex);

    void errorPrint(String title, Throwable ex);

    void loading(Task aTask);

    void loading(List emptyList, Supplier<List> getItems);

}
