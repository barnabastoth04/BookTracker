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
public class WishlistController {
    @FXML
    private ListView<BookDto> wishlistListView;

    @FXML
    private TextField addToWishList;

    private final StageManager stageManager;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final SessionService sessionService;

    @Lazy
    public WishlistController(StageManager stageManager, BookService bookService, UserBookService userBookService, SessionService sessionService) {
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
        List<BookDto> results = userBookService.getWishlistBooks(sessionService.getCurrentUser());

        wishlistListView.getItems().setAll(results);
    }

    @FXML
    private void onAddToWishlistClicked() {
        String isbnString = addToWishList.getText();

        if (isbnString.isEmpty()) {
            return;
        }
        Long isbn = Long.parseLong(isbnString);
        BookDto selected = bookService.getBookByIsbn(isbn);

        if (selected == null) {
            Dialog dialog = new Dialog();
            dialog.display("Error", "This book does not exist");
            return;
        }
        //TODO: már rajta van, könyvtárban van

        userBookService.addToWishlist(selected, sessionService.getCurrentUser());

        Dialog dialog = new Dialog();
        dialog.display("Add Book Success", "Book Added Successfully!");

        stageManager.switchToNextScene(FxmlView.WISHLIST);
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
}
