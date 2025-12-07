package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.model.UserBook;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WishlistController {
    @FXML
    private ListView<UserBook> wishlistListView;

    private final StageManager stageManager;
    private final UserBookService userBookService;

    @Lazy
    public WishlistController(StageManager stageManager, UserBookService userBookService) {
        this.stageManager = stageManager;
        this.userBookService = userBookService;
    }

    @FXML
    private void initialize() {
        if (SessionService.findCurrentUser() == null) return;

        wishlistListView.getItems().addAll(
                userBookService.getWishlistBooks(SessionService.findCurrentUser())
        );
    }

    @FXML
    private void onLibraryClicked(MouseEvent event) throws IOException {
        stageManager.switchToNextScene(FxmlView.LIBRARY);
    }

    @FXML
    private void onWishlistClicked() {
    }

    @FXML
    private void onSearchClicked(MouseEvent event) throws IOException {
        stageManager.switchToNextScene(FxmlView.SEARCH);
    }

    @FXML
    private void onSignOutClicked(ActionEvent event) throws IOException {
        SessionService.clear();
        stageManager.switchToNextScene(FxmlView.START);
    }
}
