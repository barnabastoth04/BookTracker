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
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @Column(name = "IN_WISHLIST")
    private Boolean inWishlist;

    @Column(name = "STARTED")
    private LocalDate startedAt;

    @Column(name = "FINISHED")
    private LocalDate finishedAt;

    @Column(name = "IS_RATED")
    private Boolean rated;

    @Column(name = "STARS")
    private Integer stars;

    @Column(name = "REVIEW")
    private String reviewText;
}
