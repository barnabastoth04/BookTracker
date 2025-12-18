package com.booktracker.services;

import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.User;
import com.booktracker.model.UserReadingDays;
import com.booktracker.repositories.UserReadingDaysRepository;
import com.booktracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReadingDaysRepository userReadingDaysRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;
    private LocalDate testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDate.of(2025, 12, 15);

        testUser = new User();
        testUser.setUserId(100001L);
        testUser.setUsername("test.user");
        testUser.setPassword(PasswordSecurity.hash("password"));
        testUser.setEmail("test@bme.hu");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setGender("Male");
        testUser.setBirthDate(LocalDate.of(2004, 8, 29));
        testUser.setReadingDays(new HashSet<>());

        testUserDto = new UserDto();
        testUserDto.setId(100001L);
        testUserDto.setUsername("test.user");
        testUserDto.setEmail("test@bme.hu");
        testUserDto.setFirstName("Test");
        testUserDto.setLastName("User");
        testUserDto.setGender("Male");
        testUserDto.setBirthDate(LocalDate.of(2004, 8, 29));
    }

    @Test
    public void validateUserWhenUserExistsAndPasswordCorrect() {
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        boolean result = userService.validateUser("test.user", "password");

        assertTrue(result);
        verify(userRepository).findByUsername("test.user");
    }

    @Test
    public void validateUserWhenUserDoesNotExist() {
        when(userRepository.findByUsername("not.exists")).thenReturn(Optional.empty());

        boolean result = userService.validateUser("not.exists", "password");

        assertFalse(result);
        verify(userRepository).findByUsername("not.exists");
    }

    @Test
    public void validateUserWhenPasswordIncorrect() {
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        boolean result = userService.validateUser("test.user", "wrongpassword");

        assertFalse(result);
        verify(userRepository).findByUsername("test.user");
    }

    @Test
    public void signUpWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("exists.user")).thenReturn(true);

        UserDto result = userService.signUp("exists.user", "password", "existing@bme.hu",
                "Example", "User", "Male", LocalDate.of(1999, 1, 1));

        assertNull(result);
        verify(userRepository).existsByUsername("exists.user");
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).entityToDto(any(User.class));
    }

    @Test
    public void signUpWhenUserDoesNotExist() {
        when(userRepository.existsByUsername("new.user")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(100002L);
            return user;
        });
        when(userMapper.entityToDto(any(User.class))).thenReturn(testUserDto);

        UserDto result = userService.signUp("new.user", "password", "new@bme.hu",
                "New", "User", "Female", LocalDate.of(1999, 1, 1));

        assertNotNull(result);
        verify(userRepository).existsByUsername("new.user");
        verify(userRepository).save(any(User.class));
        verify(userMapper).entityToDto(any(User.class));
    }

    @Test
    public void signInWhenUserExists() {
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));
        when(userMapper.entityToDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.signIn("test.user");

        assertNotNull(result);
        assertEquals("test.user", result.getUsername());
        verify(userRepository).findByUsername("test.user");
        verify(userMapper).entityToDto(testUser);
    }

    @Test
    public void signInWhenUserDoesNotExist() {
        when(userRepository.findByUsername("not.exists")).thenReturn(Optional.empty());

        UserDto result = userService.signIn("not.exists");

        assertNull(result);
        verify(userRepository).findByUsername("not.exists");
        verify(userMapper, never()).entityToDto(any(User.class));
    }

    @Test
    public void saveReadingWhenUserExistsAndDateNotRecorded() {
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));
        when(userReadingDaysRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        boolean result = userService.saveReading(testDate, "test.user");

        assertTrue(result);
        verify(userRepository).findByUsername("test.user");
        verify(userReadingDaysRepository).findAllByUser(testUser);
        verify(userReadingDaysRepository).save(any(UserReadingDays.class));
    }

    @Test
    public void saveReadingWhenUserDoesNotExist() {
        when(userRepository.findByUsername("not.exists")).thenReturn(Optional.empty());

        boolean result = userService.saveReading(testDate, "not.exists");

        assertFalse(result);
        verify(userRepository).findByUsername("not.exists");
        verify(userReadingDaysRepository, never()).findAllByUser(any(User.class));
        verify(userReadingDaysRepository, never()).save(any(UserReadingDays.class));
    }

    @Test
    public void saveReadingWhenDateAlreadyRecorded() {
        UserReadingDays existingReadingDay = new UserReadingDays();
        existingReadingDay.setId(2L);
        existingReadingDay.setUser(testUser);
        existingReadingDay.setDate(testDate);

        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));
        when(userReadingDaysRepository.findAllByUser(testUser)).thenReturn(List.of(existingReadingDay));

        boolean result = userService.saveReading(testDate, "test.user");

        assertFalse(result);
        verify(userRepository).findByUsername("test.user");
        verify(userReadingDaysRepository).findAllByUser(testUser);
        verify(userReadingDaysRepository, never()).save(any(UserReadingDays.class));
    }

    @Test
    public void getDaysPerYearWhenUserHasNoReadingDays() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userReadingDaysRepository.findAllByUser(testUser)).thenReturn(new ArrayList<>());

        int result = userService.getDaysPerYear(testUserDto);

        assertEquals(0, result);
        verify(userMapper).dtoToEntity(testUserDto);
        verify(userReadingDaysRepository).findAllByUser(testUser);
    }

    @Test
    public void getDaysPerYearWhenUserHasReadingDaysInCurrentYear() {
        LocalDate currentYearDate1 = LocalDate.of(2025, 12, 15);
        LocalDate currentYearDate2 = LocalDate.of(2025, 12, 16);
        LocalDate previousYearDate = LocalDate.of(2024, 12, 17);

        UserReadingDays readingDay1 = new UserReadingDays();
        readingDay1.setDate(currentYearDate1);

        UserReadingDays readingDay2 = new UserReadingDays();
        readingDay2.setDate(currentYearDate2);

        UserReadingDays readingDay3 = new UserReadingDays();
        readingDay3.setDate(previousYearDate);

        List<UserReadingDays> readingDays = Arrays.asList(readingDay1, readingDay2, readingDay3);

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userReadingDaysRepository.findAllByUser(testUser)).thenReturn(readingDays);

        int result = userService.getDaysPerYear(testUserDto);

        assertEquals(2, result);
        verify(userMapper).dtoToEntity(testUserDto);
        verify(userReadingDaysRepository).findAllByUser(testUser);
    }
}
