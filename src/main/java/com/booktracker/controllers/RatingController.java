package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.BookDto;
import com.booktracker.dtos.UserBookDto;
import com.booktracker.services.BookService;
import com.booktracker.services.SessionService;
import com.booktracker.services.UserBookService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingController {
    @FXML
    private ListView<UserBookDto> ratingsListView;

    @FXML
    private TextField addRating;

    @FXML
    private TextArea addReview;

    @FXML
    private ComboBox<Integer> starsField;

    private final StageManager stageManager;
    private final BookService bookService;
    private final UserBookService userBookService;
    private final SessionService sessionService;

    @Lazy
    public RatingController(StageManager stageManager, BookService bookService, UserBookService userBookService, SessionService sessionService) {
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
        List<UserBookDto> results = userBookService.getRatedBooks(sessionService.getCurrentUser());

        ratingsListView.getItems().setAll(results);
    }

    @FXML
    private void onAddRating() {
        String title = addRating.getText();
        String reviewText = addReview.getText();
        var stars = starsField.getValue();

        Dialog dialog = new Dialog();
        if (title.isEmpty() || stars == null) {
            dialog.display("Error", "Please fill all the required fields");
            return;
        }

        BookDto selected = bookService.getBookByTitle(title);

        if (selected == null) {
            dialog.display("Error", "This book does not exist");
            return;
        }

        boolean save = userBookService.addRating(selected, sessionService.getCurrentUser(), stars, reviewText);

        if (!save) {
            dialog.display("Error", "You haven't read this book!");
        } else {
            dialog.display("Add Book Success", "Book Added Successfully!");
        }
        stageManager.switchToNextScene(FxmlView.RATINGS);
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
