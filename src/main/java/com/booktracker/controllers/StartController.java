package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class StartController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button signInButton;

    @FXML
    private Hyperlink signUpLink;

    private final StageManager stageManager;

    @Lazy
    public StartController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    private void onSignInClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.SIGN_IN);
    }

    @FXML
    private void onSignUpClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.SIGN_UP);
    }

    public void makeFullscreen(Stage stage) {
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); // Ne jelenjen meg kilépési segítség
    }
}
