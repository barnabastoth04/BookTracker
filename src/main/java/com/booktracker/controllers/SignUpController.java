package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.model.User;
import com.booktracker.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
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

    @FXML
    private Button signUpButton;

    @FXML
    private Button returnButton;

    @FXML
    private Hyperlink signInLink;

    private final StageManager stageManager;
    private final UserService userService;

    @Lazy
    public SignUpController(StageManager stageManager, UserService userService) {
        this.stageManager = stageManager;
        this.userService = userService;
    }

    @FXML
    private void onSignUpClicked(ActionEvent event) {
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

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setGender(gender);
        newUser.setBirthDate(birthDate);

        User savedUser = userService.signUp(username, password);
        if (savedUser == null) {
            Dialog dialog = new Dialog();
            dialog.display("Error!", "Username already exists.");
            return;
        }
        stageManager.switchToNextScene(FxmlView.USER);
    }

    @FXML
    private void onReturnClicked() {
        stageManager.switchToNextScene(FxmlView.START);
    }

    @FXML
    private void onSignInLinkClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.SIGN_IN);
    }
}
