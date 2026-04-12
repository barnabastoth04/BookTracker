package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.AchievementDto;
import com.booktracker.services.AchievementService;
import com.booktracker.services.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementController {
    @FXML
    private ListView<AchievementDto> achievementListView;

    private final StageManager stageManager;
    private final AchievementService achievementService;
    private final SessionService sessionService;

    @Lazy
    public AchievementController(StageManager stageManager, AchievementService achievementService, SessionService sessionService) {
        this.stageManager = stageManager;
        this.achievementService = achievementService;
        this.sessionService = sessionService;
    }

    @FXML
    private void initialize() {
        if (sessionService.getCurrentUser() == null) {
            return;
        }
        List<AchievementDto> results = achievementService.getAchievementsForUser(sessionService.getCurrentUser());

        achievementListView.getItems().setAll(results);
    }

    @FXML
    private void onHomeClicked() {
        stageManager.switchToNextScene(FxmlView.USER);
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
}
