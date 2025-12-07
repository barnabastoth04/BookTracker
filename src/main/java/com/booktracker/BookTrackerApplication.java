package com.booktracker;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class BookTrackerApplication extends Application {
    private static Stage stage;
    private ConfigurableApplicationContext applicationContext;
    private StageManager stageManager;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(Launcher.class).run();
    }

    @Override
    public void stop() {
        applicationContext.close();
        stage.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stageManager = applicationContext.getBean(StageManager.class, stage);
        showStartScene();
    }

    private void showStartScene() {
        stageManager.switchScene(FxmlView.START);
    }
}
