package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.UserDto;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SignUpController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> genderField;

    @FXML
    private DatePicker birthDateField;

    private final StageManager stageManager;
    private final UserService userService;
    private final SessionService sessionService;

    @Lazy
    public SignUpController(StageManager stageManager, UserService userService, SessionService sessionService) {
        this.stageManager = stageManager;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @FXML
    private void onSignUpClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = genderField.getValue();
        LocalDate birthDate = birthDateField.getValue();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() ||
                firstName.isEmpty() || lastName.isEmpty() || birthDate == null) {
            Dialog dialog = new Dialog();
            dialog.display("Missing Sign Up Data", "Sign Up Failed: Please fill in all required fields.");
            return;
        }

        UserDto savedUser = userService.signUp(username, password, email, firstName, lastName, gender, birthDate);

        if (savedUser == null) {
            Dialog dialog = new Dialog();
            dialog.display("Error!", "Username already exists.");
            return;
        }
        sessionService.setCurrentUser(savedUser);
        stageManager.switchToNextScene(FxmlView.USER);
    }

    @FXML
    private void onReturnClicked() {
        stageManager.switchToNextScene(FxmlView.START);
    }

    @FXML
    private void onSignInLinkClicked() {
        stageManager.switchToNextScene(FxmlView.SIGN_IN);
    }
}
