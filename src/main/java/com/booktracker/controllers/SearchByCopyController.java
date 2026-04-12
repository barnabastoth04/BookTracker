package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.BookDto;
import com.booktracker.services.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SearchByCopyController {
    @FXML
    private TextField searchField;

    @FXML
    private Label authorLabel, titleLabel, publisherLabel, yearOfPublicationLabel, pageNumberLabel, coverLabel;

    @FXML
    private Label isbnLabel, genreLabel, firstPublishedLabel, originalTitleLabel, translatorLabel, illustratorLabel;

    @FXML
    private ImageView coverImageView;

    private final StageManager stageManager;
    private final BookService bookService;

    @Lazy
    public SearchByCopyController(StageManager stageManager, BookService bookService) {
        this.stageManager = stageManager;
        this.bookService = bookService;
    }

    @FXML
    private void onSearchClicked() {
        String keyword = searchField.getText();

        if (keyword.isEmpty()) {
            return;
        }
        long isbn = Long.parseLong(searchField.getText());

        BookDto book = bookService.getBookByIsbn(isbn);

        Dialog dialog = new Dialog();
        if (book == null) {
            dialog.display("Error!", "Book not found!");
            return;
        }

        authorLabel.setText(book.getAuthor());
        titleLabel.setText(book.getTitle());
        publisherLabel.setText(book.getPublisher());
        yearOfPublicationLabel.setText(String.valueOf(book.getYearOfPublication()));
        pageNumberLabel.setText(String.valueOf(book.getPageNumber()));
        coverLabel.setText(String.valueOf(book.getCover()).toLowerCase());
        isbnLabel.setText(String.valueOf(book.getIsbn()));
        genreLabel.setText(book.getGenre());
        firstPublishedLabel.setText(String.valueOf(book.getFirstPublished()));
        originalTitleLabel.setText(book.getOriginalTitle());
        translatorLabel.setText(book.getTranslator());
        illustratorLabel.setText(book.getIllustrator());

        Image image = new Image(book.getCoverImagePath());
        coverImageView.setImage(image);
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
