package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.BookDto;
import com.booktracker.services.BookService;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryController {
    @FXML
    private ListView<BookDto> libraryListView;

    @FXML
    private TextField addToLibrary;

    private final StageManager stageManager;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final SessionService sessionService;

    @Lazy
    public LibraryController(StageManager stageManager, BookService bookService, UserBookService userBookService, SessionService sessionService) {
        this.stageManager = stageManager;
        this.bookService = bookService;
        this.userBookService = userBookService;
        this.sessionService = sessionService;
    }

    @FXML
    private void initialize() {
        if (sessionService.getCurrentUser() == null) {
            return;
        }
        List<BookDto> results = userBookService.getLibraryBooks(sessionService.getCurrentUser());

        libraryListView.getItems().setAll(results);
    }

    @FXML
    private void onAddToLibraryClicked() {
        String isbnString = addToLibrary.getText();

        if (isbnString.isEmpty()) {
            return;
        }
        long isbn = Long.parseLong(isbnString);
        BookDto selected = bookService.getBookByIsbn(isbn);

        if (selected == null) {
            Dialog dialog = new Dialog();
            dialog.display("Error", "This book does not exist");
            return;
        }

        boolean save = userBookService.addToLibrary(selected, sessionService.getCurrentUser());

        Dialog dialog = new Dialog();
        if (!save) {
            dialog.display("Error", "This book is already in your wishlist!");
        }

        dialog.display("Add Book Success", "Book Added Successfully!");
        stageManager.switchToNextScene(FxmlView.LIBRARY);
    }

    @FXML
    private void onHomeClicked() {
        stageManager.switchToNextScene(FxmlView.USER);
    }

    @FXML
    private void onLibraryClicked() {
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
