package com.ggpk.studyload.config;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.ResourceBundle;

@Configuration
public class ApplicationConfiguration {


    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("lang.language", Locale.getDefault());
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("lang.language");
        source.setDefaultEncoding("UTF-8");
        return source;
    }


}
