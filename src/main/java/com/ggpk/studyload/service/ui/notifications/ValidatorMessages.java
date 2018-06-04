package com.ggpk.studyload.service.ui.notifications;

public interface ValidatorMessages {
    String validatorMinMax(String columnProperty, Integer min, Integer max);

    String validatorNotNull(String columnProperty);

    String validatorEmpty(String columnProperty);

    String validatorNotSelected(String columnProperty);

    String validatorMin(Number value);

    String validatorDateNotEqualsNow();

}
