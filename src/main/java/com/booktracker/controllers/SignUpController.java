package com.booktracker.controllers;

import com.booktracker.model.User;
import com.booktracker.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

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

    private final SceneController sceneController = new SceneController();

    public SignUpController() {
    }

    @Setter
    private UserService userService;

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

        try {
            sceneController.switchScene(event, "/com/booktracker/view/home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onReturnClicked(ActionEvent event) {
        try {
            sceneController.switchScene(event, "/com/booktracker/view/booktracker-application.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignInLinkClicked(ActionEvent event) {
        try {
            sceneController.switchScene(event, "/com/booktracker/view/sign-in.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
