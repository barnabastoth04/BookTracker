package com.booktracker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    private final SceneController sceneController = new SceneController();

    @FXML
    private void onSignInClicked(ActionEvent event) {
        try {
            sceneController.switchScene(event, "/com/booktracker/view/sign-in.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignUpClicked(ActionEvent event) {
        try {
            sceneController.switchScene(event, "/com/booktracker/view/sign-up.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFullscreen(Stage stage) {
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); // Ne jelenjen meg kilépési segítség
    }
}
