package com.booktracker.controllers;

import com.booktracker.config.FxmlView;
import com.booktracker.config.StageManager;
import com.booktracker.dtos.BookDto;
import com.booktracker.model.Cover;
import com.booktracker.services.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Year;

@Component
public class BookController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField yearOfPublicationField;

    @FXML
    private TextField pageField;

    @FXML
    private ComboBox<String> coverField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField firstPublishedField;

    @FXML
    private TextField originalTitleField;

    @FXML
    private TextField translatorField;

    @FXML
    private TextField illustratorField;

    @FXML
    private ImageView coverImageView;

    private File selectedCoverFile;

    private final StageManager stageManager;
    private final BookService bookService;

    @Lazy
    public BookController(StageManager stageManager, BookService bookService) {
        this.stageManager = stageManager;
        this.bookService = bookService;
    }

    @FXML
    private void onAddBookClicked() {
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String yearOfPublication = yearOfPublicationField.getText();
        String pageString = pageField.getText();
        String coverString = coverField.getValue();
        String isbnString = isbnField.getText();
        String genre = genreField.getText();
        String firstPublished = firstPublishedField.getText();
        String originalTitle = originalTitleField.getText();
        String translator = translatorField.getText();
        String illustrator = illustratorField.getText();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() ||
                yearOfPublication.isEmpty() || pageString.isEmpty() || isbnString.isEmpty() || firstPublished.isEmpty()) {
            Dialog dialog = new Dialog();
            dialog.display("Missing Important Data", "Add Book Failed: Please fill in all required fields!");
            return;
        }

        Year yearOfPublicationYear = Year.parse(yearOfPublication);
        Year firstPublishedYear = Year.parse(firstPublished);
        Cover cover = Cover.valueOf(coverString.toUpperCase());
        int page = Integer.parseInt(pageString);
        long isbn = Long.parseLong(isbnString);

        if (yearOfPublicationYear.isAfter(Year.now()) || firstPublishedYear.isAfter(Year.now())) {
            Dialog dialog = new Dialog();
            dialog.display("Invalid Date", "Add Book Failed: Please add valid dates!");
            return;
        }

        BookDto savedBook = bookService.addBook(title, author, publisher, yearOfPublicationYear, page, cover, isbn, genre,
                firstPublishedYear, originalTitle, translator, illustrator);

        if (savedBook == null) {
            Dialog dialog = new Dialog();
            dialog.display("Add Book Failed", "Sorry, it seems like that there is already an existing Book.");
            return;
        }

        if (selectedCoverFile != null) {
            bookService.saveCoverImage(selectedCoverFile, isbn);
        }

        Dialog dialog = new Dialog();
        dialog.display("Add Book Success", "Book Added Successfully");
        stageManager.switchToNextScene(FxmlView.USER);
    }

    @FXML
    private void onSelectCoverImageClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book Cover Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));

        selectedCoverFile = fileChooser.showOpenDialog(null);

        if (selectedCoverFile != null) {
            Image image = new Image(selectedCoverFile.toURI().toString());
            coverImageView.setImage(image);
        }
    }

    @FXML
    private void onReturnClicked() {
        stageManager.switchToNextScene(FxmlView.SEARCH);
    }
}
