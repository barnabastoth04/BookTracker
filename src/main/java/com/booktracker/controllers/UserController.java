package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.model.User;
import com.booktracker.services.SessionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;

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

    @Lazy
    public UserController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    public void initialize() {
        User currentUser = SessionService.findCurrentUser();

        if (currentUser != null) {
            welcomeLabel.setText("Hi " + currentUser.getUsername() + "!");
            nameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            emailLabel.setText(currentUser.getEmail());
            birthDateLabel.setText(String.valueOf(currentUser.getBirthDate()));
        }
        //TODO: mi van ha nincs felhasználó?!?
    }

    @FXML
    private void onLibraryClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.LIBRARY);
    }

    @FXML
    private void onWishlistClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.WISHLIST);
    }

    @FXML
    private void onSearchClicked(ActionEvent event) {
        stageManager.switchToNextScene(FxmlView.SEARCH);
    }

    @FXML
    private void onSignOutClicked(ActionEvent event) {
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

        // Itt fogod elmenteni az adatot DB-be a későbbiekben

    }

    @FXML
    private void onReadingsClicked(ActionEvent event) {
        // readings oldal
    }

    @FXML
    private void onAchievementsClicked(ActionEvent event) {
        // achievements oldal
    }

    @FXML
    private void onStatisticsClicked(ActionEvent event) {
        // statistics oldal
    }
}
