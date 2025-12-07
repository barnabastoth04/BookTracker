package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.model.UserBook;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LibraryController implements Initializable {
    @FXML
    private ListView<UserBook> libraryListView;

    private final StageManager stageManager;
    private final UserBookService userBookService;

    @Lazy
    public LibraryController(StageManager stageManager, UserBookService userBookService) {
        this.stageManager = stageManager;
        this.userBookService = userBookService;
    }

    @FXML
    private void initialize() {
        if (SessionService.findCurrentUser() == null) return;

        libraryListView.getItems().addAll(
                userBookService.getLibraryBooks(SessionService.findCurrentUser())
        );
    }

    @FXML
    private void onLibraryClicked() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
