package com.booktracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBookDto {
    private Long id;
    private String bookAuthor;
    private String bookTitle;
    private int stars;
    private String reviewText;

    @Override
    public String toString() {
        return bookAuthor + ": " + bookTitle + " - " + stars + " ★| " + reviewText;
    }
}
