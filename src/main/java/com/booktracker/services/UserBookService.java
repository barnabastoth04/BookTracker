package com.booktracker.services;

import com.booktracker.dtos.BookDto;
import com.booktracker.dtos.UserBookDto;
import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.BookMapper;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.UserBook;
import com.booktracker.repositories.UserBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBookService {
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final UserBookRepository userBookRepository;
    private final AchievementService achievementService;

    public boolean addToWishlist(BookDto bookDto, UserDto userDto) {
        Optional<UserBook> optUserBook = userBookRepository.findUserBookByUserAndBook(userMapper.dtoToEntity(userDto), bookMapper.dtoToEntity(bookDto));

        UserBook userBook;
        if (optUserBook.isEmpty()) {
            userBook = new UserBook();
            userBook.setUser(userMapper.dtoToEntity(userDto));
            userBook.setBook(bookMapper.dtoToEntity(bookDto));
            userBook.setInWishlist(true);
            userBook.setRated(false);
        } else {
            userBook = optUserBook.get();

            if (userBook.getInWishlist()) {
                return false;
            } else {
                userBook.setInWishlist(true);
            }
        }

        userBookRepository.save(userBook);
        return true;
    }

    public boolean addToLibrary(BookDto bookDto, UserDto userDto) {
        Optional<UserBook> optUserBook = userBookRepository.findUserBookByUserAndBook(userMapper.dtoToEntity(userDto), bookMapper.dtoToEntity(bookDto));

        UserBook userBook;
        if (optUserBook.isEmpty()) {
            userBook = new UserBook();
            userBook.setUser(userMapper.dtoToEntity(userDto));
            userBook.setBook(bookMapper.dtoToEntity(bookDto));
            userBook.setInWishlist(false);
            userBook.setRated(false);
        } else {
            userBook = optUserBook.get();

            if (userBook.getInWishlist()) {
                userBook.setInWishlist(false);
            } else {
                return false;
            }
        }

        userBookRepository.save(userBook);
        return true;
    }

    public boolean addNewToRead(BookDto bookDto, UserDto userDto) {
        Optional<UserBook> optUserBook = userBookRepository.findUserBookByUserAndBook(userMapper.dtoToEntity(userDto), bookMapper.dtoToEntity(bookDto));

        UserBook userBook;
        if (optUserBook.isPresent()) {
            userBook = optUserBook.get();
        } else {
            userBook = new UserBook();
            userBook.setUser(userMapper.dtoToEntity(userDto));
            userBook.setBook(bookMapper.dtoToEntity(bookDto));
            userBook.setInWishlist(false);
            userBook.setRated(false);
        }

        if ((userBook.getStartedAt() == null && userBook.getFinishedAt() == null) || (userBook.getStartedAt() != null && userBook.getFinishedAt() != null)) {
            userBook.setStartedAt(LocalDate.now());
            userBook.setFinishedAt(null);

            userBookRepository.save(userBook);
            return true;
        }
        return false;
    }

    public boolean addToRead(BookDto bookDto, UserDto userDto) {
        Optional<UserBook> userBook = userBookRepository.findUserBookByUserAndBook(userMapper.dtoToEntity(userDto), bookMapper.dtoToEntity(bookDto));

        if (userBook.isPresent()) {
            if (userBook.get().getStartedAt() != null && userBook.get().getFinishedAt() == null) {
                userBook.get().setFinishedAt(LocalDate.now());
                userBookRepository.save(userBook.get());

                achievementService.checkAchievements(userMapper.dtoToEntity(userDto));
                return true;
            }
        }
        return false;
    }

    public boolean addRating(BookDto bookDto, UserDto userDto, int stars, String reviewText) {
        Optional<UserBook> userBook = userBookRepository.findUserBookByUserAndBook(userMapper.dtoToEntity(userDto), bookMapper.dtoToEntity(bookDto));

        if (userBook.isPresent()) {
            userBook.get().setRated(true);
            userBook.get().setStars(stars);
            userBook.get().setReviewText(reviewText);
            userBookRepository.save(userBook.get());
            return true;
        }
        return false;
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

    public List<BookDto> getCurrentlyReading(UserDto userDto) {
        List<UserBook> currentlyReading = userBookRepository.findByUserAndStartedAtIsNotNullAndFinishedAtNull(userMapper.dtoToEntity(userDto));

        List<BookDto> results = new ArrayList<>();

        for (UserBook userBook : currentlyReading) {
            BookDto bookDto = bookMapper.entityToDto(userBook.getBook());
            results.add(bookDto);
        }
        return results;
    }

    public List<UserBookDto> getRatedBooks(UserDto userDto) {
        List<UserBook> ratedBooks = userBookRepository.findByUserAndRatedTrue(userMapper.dtoToEntity(userDto));

        List<UserBookDto> results = new ArrayList<>();

        for (UserBook userBook : ratedBooks) {
            UserBookDto userBookDto = new UserBookDto();
            userBookDto.setBookAuthor(userBook.getBook().getAuthor());
            userBookDto.setBookTitle(userBook.getBook().getTitle());
            userBookDto.setStars(userBook.getStars());
            userBookDto.setReviewText(userBook.getReviewText());
            results.add(userBookDto);
        }
        return results;
    }

    public int getBooksPerYear(UserDto userDto) {
        int counter = 0;
        List<UserBook> userBooks = userBookRepository.findUserBooksByUser(userMapper.dtoToEntity(userDto));

        for (UserBook userBook : userBooks) {
            if (userBook.getFinishedAt() != null && userBook.getFinishedAt().getYear() == LocalDate.now().getYear()) {
                counter++;
            }
        }
        return counter;
    }

    public int getPagesPerYear(UserDto userDto) {
        int counter = 0;
        List<UserBook> userBooks = userBookRepository.findUserBooksByUser(userMapper.dtoToEntity(userDto));

        for (UserBook userBook : userBooks) {
            if (userBook.getFinishedAt() != null && userBook.getFinishedAt().getYear() == LocalDate.now().getYear()) {
                counter += userBook.getBook().getPageNumber();
            }
        }
        return counter;
    }
}
