package com.booktracker.services;

import com.booktracker.dtos.BookDto;
import com.booktracker.dtos.UserBookDto;
import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.BookMapper;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.Book;
import com.booktracker.model.User;
import com.booktracker.model.UserBook;
import com.booktracker.repositories.UserBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserBookServiceTest {
    @Mock
    private BookMapper bookMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserBookRepository userBookRepository;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private UserBookService userBookService;

    private User testUser;
    private UserDto testUserDto;
    private Book testBook;
    private BookDto testBookDto;
    private UserBook testUserBook;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(100001L);
        testUser.setUsername("test.user");

        testUserDto = new UserDto();
        testUserDto.setId(100001L);
        testUserDto.setUsername("test.user");

        testBook = new Book();
        testBook.setBookId(100001L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPageNumber(300);

        testBookDto = new BookDto();
        testBookDto.setId(100001L);
        testBookDto.setTitle("Test Book");
        testBookDto.setAuthor("Test Author");
        testBookDto.setPageNumber(300);

        testUserBook = new UserBook();
        testUserBook.setId(100001L);
        testUserBook.setUser(testUser);
        testUserBook.setBook(testBook);
        testUserBook.setInWishlist(false);
        testUserBook.setRated(false);
        testUserBook.setStars(0);
        testUserBook.setReviewText(null);
        testUserBook.setStartedAt(null);
        testUserBook.setFinishedAt(null);
    }

    @Test
    public void addToWishlistWhenUserBookDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addToWishlist(testBookDto, testUserDto);

        assertTrue(result);
        verify(userBookRepository).findUserBookByUserAndBook(testUser, testBook);
        verify(userBookRepository).save(any(UserBook.class));
    }

    @Test
    public void addToWishlistWhenUserBookExistsAndNotInWishlist() {
        testUserBook.setInWishlist(false);
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addToWishlist(testBookDto, testUserDto);

        assertTrue(result);
        assertTrue(testUserBook.getInWishlist());
        verify(userBookRepository).save(testUserBook);
    }

    @Test
    public void addToWishlistWhenAlreadyInWishlist() {
        testUserBook.setInWishlist(true);
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));

        boolean result = userBookService.addToWishlist(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
    }

    @Test
    public void addToLibraryWhenUserBookDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addToLibrary(testBookDto, testUserDto);

        assertTrue(result);
        assertFalse(testUserBook.getInWishlist());
        verify(userBookRepository).save(any(UserBook.class));
    }

    @Test
    public void addToLibraryWhenInWishlist() {
        testUserBook.setInWishlist(true);
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addToLibrary(testBookDto, testUserDto);

        assertTrue(result);
        assertFalse(testUserBook.getInWishlist());
        verify(userBookRepository).save(testUserBook);
    }

    @Test
    public void addToLibraryWhenAlreadyInLibraryAndNotInWishlist() {
        testUserBook.setInWishlist(false);
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));

        boolean result = userBookService.addToLibrary(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
    }

    @Test
    public void addNewToReadWhenUserBookDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addNewToRead(testBookDto, testUserDto);

        assertTrue(result);
        verify(userBookRepository).save(any(UserBook.class));
    }

    @Test
    public void addNewToReadWhenNotStartedOrAlreadyFinished() {
        testUserBook.setStartedAt(null);
        testUserBook.setFinishedAt(null);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addNewToRead(testBookDto, testUserDto);

        assertTrue(result);
        assertNotNull(testUserBook.getStartedAt());
        assertNull(testUserBook.getFinishedAt());
        verify(userBookRepository).save(testUserBook);
    }

    @Test
    public void addNewToReadWhenAlreadyStartedAndNotFinished() {
        testUserBook.setStartedAt(LocalDate.of(2020, 1, 1));
        testUserBook.setFinishedAt(null);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));

        boolean result = userBookService.addNewToRead(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
    }

    @Test
    public void addNewToReadWhenAlreadyFinished() {
        testUserBook.setStartedAt(LocalDate.of(2020, 1, 1));
        testUserBook.setFinishedAt(LocalDate.of(2020, 2, 1));

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addNewToRead(testBookDto, testUserDto);

        assertTrue(result);
        assertNotNull(testUserBook.getStartedAt());
        assertNull(testUserBook.getFinishedAt());
        verify(userBookRepository).save(testUserBook);
    }

    @Test
    public void addToReadWhenStartedAndNotFinished() {
        testUserBook.setStartedAt(LocalDate.of(2020, 1, 1));
        testUserBook.setFinishedAt(null);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addToRead(testBookDto, testUserDto);

        assertTrue(result);
        assertNotNull(testUserBook.getFinishedAt());
        verify(userBookRepository).save(testUserBook);
        verify(achievementService).checkAchievements(testUser);
    }

    @Test
    public void addToReadWhenNotStarted() {
        testUserBook.setStartedAt(null);
        testUserBook.setFinishedAt(null);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));

        boolean result = userBookService.addToRead(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
        verify(achievementService, never()).checkAchievements(any(User.class));
    }

    @Test
    public void addToReadWhenAlreadyFinished() {
        testUserBook.setStartedAt(LocalDate.of(2020, 1, 1));
        testUserBook.setFinishedAt(LocalDate.of(2020, 2, 1));

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));

        boolean result = userBookService.addToRead(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
        verify(achievementService, never()).checkAchievements(any(User.class));
    }

    @Test
    public void addToReadWhenUserBookDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());

        boolean result = userBookService.addToRead(testBookDto, testUserDto);

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
        verify(achievementService, never()).checkAchievements(any(User.class));
    }

    @Test
    public void addRatingWhenUserBookExists() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.of(testUserBook));
        when(userBookRepository.save(any(UserBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = userBookService.addRating(testBookDto, testUserDto, 5, "Good book!");

        assertTrue(result);
        assertTrue(testUserBook.getRated());
        assertEquals(5, testUserBook.getStars());
        assertEquals("Good book!", testUserBook.getReviewText());
        verify(userBookRepository).save(testUserBook);
    }

    @Test
    public void addRatingWhenUserBookDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(bookMapper.dtoToEntity(testBookDto)).thenReturn(testBook);
        when(userBookRepository.findUserBookByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());

        boolean result = userBookService.addRating(testBookDto, testUserDto, 5, "Good book!");

        assertFalse(result);
        verify(userBookRepository, never()).save(any(UserBook.class));
    }

    @Test
    public void getLibraryBooks() {
        List<UserBook> userBooks = new ArrayList<>();
        testUserBook.setInWishlist(false);
        userBooks.add(testUserBook);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findByUserAndInWishlistFalse(testUser)).thenReturn(userBooks);
        when(bookMapper.entityToDto(testBook)).thenReturn(testBookDto);

        List<BookDto> result = userBookService.getLibraryBooks(testUserDto);

        assertEquals(1, result.size());
        assertEquals(testBookDto, result.getFirst());
        verify(userBookRepository).findByUserAndInWishlistFalse(testUser);
        verify(bookMapper).entityToDto(testBook);
    }

    @Test
    public void getWishlistBooks() {
        List<UserBook> userBooks = new ArrayList<>();
        testUserBook.setInWishlist(true);
        userBooks.add(testUserBook);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findByUserAndInWishlistTrue(testUser)).thenReturn(userBooks);
        when(bookMapper.entityToDto(testBook)).thenReturn(testBookDto);

        List<BookDto> result = userBookService.getWishlistBooks(testUserDto);

        assertEquals(1, result.size());
        assertEquals(testBookDto, result.getFirst());
        verify(userBookRepository).findByUserAndInWishlistTrue(testUser);
        verify(bookMapper).entityToDto(testBook);
    }

    @Test
    public void getCurrentlyReading() {
        List<UserBook> userBooks = new ArrayList<>();
        testUserBook.setStartedAt(LocalDate.now().minusDays(3));
        testUserBook.setFinishedAt(null);
        userBooks.add(testUserBook);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findByUserAndStartedAtIsNotNullAndFinishedAtNull(testUser)).thenReturn(userBooks);
        when(bookMapper.entityToDto(testBook)).thenReturn(testBookDto);

        List<BookDto> result = userBookService.getCurrentlyReading(testUserDto);

        assertEquals(1, result.size());
        assertEquals(testBookDto, result.getFirst());
        verify(userBookRepository).findByUserAndStartedAtIsNotNullAndFinishedAtNull(testUser);
    }

    @Test
    public void getRatedBooks_ReturnsRatedBooksWithDetails() {
        List<UserBook> ratedBooks = new ArrayList<>();
        testUserBook.setRated(true);
        testUserBook.setStars(5);
        testUserBook.setReviewText("Good book!");
        ratedBooks.add(testUserBook);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findByUserAndRatedTrue(testUser)).thenReturn(ratedBooks);

        List<UserBookDto> result = userBookService.getRatedBooks(testUserDto);

        assertEquals(1, result.size());
        UserBookDto dto = result.getFirst();
        assertEquals("Test Author", dto.getBookAuthor());
        assertEquals("Test Book", dto.getBookTitle());
        assertEquals(5, dto.getStars());
        assertEquals("Good book!", dto.getReviewText());
        verify(userBookRepository).findByUserAndRatedTrue(testUser);
    }

    @Test
    public void getBooksPerYear() {
        UserBook book1 = new UserBook();
        book1.setFinishedAt(LocalDate.now().withMonth(3).withDayOfMonth(15));

        UserBook book2 = new UserBook();
        book2.setFinishedAt(LocalDate.now().minusYears(1).withMonth(6).withDayOfMonth(20));

        UserBook book3 = new UserBook();
        book3.setFinishedAt(LocalDate.now().withMonth(10).withDayOfMonth(5));

        UserBook book4 = new UserBook();
        book4.setFinishedAt(null);

        List<UserBook> userBooks = new ArrayList<>(List.of(book1, book2, book3, book4));

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findUserBooksByUser(testUser)).thenReturn(userBooks);

        int result = userBookService.getBooksPerYear(testUserDto);

        assertEquals(2, result);
        verify(userBookRepository).findUserBooksByUser(testUser);
    }

    @Test
    public void getPagesPerYear_SumsPagesOfBooksFinishedInCurrentYear() {
        Book bookA = new Book();
        bookA.setPageNumber(200);
        UserBook userBook1 = new UserBook();
        userBook1.setBook(bookA);
        userBook1.setFinishedAt(LocalDate.now().withMonth(5).withDayOfMonth(10));

        Book bookB = new Book();
        bookB.setPageNumber(350);
        UserBook userBook2 = new UserBook();
        userBook2.setBook(bookB);
        userBook2.setFinishedAt(LocalDate.now().withMonth(8).withDayOfMonth(20));

        Book bookC = new Book();
        bookC.setPageNumber(150);
        UserBook userBook3 = new UserBook();
        userBook3.setBook(bookC);
        userBook3.setFinishedAt(LocalDate.now().minusYears(1).withMonth(12).withDayOfMonth(25));

        List<UserBook> userBooks = new ArrayList<>(List.of(userBook1, userBook2, userBook3));

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userBookRepository.findUserBooksByUser(testUser)).thenReturn(userBooks);

        int result = userBookService.getPagesPerYear(testUserDto);

        assertEquals(550, result);
        verify(userBookRepository).findUserBooksByUser(testUser);
    }
}
