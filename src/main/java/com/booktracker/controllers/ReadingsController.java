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
public class ReadingsController {
    @FXML
    private ListView<BookDto> currentlyReadingListView;

    @FXML
    private TextField addToRead;

    @FXML
    private TextField addNewToRead;

    private final StageManager stageManager;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final SessionService sessionService;

    @Lazy
    public ReadingsController(StageManager stageManager, BookService bookService, UserBookService userBookService, SessionService sessionService) {
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
        List<BookDto> results = userBookService.getCurrentlyReading(sessionService.getCurrentUser());

        currentlyReadingListView.getItems().setAll(results);
    }

    @FXML
    private void onAddNewToRead() {
        String isbnString = addNewToRead.getText();

        if (isbnString.isEmpty()) {
            return;
        }
        long isbn = Long.parseLong(isbnString);
        BookDto selected = bookService.getBookByIsbn(isbn);

        Dialog dialog = new Dialog();
        if (selected == null) {
            dialog.display("Error", "This book does not exist!");
            return;
        }

        boolean save = userBookService.addNewToRead(selected, sessionService.getCurrentUser());

        if (save) {
            dialog.display("Add Book Success", "Book Saved Successfully!");
        } else {
            dialog.display("Add Book Failed", "Book Save Failed!");
        }
        stageManager.switchToNextScene(FxmlView.READINGS);
    }

    @FXML
    private void onAddToRead() {
        String title = addToRead.getText();

        if (title.isEmpty()) {
            return;
        }
        BookDto selected = bookService.getBookByTitle(title);

        Dialog dialog = new Dialog();
        if (selected == null) {
            dialog.display("Error", "You're not reading this book!");
            return;
        }

        boolean save = userBookService.addToRead(selected, sessionService.getCurrentUser());

        if (save) {
            dialog.display("Add Book Success", "Book Saved Successfully!");
        } else {
            dialog.display("Add Book Failed", "Book Save Failed!");
        }
        stageManager.switchToNextScene(FxmlView.READINGS);
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
