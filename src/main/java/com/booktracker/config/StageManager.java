package com.booktracker.config;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageManager {
    private final Stage primaryStage;
    private final FxmlLoader fxmlLoader;
    private final String applicationTitle;

    public StageManager(FxmlLoader fxmlLoader, Stage primaryStage, String applicationTitle) {
        this.primaryStage = primaryStage;
        this.fxmlLoader = fxmlLoader;
        this.applicationTitle = applicationTitle;
    }

    public void switchScene(final FxmlView view) {

        primaryStage.setTitle(applicationTitle);

        Parent root = loadRoot(view.getFxmlPath());

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void switchToNextScene(final FxmlView view) {

        Parent root = loadRoot(view.getFxmlPath());
        primaryStage.getScene().setRoot(root);

        primaryStage.show();
    }

    private Parent loadRoot(String fxmlPath) {
        Parent root;
        try {
            root = fxmlLoader.load(fxmlPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }
}
