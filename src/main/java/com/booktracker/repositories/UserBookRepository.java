package com.booktracker.repositories;

import com.booktracker.model.Book;
import com.booktracker.model.User;
import com.booktracker.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    Optional<UserBook> findUserBookByUserAndBook(User user, Book book);

    List<UserBook> findUserBooksByUser(User user);

    List<UserBook> findByUserAndInWishlistFalse(User user);

    List<UserBook> findByUserAndInWishlistTrue(User user);

    List<UserBook> findByUserAndStartedAtIsNotNullAndFinishedAtNull(User user);

    List<UserBook> findByUserAndRatedTrue(User user);

    long countByUserAndFinishedAtIsNotNull(User user);
}
