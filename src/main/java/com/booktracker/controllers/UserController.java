package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.UserDto;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserService;
import javafx.fxml.FXML;
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

    private final StageManager stageManager;
    private final SessionService sessionService;
    private final UserService userService;

    @Lazy
    public UserController(StageManager stageManager, SessionService sessionService, UserService userService) {
        this.stageManager = stageManager;
        this.sessionService = sessionService;
        this.userService = userService;
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
    private void onSearchByAuthorTitle() {
        stageManager.switchToNextScene(FxmlView.SEARCH_BY_NAME);
    }

    @FXML
    private void onSearchByCopy() {
        stageManager.switchToNextScene(FxmlView.SEARCH_BY_COPY);
    }

    @FXML
    private void onSignOutClicked() {
        SessionService.clear();
        stageManager.switchToNextScene(FxmlView.START);
    }

    @FXML
    private void onSaveReadingClicked() {
        LocalDate date = readingDatePicker.getValue();
        Dialog dialog = new Dialog();

        if (date == null || date.isAfter(LocalDate.now())) {
            dialog.display("Error", "Please select a valid date to read!");
            return;
        }

        boolean save = userService.saveReading(date, sessionService.getCurrentUser().getUsername());

        if (!save) {
            dialog.display("Error", "Save failed!");
        } else {
            dialog.display("Success!", "Saved!");
        }
    }

    @FXML
    private void onReadingsClicked() {
        stageManager.switchToNextScene(FxmlView.READINGS);
    }

    @FXML
    private void onRatingsClicked() {
        stageManager.switchToNextScene(FxmlView.RATINGS);
    }

    @FXML
    private void onAchievementsClicked() {
        stageManager.switchToNextScene(FxmlView.ACHIEVEMENTS);
    }

    @FXML
    private void onStatisticsClicked() {
        stageManager.switchToNextScene(FxmlView.STATISTICS);
    }
}
