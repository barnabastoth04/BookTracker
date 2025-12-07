package com.booktracker.services;

import com.booktracker.model.User;
import com.booktracker.model.UserBook;
import com.booktracker.repositories.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {
    private final UserBookRepository userBookRepository;

    public List<UserBook> getLibraryBooks(User user) {
        return userBookRepository.findByUserAndInWishlistFalse(user);
    }

    public List<UserBook> getWishlistBooks(User user) {
        return userBookRepository.findByUserAndInWishlistTrue(user);
    }

    public void save(UserBook userBook) {
        userBookRepository.save(userBook);
    }
}
