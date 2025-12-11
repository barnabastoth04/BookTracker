package com.booktracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean inWishlist;

    private LocalDate startedAt;
    private LocalDate finishedAt;

    private Integer rating;
    private String reviewText;
}
