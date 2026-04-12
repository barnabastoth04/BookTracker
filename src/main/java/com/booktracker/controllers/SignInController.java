package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.UserDto;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SignInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final StageManager stageManager;
    private final UserService userService;
    private final SessionService sessionService;

    @Lazy
    public SignInController(StageManager stageManager, UserService userService, SessionService sessionService) {
        this.stageManager = stageManager;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @FXML
    public void onSignInClicked() {
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
            dialog.display("Invalid Username/Password", "Sign In Failed: Invalid username or password!");
            return;
        }
        UserDto user = userService.signIn(username);
        sessionService.setCurrentUser(user);
        stageManager.switchToNextScene(FxmlView.USER);
    }

    @FXML
    private void onReturnClicked() {
        stageManager.switchToNextScene(FxmlView.START);
    }

    @FXML
    private void onSignUpClicked() {
        stageManager.switchToNextScene(FxmlView.SIGN_UP);
    }
}
