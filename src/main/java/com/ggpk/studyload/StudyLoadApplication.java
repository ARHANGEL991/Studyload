package com.ggpk.studyload;

import com.ggpk.studyload.ui.HomeView;
import com.ggpk.studyload.ui.LoadingView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
public class StudyLoadApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(StudyLoadApplication.class, HomeView.class, new LoadingView(), args);

    }


}
