package com.booktracker.services;

import com.booktracker.dtos.BookDto;
import com.booktracker.mappers.BookMapper;
import com.booktracker.model.Book;
import com.booktracker.model.Cover;
import com.booktracker.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    @Autowired
    BookMapper bookMapper;

    private final BookRepository bookRepository;

    public BookDto addBook(String title, String author, String publisher, Year yearOfPublication, int page,
                           Cover cover, long isbn, String genre, Year firstPublished, String originalTitle,
                           String translator, String illustrator) {
        if (bookRepository.existsByIsbn(isbn)) {
            return null;
        }
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setYearOfPublication(yearOfPublication);
        book.setPageNumber(page);
        book.setCover(cover);
        book.setIsbn(isbn);
        book.setGenre(genre);
        book.setFirstPublished(firstPublished);
        book.setOriginalTitle(originalTitle);
        book.setTranslator(translator);
        book.setIllustrator(illustrator);

        bookRepository.save(book);

        return bookMapper.entityToDto(book);
    }

    public void saveCoverImage(File image, Long isbn) {
        String path;

        Optional<Book> optBook = bookRepository.findByIsbn(isbn);
        if (optBook.isEmpty()) {
            return;
        }
        Book book = optBook.get();
        long bookId = book.getBookId();

        try {
            Path dir = Paths.get("src/main/resources/com/booktracker/covers");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            String extension = image.getName().substring(image.getName().lastIndexOf("."));
            Path target = dir.resolve("book_" + bookId + extension);
            Files.copy(image.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            path = "/com/booktracker/covers/book_" + bookId + extension;
        } catch (IOException e) {
            throw new RuntimeException("Could not save cover image!", e);
        }
        book.setCoverImagePath(path);
        bookRepository.save(book);
    }

    public BookDto getBookByTitle(String title) {
        Optional<Book> optBook = bookRepository.findByTitle(title);
        if (optBook.isPresent()) {
            return bookMapper.entityToDto(optBook.get());
        }
        return null;
    }

    public BookDto getBookByIsbn(Long isbn) {
        Optional<Book> optBook = bookRepository.findByIsbn(isbn);
        if (optBook.isPresent()) {
            return bookMapper.entityToDto(optBook.get());
        }
        return null;
    }

    public List<BookDto> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>();
        }

        List<Book> byTitle = bookRepository.findByTitleContainingIgnoreCase(keyword);
        List<Book> byAuthor = bookRepository.findByAuthorContainingIgnoreCase(keyword);

        List<BookDto> results = new ArrayList<>();
        for (Book book : byAuthor) {
            BookDto bookDto = bookMapper.entityToDto(book);
            results.add(bookDto);
        }

        for (Book book : byTitle) {
            BookDto bookDto = bookMapper.entityToDto(book);
            results.add(bookDto);
        }

        return results;
    }
}
