package com.booktracker.repositories;

import com.booktracker.model.User;
import com.booktracker.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findByUserAndInWishlistFalse(User user);

    List<UserBook> findByUserAndInWishlistTrue(User user);
}
