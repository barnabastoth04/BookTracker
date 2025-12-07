package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.model.Book;
import com.booktracker.model.UserBook;
import com.booktracker.services.BookService;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SearchController {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Book> searchResultsListView;

    private final StageManager stageManager;
    private final UserBookService userBookService;
    private final BookService bookService;

    @Lazy
    public SearchController(StageManager stageManager, UserBookService userBookService, BookService bookService) {
        this.stageManager = stageManager;
        this.userBookService = userBookService;
        this.bookService = bookService;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void onSearchClicked() {
        searchResultsListView.getItems().clear();

        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            return;
        }
        List<Book> results = bookService.search(keyword);
        searchResultsListView.getItems().setAll(results);
    }

    @FXML
    private void onAddToWishlistClicked() {
        Book selected = searchResultsListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        UserBook ub = new UserBook();
        ub.setUser(SessionService.findCurrentUser());
        ub.setBook(selected);
        ub.setInWishlist(true);

        userBookService.save(ub);
    }

    @FXML
    private void onLibraryClicked(MouseEvent event) throws IOException {
        stageManager.switchToNextScene(FxmlView.LIBRARY);
    }

    @FXML
    private void onWishlistClicked(MouseEvent event) throws IOException {
        stageManager.switchToNextScene(FxmlView.WISHLIST);
    }

    @FXML
    private void onSearchMenuClicked() {
    }

    @FXML
    private void onSignOutClicked(ActionEvent event) throws IOException {
        stageManager.switchToNextScene(FxmlView.START);
    }
}
