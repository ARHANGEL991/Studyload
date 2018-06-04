package com.ggpk.studyload;

import com.ggpk.studyload.ui.HomeView;
import com.ggpk.studyload.ui.LoadingView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;
import java.util.ResourceBundle;


@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class StudyLoadApplication extends AbstractJavaFxApplicationSupport {

    public static final String LANG_LANGUAGE = "lang.language";

    public static void main(String[] args) {

        try {
            ResourceBundle.getBundle(LANG_LANGUAGE, Locale.getDefault());
        } catch (Exception e) {
            log.error("Error to find bundle for this locale, get default bundle", e.getMessage());
            Locale.setDefault(new Locale.Builder().setLanguage("ru").setRegion("RU").build());
        }
        launch(StudyLoadApplication.class, HomeView.class, new LoadingView(), args);

    }


}
