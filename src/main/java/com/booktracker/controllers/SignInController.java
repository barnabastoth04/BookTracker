package com.booktracker.controllers;

import com.booktracker.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.io.IOException;

public class SignInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final SceneController sceneController = new SceneController();

    public SignInController() {
    }

    @Setter
    private UserService userService;

    @FXML
    public void onSignInClicked(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Dialog dialog = new Dialog();
            dialog.display("Missing Username/Password", "Missing Data: Please fill in both username and password!");
            return;
        }
        boolean valid = userService.validateUser(username, password);

        if (!valid) {
            Dialog dialog = new Dialog();
            dialog.display("Missing Username/Password", "Sign In Failed: Invalid username or password!");
            return;
        }
        try {
            sceneController.switchScene(event, "/com/booktracker/view/home.fxml");
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
}
