package com.ggpk.studyload.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.ggpk.studyload.StudyLoadApplication.LANG_LANGUAGE;

@Configuration
@Slf4j
public class ApplicationConfiguration {


    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle(LANG_LANGUAGE, Locale.getDefault());
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(LANG_LANGUAGE);
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
