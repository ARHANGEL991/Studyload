package com.ggpk.studyload.service.ui.notifications;

public interface DialogBalloon {

    void succeedRemoved(String title);

    void succeedRemoved(String title, Object value);

    void succeedUpdated(String title, String columnProperty, Object value);

    void succeedUpdated(String title, String columnProperty, Object value, Number from, Number to);

    void succeedSave(String title);

    void succeedSave(String title, String value);

    void succeedSave(String title, String columnProperty, Object value);

    void warningMessage(String title, String message, Object[] params);

    void warningPaidMessage(String title, Object value);

    void warningSendingMessage(String title, String columnProperty, Object value);

    void warningExpiredMessage(String title, Number value);

    void warningCantRemovedSendMessage(String title, Object value);

    void warningNotNullMessage(String title, Object value);


}
