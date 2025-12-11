package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.UserDto;
import com.booktracker.services.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label birthDateLabel;

    @FXML
    private DatePicker readingDatePicker;

    @FXML
    private CheckBox didReadCheckBox;

    private final StageManager stageManager;
    private final SessionService sessionService;

    @Lazy
    public UserController(StageManager stageManager, SessionService sessionService) {
        this.stageManager = stageManager;
        this.sessionService = sessionService;
    }

    @FXML
    public void initialize() {
        UserDto currentUser = sessionService.getCurrentUser();

        if (currentUser != null) {
            welcomeLabel.setText("Nice to see you " + currentUser.getUsername() + "!");
            nameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            emailLabel.setText(currentUser.getEmail());
            birthDateLabel.setText(String.valueOf(currentUser.getBirthDate()));
        }
        //TODO: mi van ha nincs felhasználó?!?
    }

    @FXML
    private void onHomeClicked() {
    }

    @FXML
    private void onLibraryClicked() {
        stageManager.switchToNextScene(FxmlView.LIBRARY);
    }

    @FXML
    private void onWishlistClicked() {
        stageManager.switchToNextScene(FxmlView.WISHLIST);
    }

    @FXML
    private void onSearchClicked() {
        stageManager.switchToNextScene(FxmlView.SEARCH);
    }

    @FXML
    private void onSignOutClicked() {
        SessionService.clear();
        stageManager.switchToNextScene(FxmlView.START);
    }

    @FXML
    private void onSaveReadingClicked() {
        LocalDate date = readingDatePicker.getValue();
        boolean read = didReadCheckBox.isSelected();

        if (date == null) {
            Dialog dialog = new Dialog();
            dialog.display("Warning", "Please select a date to read this!");
            return;
        }

        //TODO: jövőbeni dátum ellenőrzése
        //TODO: adatb
    }

    @FXML
    private void onReadingsClicked() {
        // readings oldal
    }

    @FXML
    private void onRatingsClicked() {
        // ratings oldal
    }

    @FXML
    private void onAchievementsClicked() {
        // achievements oldal
    }

    @FXML
    private void onStatisticsClicked() {
        // statistics oldal
    }
}
