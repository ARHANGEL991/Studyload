package com.ggpk.studyload.service.ui.notifications.impl;

import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.notifications.ValidatorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by dimmaryanto on 18/12/15.
 */
@Component
public class ValidatorMessagesImpl implements ValidatorMessages {

    private final MessageSource messageSource;

    @Autowired
    public ValidatorMessagesImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String validatorMinMax(String columnProperty, Integer min, Integer max) {
        return messageSource.getMessage(LangProperties.MIN_MAX_WITH_PARAMS.getValue(),
                new Object[]{
                        messageSource.getMessage(columnProperty, null, Locale.getDefault()),
                        min,
                        max
                }, Locale.getDefault());
    }

    /**
     * @param columnProperty model property {scene.xxx.placeholder.xxx}  file /lang/language_ru_RU.properties
     * @return
     */
    public String validatorNotNull(String columnProperty) {
        return messageSource.getMessage(LangProperties.NULL_WITH_PARAM.getValue(), new Object[]{
                messageSource.getMessage(columnProperty, null, Locale.getDefault())
        }, Locale.getDefault());
    }

    /**
     * @param columnProperty model property {scene.xxx.placeholder.xxx}  file /lang/language_ru_RU.properties
     * @return
     */
    public String validatorEmpty(String columnProperty) {
        return messageSource.getMessage(LangProperties.EMPTY_WITH_PARAM.getValue(), new Object[]{
                messageSource.getMessage(columnProperty, null, Locale.getDefault())
        }, Locale.getDefault());
    }

    /**
     * @param columnProperty model property {scene.xxx.placeholder.xxx}  file /lang/language_ru_RU.properties
     * @return
     */
    public String validatorNotSelected(String columnProperty) {
        return messageSource.getMessage(LangProperties.NOT_SELECTED_WITH_PARAM.getValue(), new Object[]{
                messageSource.getMessage(columnProperty, null, Locale.getDefault())
        }, Locale.getDefault());
    }

    /**
     * @param value number only
     * @return
     */
    public String validatorMin(Number value) {
        return messageSource.getMessage(LangProperties.MIN_WITH_PARAM.getValue(), new Object[]{
                value
        }, Locale.getDefault());
    }

    public String validatorDateNotEqualsNow() {
        return messageSource.getMessage(LangProperties.DATE_NOT_EQUAL_NOW.getValue(), null, Locale.getDefault());
    }
}
