package com.booktracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long bookId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "PUBLISHER", nullable = false)
    private String publisher;

    @Column(name = "YEAR_OF_PUBLICATION", nullable = false)
    private LocalDate yearOfPublication;

    @Column(name = "PAGE_NUMBER", nullable = false)
    private int pageNumber;

    @Column(name = "COVER", nullable = false)
    @Enumerated(EnumType.STRING)
    private Cover cover;

    @Column(name = "ISBN", nullable = false)
    private long isbn;

    @Column(name="GENRE")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "FIRST_PUBLISHED")
    private LocalDate firstPublished;

    @Column(name = "ORIGINAL_TITLE")
    private String originalTitle;

    @Column(name = "TRANSLATOR")
    private String translator;

    @Column(name = "ILLUSTRATOR")
    private String illustrator;
}
