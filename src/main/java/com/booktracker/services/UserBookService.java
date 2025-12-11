package com.booktracker.services;

import com.booktracker.dtos.BookDto;
import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.BookMapper;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.UserBook;
import com.booktracker.repositories.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {
    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserMapper userMapper;

    private final UserBookRepository userBookRepository;

    public void addToWishlist(BookDto bookDto, UserDto userDto) {
        UserBook userBook = new UserBook();
        userBook.setUser(userMapper.dtoToEntity(userDto));
        userBook.setBook(bookMapper.dtoToEntity(bookDto));
        userBook.setInWishlist(true);
        userBookRepository.save(userBook);
    }

    public void addToLibrary(BookDto bookDto, UserDto userDto) {
        UserBook userBook = new UserBook();
        userBook.setUser(userMapper.dtoToEntity(userDto));
        userBook.setBook(bookMapper.dtoToEntity(bookDto));
        userBook.setInWishlist(false);
        userBookRepository.save(userBook);
    }

    public List<BookDto> getLibraryBooks(UserDto userDto) {
        List<UserBook> userBooksLibrary = userBookRepository.findByUserAndInWishlistFalse(userMapper.dtoToEntity(userDto));

        List<BookDto> results = new ArrayList<>();

        for (UserBook userBook : userBooksLibrary) {
            BookDto bookDto = bookMapper.entityToDto(userBook.getBook());
            results.add(bookDto);
        }
        return results;
    }

    public List<BookDto> getWishlistBooks(UserDto userDto) {
        List<UserBook> userBooksLibrary = userBookRepository.findByUserAndInWishlistTrue(userMapper.dtoToEntity(userDto));

        List<BookDto> results = new ArrayList<>();

        for (UserBook userBook : userBooksLibrary) {
            BookDto bookDto = bookMapper.entityToDto(userBook.getBook());
            results.add(bookDto);
        }
        return results;
    }
}
