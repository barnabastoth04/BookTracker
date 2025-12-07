package com.booktracker.services;

import com.booktracker.model.Book;
import com.booktracker.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();

        // keresés címre és szerzőre egyszerre
        List<Book> byTitle = bookRepository.findByTitleContainingIgnoreCase(keyword);
        List<Book> byAuthor = bookRepository.findByAuthorContainingIgnoreCase(keyword);

        // összefésülés duplikációk nélkül
        return List.copyOf(
                new java.util.LinkedHashSet<>( // Preserve order
                        java.util.stream.Stream.concat(byTitle.stream(), byAuthor.stream()).toList()
                )
        );
    }
}
