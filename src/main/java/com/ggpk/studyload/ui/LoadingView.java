package com.ggpk.studyload.ui;

import de.felixroske.jfxsupport.SplashScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.SneakyThrows;


public class LoadingView extends SplashScreen {

    public LoadingView() {

    }

    @Override
    @SneakyThrows
    public Parent getParent() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loading.fxml"));


        return (Parent) loader.load();
    }


}
