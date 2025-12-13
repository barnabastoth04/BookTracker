package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.BookDto;
import com.booktracker.services.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchByNameController {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<BookDto> searchResultsListView;

    private final StageManager stageManager;
    private final BookService bookService;

    @Lazy
    public SearchByNameController(StageManager stageManager, BookService bookService) {
        this.stageManager = stageManager;
        this.bookService = bookService;
    }

    @FXML
    private void onSearchClicked() {
        searchResultsListView.getItems().clear();

        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            return;
        }

        List<BookDto> results = bookService.search(keyword);

        searchResultsListView.getItems().setAll(results);
    }

    @FXML
    private void onAddBookClicked() {
        stageManager.switchToNextScene(FxmlView.BOOK);
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
        stageManager.switchToNextScene(FxmlView.START);
    }
}
