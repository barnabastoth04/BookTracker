package com.booktracker.services;

import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.User;
import com.booktracker.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    public void setUp() {
        SessionService.clear();

        testUser = new User();
        testUser.setUserId(100001L);
        testUser.setUsername("test.user");
        testUser.setEmail("test@bme.hu");
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        testUserDto = new UserDto();
        testUserDto.setId(100001L);
        testUserDto.setUsername("test.user");
        testUserDto.setEmail("test@bme.hu");
        testUserDto.setFirstName("Test");
        testUserDto.setLastName("User");
    }

    @AfterEach
    void tearDown() {
        SessionService.clear();
    }

    @Test
    public void setCurrentUserWhenUserExists() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        UserDto expectedDto = new UserDto();
        expectedDto.setId(100001L);
        expectedDto.setUsername("test.user");
        when(userMapper.entityToDto(testUser)).thenReturn(expectedDto);

        sessionService.setCurrentUser(testUserDto);

        verify(userMapper).dtoToEntity(testUserDto);
        verify(userRepository).findByUsername("test.user");

        UserDto currentUser = sessionService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("test.user", currentUser.getUsername());
    }

    @Test
    public void setCurrentUserWhenUserDoesNotExist() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.empty());

        sessionService.setCurrentUser(testUserDto);

        verify(userMapper).dtoToEntity(testUserDto);
        verify(userRepository).findByUsername("test.user");

        UserDto currentUser = sessionService.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    public void getCurrentUserWhenCurrentUserIsSet() {
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(testUser));

        UserDto expectedCurrentUserDto = new UserDto();
        expectedCurrentUserDto.setId(100001L);
        expectedCurrentUserDto.setUsername("test.user");

        when(userMapper.entityToDto(testUser)).thenReturn(expectedCurrentUserDto);

        sessionService.setCurrentUser(testUserDto);

        UserDto result = sessionService.getCurrentUser();

        assertNotNull(result);
        assertEquals(expectedCurrentUserDto, result);
        verify(userMapper).entityToDto(testUser);
    }
}
