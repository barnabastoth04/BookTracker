package com.booktracker.services;

import com.booktracker.dtos.BookDto;
import com.booktracker.mappers.BookMapper;
import com.booktracker.model.Book;
import com.booktracker.model.Cover;
import com.booktracker.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookDto testBookDto;
    private final long testIsbn = 9789630797315L;
    private final Year testYear = Year.of(2020);
    private final Cover testCover = Cover.HARDCOVER;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setBookId(100001L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPublisher("Test Publisher");
        testBook.setYearOfPublication(testYear);
        testBook.setPageNumber(300);
        testBook.setCover(testCover);
        testBook.setIsbn(testIsbn);
        testBook.setGenre("Fiction");
        testBook.setFirstPublished(testYear);
        testBook.setOriginalTitle("Original Test Book");
        testBook.setTranslator("Test Translator");
        testBook.setIllustrator("Test Illustrator");
        testBook.setCoverImagePath("/com/booktracker/covers/book_1.jpg");

        testBookDto = new BookDto();
        testBookDto.setId(100001L);
        testBookDto.setTitle("Test Book");
        testBookDto.setAuthor("Test Author");
        testBookDto.setPublisher("Test Publisher");
        testBookDto.setYearOfPublication(testYear);
        testBookDto.setPageNumber(300);
        testBookDto.setCover(testCover);
        testBookDto.setIsbn(testIsbn);
        testBookDto.setGenre("Fiction");
        testBookDto.setFirstPublished(testYear);
        testBookDto.setOriginalTitle("Original Test Book");
        testBookDto.setTranslator("Test Translator");
        testBookDto.setIllustrator("Test Illustrator");
        testBookDto.setCoverImagePath("/com/booktracker/covers/book_1.jpg");
    }

    @Test
    public void addBookWhenIsbnDoesNotExist() {
        when(bookRepository.existsByIsbn(testIsbn)).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setBookId(1L);
            return book;
        });
        when(bookMapper.entityToDto(any(Book.class))).thenReturn(testBookDto);

        BookDto result = bookService.addBook("Test Book", "Test Author", "Test Publisher",
                testYear, 300, testCover, testIsbn, "Fiction", testYear, "Original Test Book",
                "Test Translator", "Test Illustrator");

        assertNotNull(result);
        assertEquals(testBookDto, result);
        verify(bookRepository).existsByIsbn(testIsbn);
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).entityToDto(any(Book.class));

        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals(testIsbn, result.getIsbn());
        assertEquals(300, result.getPageNumber());
        assertEquals(testCover, result.getCover());
    }

    @Test
    public void addBookWhenIsbnAlreadyExists() {
        when(bookRepository.existsByIsbn(testIsbn)).thenReturn(true);

        BookDto result = bookService.addBook("Test Book", "Test Author", "Test Publisher",
                testYear, 300, testCover, testIsbn, "Fiction", testYear, "Original Test Book",
                "Test Translator", "Test Illustrator");

        assertNull(result);
        verify(bookRepository).existsByIsbn(testIsbn);
        verify(bookRepository, never()).save(any(Book.class));
        verify(bookMapper, never()).entityToDto(any(Book.class));
    }

    @Test
    public void getBookByTitleWhenBookExists() {
        when(bookRepository.findByTitle("Test Book")).thenReturn(Optional.of(testBook));
        when(bookMapper.entityToDto(testBook)).thenReturn(testBookDto);

        BookDto result = bookService.getBookByTitle("Test Book");

        assertNotNull(result);
        assertEquals(testBookDto, result);
        verify(bookRepository).findByTitle("Test Book");
        verify(bookMapper).entityToDto(testBook);
    }

    @Test
    public void getBookByTitleWhenBookDoesNotExist() {
        when(bookRepository.findByTitle("Nonexistent Book")).thenReturn(Optional.empty());

        BookDto result = bookService.getBookByTitle("Nonexistent Book");

        assertNull(result);
        verify(bookRepository).findByTitle("Nonexistent Book");
        verify(bookMapper, never()).entityToDto(any(Book.class));
    }

    @Test
    public void getBookByIsbnWhenBookExists() {
        when(bookRepository.findByIsbn(testIsbn)).thenReturn(Optional.of(testBook));
        when(bookMapper.entityToDto(testBook)).thenReturn(testBookDto);

        BookDto result = bookService.getBookByIsbn(testIsbn);

        assertNotNull(result);
        assertEquals(testBookDto, result);
        verify(bookRepository).findByIsbn(testIsbn);
        verify(bookMapper).entityToDto(testBook);
    }

    @Test
    public void getBookByIsbnWhenBookDoesNotExist() {
        long nonexistentIsbn = 1234567890123L;
        when(bookRepository.findByIsbn(nonexistentIsbn)).thenReturn(Optional.empty());

        BookDto result = bookService.getBookByIsbn(nonexistentIsbn);

        assertNull(result);
        verify(bookRepository).findByIsbn(nonexistentIsbn);
        verify(bookMapper, never()).entityToDto(any(Book.class));
    }

    @Test
    public void searchWithValidKeyword() {
        String keyword = "test";

        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setTitle("Test Book 1");
        book1.setAuthor("Author 1");

        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Test Author");

        Book book3 = new Book();
        book3.setBookId(3L);
        book3.setTitle("Another Test Book");
        book3.setAuthor("Another Author");

        List<Book> byTitle = List.of(book1, book3);
        List<Book> byAuthor = List.of(book2);

        when(bookRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(byTitle);
        when(bookRepository.findByAuthorContainingIgnoreCase(keyword)).thenReturn(byAuthor);

        BookDto dto1 = new BookDto();
        dto1.setId(1L);
        dto1.setTitle("Test Book 1");

        BookDto dto2 = new BookDto();
        dto2.setId(2L);
        dto2.setTitle("Book 2");

        BookDto dto3 = new BookDto();
        dto3.setId(3L);
        dto3.setTitle("Another Test Book");

        when(bookMapper.entityToDto(book1)).thenReturn(dto1);
        when(bookMapper.entityToDto(book2)).thenReturn(dto2);
        when(bookMapper.entityToDto(book3)).thenReturn(dto3);

        List<BookDto> result = bookService.search(keyword);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        assertTrue(result.contains(dto3));

        verify(bookRepository).findByTitleContainingIgnoreCase(keyword);
        verify(bookRepository).findByAuthorContainingIgnoreCase(keyword);
        verify(bookMapper, times(3)).entityToDto(any(Book.class));
    }

    @Test
    public void searchWithBlankKeyword() {
        String[] blankKeywords = {"", "   ", "\t", "\n"};

        for (String keyword : blankKeywords) {
            List<BookDto> result = bookService.search(keyword);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        verify(bookRepository, never()).findByTitleContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByAuthorContainingIgnoreCase(anyString());
    }

    @Test
    public void saveCoverImageWhenBookDoesNotExist() {
        File mockImage = mock(File.class);
        when(bookRepository.findByIsbn(testIsbn)).thenReturn(Optional.empty());

        bookService.saveCoverImage(mockImage, testIsbn);

        verify(bookRepository).findByIsbn(testIsbn);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void saveCoverImageWhenBookExists() {
        File mockImage = mock(File.class);
        when(mockImage.getName()).thenReturn("cover.jpg");
        when(mockImage.toPath()).thenReturn(Paths.get("test.jpg"));

        when(bookRepository.findByIsbn(testIsbn)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
             MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

            Path mockDir = mock(Path.class);
            Path mockTarget = mock(Path.class);

            pathsMock.when(() -> Paths.get("src/main/resources/com/booktracker/covers")).thenReturn(mockDir);
            pathsMock.when(() -> Paths.get("test.jpg")).thenReturn(mockTarget);
            pathsMock.when(() -> Paths.get(eq("src/main/resources/com/booktracker/covers"), anyString())).thenReturn(mockTarget);

            filesMock.when(() -> Files.exists(mockDir)).thenReturn(true);
            filesMock.when(() -> Files.copy(any(Path.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(mockTarget);

            bookService.saveCoverImage(mockImage, testIsbn);

            verify(bookRepository).findByIsbn(testIsbn);
            verify(bookRepository).save(testBook);
            assertTrue(testBook.getCoverImagePath().startsWith("/com/booktracker/covers/book_1"));
            assertTrue(testBook.getCoverImagePath().endsWith(".jpg"));
        }
    }
}
