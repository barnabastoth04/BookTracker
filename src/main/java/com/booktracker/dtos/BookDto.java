package com.booktracker.dtos;

import com.booktracker.model.Cover;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private Year yearOfPublication;
    private int pageNumber;
    private Cover cover;
    private long isbn;
    private String genre;
    private Year firstPublished;
    private String originalTitle;
    private String translator;
    private String illustrator;
    private String coverImagePath;

    @Override
    public String toString() {
        return author + ": " + title + " (ISBN: " + isbn + ")";
    }
}
