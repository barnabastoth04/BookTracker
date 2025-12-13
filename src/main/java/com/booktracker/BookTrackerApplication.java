package com.booktracker;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class BookTrackerApplication extends Application {
    private static Stage stage;
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(Launcher.class).run();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        StageManager stageManager = applicationContext.getBean(StageManager.class, primaryStage);
        stageManager.switchScene(FxmlView.START);
    }

    @Override
    public void stop() {
        applicationContext.close();
        stage.close();
    }
}
