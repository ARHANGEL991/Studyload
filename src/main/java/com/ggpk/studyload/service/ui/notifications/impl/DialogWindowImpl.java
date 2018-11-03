package com.ggpk.studyload.service.ui.notifications.impl;

import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.notifications.DialogWindow;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;


@Component
@Slf4j
public class DialogWindowImpl implements DialogWindow {

    private final Integer SUCCESS_INDICATOR = 500;
    private final Integer LOADING_DATA = 10;

    private String title;
    private String header;
    private String message;

    private final MessageSource messageSource;

    @Autowired
    public DialogWindowImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void showError(Throwable ex) {

        ExceptionDialog dialog = new ExceptionDialog(ex);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.show();
    }

    public Optional<ButtonType> showConfirmation() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(getTitle());
        dialog.setHeaderText(getHeader());
        dialog.setContentText(getMessage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        return dialog.showAndWait();
    }

    public void showWarningDialog() {
        Alert dialog = new Alert(Alert.AlertType.WARNING);
        dialog.setTitle(getTitle());
        dialog.setHeaderText(getHeader());
        dialog.setContentText(getMessage());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.show();
    }

    /**
     * @param removableEntityName {scene.xxx.text} from /lang/language_ru_RU.properties
     * @param value               value selected
     * @param propertyColumnName  {scene.xxx.placeholder.xxx.id} from /lang/language_ru_RU.properties
     * @param id                  get value
     * @return
     */
    public Optional<ButtonType> confirmDelete(String removableEntityName, Object value, String propertyColumnName, Object id) {
        setTitle(messageSource.getMessage(LangProperties.REMOVE.getValue(), null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.QUESTION_REMOVE_WITH_PARAMS.getValue(), new Object[]{
                (messageSource.getMessage(removableEntityName, null, Locale.getDefault())), value,
                messageSource.getMessage(propertyColumnName, null, Locale.getDefault()), id
        }, Locale.getDefault()));
        setMessage(messageSource.getMessage(
                LangProperties.QUESTION_ARE_YOU_SURE.getValue()
                , null, Locale.getDefault()));
        return showConfirmation();
    }

    /**
     * @param emptyList      tableView, listView, comboBox
     * @param getItems       get items method
     * @param loadEntityName form {scene.xxx.text}  /lang/language_ru_RU.properties
     */
    public void loading(List emptyList, Supplier<List> getItems, String loadEntityName) {
        setTitle(messageSource.getMessage(LangProperties.LOADING.getValue(), null, Locale.getDefault()));
        setHeader(messageSource.getMessage(
                LangProperties.PROGRESS_LOADING_WITH_PARAM.getValue(),
                new Object[]{messageSource.getMessage(loadEntityName, null, Locale.getDefault())}, Locale.getDefault()));
        setMessage("");
        loading(emptyList, getItems);
    }

    /**
     * @param task
     * @param title
     */
    public void loading(Task task, String title) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(
                LangProperties.PROGRESS_LOADING_WITH_PARAM.getValue(),
                new Object[]{getTitle()}, Locale.getDefault()));
        setMessage("");
        loading(task);
    }

    /**
     * @param title {scene.xxx.text}  /lang/language_ru_RU.properties
     * @param ex    throwing  exception
     */
    public void errorLoading(String title, Throwable ex) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.ERROR_LOADING_WITH_PARAM.getValue(),
                new Object[]{getTitle()}, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    /**
     * @param title {scene.xxx.text}  /lang/language_ru_RU.properties
     * @param ex    throwing  exception
     */
    public void errorSave(String title, Throwable ex) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.ERROR_SAVE_WITH_PARAM.getValue(), new Object[]{
                getTitle()
        }, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    /**
     * @param title
     * @param value
     * @param ex
     */
    public void errorSave(String title, Object value, Throwable ex) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.ERROR_SAVE_WITH_PARAM.getValue()
                , new Object[]{value}, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    /**
     * @param title          {scene.xxx.text} i /lang/language_ru_RU.properties
     * @param columnProperty {scene.xxx.placeholder.xxx.id} i /lang/language_ru_RU.properties
     * @param key            get value
     * @param ex             throwing  exception
     */
    public void errorRemoved(String title, String columnProperty, Object key, Throwable ex) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.ERROR_REMOVE_WITH_PARAMS.getValue(),
                new Object[]{getTitle(),
                        messageSource.getMessage(columnProperty, null, Locale.getDefault()), key}, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    /**
     * @param title          {scene.xxx.text}  /lang/language_ru_RU.properties
     * @param columnProperty {scene.xxx.placeholder.xxx.id} i /lang/language_ru_RU.properties
     * @param key            get value
     * @param ex             throwing  exception
     */
    public void errorUpdate(String title, String columnProperty, Object key, Throwable ex) {
        setTitle(messageSource.getMessage(title, null, Locale.getDefault()));
        setHeader(messageSource.getMessage(LangProperties.ERROR_UPDATE_WITH_PARAMS.getValue(),
                new Object[]{getTitle(),
                        messageSource.getMessage(columnProperty, null, Locale.getDefault()), key}, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    public void errorReportCreate(String title, Throwable ex) {
        setTitle(messageSource.getMessage(LangProperties.ERROR_CREATE_REPORT_WITH_PARAM.getValue(), null, Locale.getDefault()));
        setHeader(messageSource.getMessage(title, null, Locale.getDefault()));
        setMessage(ex.getMessage());
        showError(ex);
    }

    //For users  only
    //Looks like pbar fine but it's really feeeeeeeee inside
    private Task<Object> getWorker(List emptyList, Supplier<List> getItems) {
        return new Task<Object>() {
            @Override
            protected void succeeded() {
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(2);
                        updateProgress(i, 99);
                        updateMessage(messageSource.getMessage(LangProperties.PROGRESS_FINISHED_WITH_PARAM.getValue(),
                                new Object[]{i}, Locale.getDefault()));
                    }
                    super.succeeded();
                } catch (InterruptedException e) {
                    log.error("Interrupt worker", e);
                    Thread.currentThread().interrupt();

                }
            }

            @Override
            protected Object call() throws Exception {
                List objects = getItems.get();
                Integer row = objects.size();
                for (int i = 0; i < row; i++) {
                    int jml = i + 1;
                    updateProgress(i, objects.size() - 1);
                    updateMessage(messageSource.getMessage(LangProperties.PROGRESS_GETTING_WITH_PARAMS.getValue(),
                            new Object[]{jml, row}, Locale.getDefault()));
                    emptyList.add(objects.get(i));
                    Thread.sleep(LOADING_DATA);
                }
                succeeded();
                return null;
            }
        };
    }

    public void loading(Task aTask) {
        Task<Object> worker = aTask;
        Thread th = new Thread(worker);
        th.setDaemon(true);
        th.start();
        ProgressDialog progressDialog = new ProgressDialog(worker);
        progressDialog.setTitle(title);
        progressDialog.setHeaderText(header);
        progressDialog.show();

    }

    public void loading(List emptyList, Supplier<List> items) {
        Task<Object> worker = getWorker(emptyList, items);
        Thread th = new Thread(worker);
        th.setDaemon(true);
        th.start();
        ProgressDialog progressDialog = new ProgressDialog(worker);
        progressDialog.setTitle(title);
        progressDialog.setHeaderText(header);
        progressDialog.show();

    }


}
