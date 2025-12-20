package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import com.booktracker.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class StatisticsController {
    @FXML
    private Label bookPerYear;

    @FXML
    private Label pagePerYear;

    @FXML
    private Label daysPerYear;

    @FXML
    private Label percentageOfYear;

    private final StageManager stageManager;
    private final UserService userService;
    private final UserBookService userBookService;
    private final SessionService sessionService;

    @Lazy
    public StatisticsController(StageManager stageManager, UserService userService, UserBookService userBookService, SessionService sessionService) {
        this.stageManager = stageManager;
        this.userService = userService;
        this.userBookService = userBookService;
        this.sessionService = sessionService;
    }

    @FXML
    private void initialize() {
        int days = userService.getDaysPerYear(sessionService.getCurrentUser());
        daysPerYear.setText(String.valueOf(days));
        double percentage = (double) days / 365 * 100;
        percentageOfYear.setText(String.format("(%.2f %%)", percentage));

        int books = userBookService.getBooksPerYear(sessionService.getCurrentUser());
        bookPerYear.setText("You've read " + books + " books this year so far");

        int pages = userBookService.getPagesPerYear(sessionService.getCurrentUser());
        pagePerYear.setText("You've read " + pages + " pages this year so far");
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
