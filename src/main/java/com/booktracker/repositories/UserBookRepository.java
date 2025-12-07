package com.booktracker.repositories;

import com.booktracker.model.Book;
import com.booktracker.model.User;
import com.booktracker.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook,Long> {
    Optional<UserBook> findByUserAndBook(User user, Book book);

    List<UserBook> findByUser(User user);

    List<UserBook> findByBook(Book book);

    List<UserBook> findByUserAndInWishlistFalse(User user);

    List<UserBook> findByUserAndInWishlistTrue(User user);

}
