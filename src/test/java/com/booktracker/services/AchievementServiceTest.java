package com.booktracker.services;

import com.booktracker.controllers.Dialog;
import com.booktracker.dtos.AchievementDto;
import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.AchievementMapper;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.Achievement;
import com.booktracker.model.User;
import com.booktracker.model.UserAchievement;
import com.booktracker.repositories.AchievementRepository;
import com.booktracker.repositories.UserAchievementRepository;
import com.booktracker.repositories.UserBookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private UserBookRepository userBookRepository;

    @InjectMocks
    private AchievementService achievementService;

    private User testUser;
    private UserDto testUserDto;
    private Achievement firstBookAchievement;
    private Achievement fiveBooksAchievement;
    private Achievement tenBooksAchievement;
    private AutoCloseable dialogMocks;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(100001L);
        testUser.setUsername("test.user");

        testUserDto = new UserDto();
        testUserDto.setId(100001L);
        testUserDto.setUsername("test.user");

        firstBookAchievement = new Achievement();
        firstBookAchievement.setId(100001L);
        firstBookAchievement.setCode("FIRST_BOOK");
        firstBookAchievement.setName("First Book");
        firstBookAchievement.setDescription("You have read 1 book");

        fiveBooksAchievement = new Achievement();
        fiveBooksAchievement.setId(100002L);
        fiveBooksAchievement.setCode("FIVE_BOOKS");
        fiveBooksAchievement.setName("Keep Going!");
        fiveBooksAchievement.setDescription("You have read 5 books");

        tenBooksAchievement = new Achievement();
        tenBooksAchievement.setId(100003L);
        tenBooksAchievement.setCode("TEN_BOOKS");
        tenBooksAchievement.setName("Bookworm");
        tenBooksAchievement.setDescription("You have read 10 books");

        dialogMocks = Mockito.mockConstruction(Dialog.class);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (dialogMocks != null) {
            dialogMocks.close();
        }
    }

    @Test
    public void checkAchievementsWhenFirstBookFinished() {
        when(userBookRepository.countByUserAndFinishedAtIsNotNull(testUser)).thenReturn(1L);
        when(achievementRepository.findByCode("FIRST_BOOK")).thenReturn(Optional.of(firstBookAchievement));
        when(userAchievementRepository.findByUserAndAchievement(testUser, firstBookAchievement)).thenReturn(Optional.empty());
        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        achievementService.checkAchievements(testUser);

        verify(userBookRepository).countByUserAndFinishedAtIsNotNull(testUser);
        verify(achievementRepository).findByCode("FIRST_BOOK");
        verify(userAchievementRepository).findByUserAndAchievement(testUser, firstBookAchievement);
        verify(userAchievementRepository).save(any(UserAchievement.class));

        verify(achievementRepository, never()).findByCode("FIVE_BOOKS");
        verify(achievementRepository, never()).findByCode("TEN_BOOKS");
    }

    @Test
    public void checkAchievementsWhenFiveBooksFinished() {
        when(userBookRepository.countByUserAndFinishedAtIsNotNull(testUser)).thenReturn(5L);

        when(achievementRepository.findByCode("FIRST_BOOK")).thenReturn(Optional.of(firstBookAchievement));
        when(userAchievementRepository.findByUserAndAchievement(testUser, firstBookAchievement)).thenReturn(Optional.empty());

        when(achievementRepository.findByCode("FIVE_BOOKS")).thenReturn(Optional.of(fiveBooksAchievement));
        when(userAchievementRepository.findByUserAndAchievement(testUser, fiveBooksAchievement)).thenReturn(Optional.empty());

        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        achievementService.checkAchievements(testUser);

        verify(userBookRepository).countByUserAndFinishedAtIsNotNull(testUser);
        verify(achievementRepository).findByCode("FIRST_BOOK");
        verify(achievementRepository).findByCode("FIVE_BOOKS");
        verify(userAchievementRepository, times(2)).save(any(UserAchievement.class));
        verify(achievementRepository, never()).findByCode("TEN_BOOKS");
    }

    @Test
    public void checkAchievementsWhenTenBooksFinished() {
        when(userBookRepository.countByUserAndFinishedAtIsNotNull(testUser)).thenReturn(10L);
        when(achievementRepository.findByCode("FIRST_BOOK")).thenReturn(Optional.of(firstBookAchievement));
        when(achievementRepository.findByCode("FIVE_BOOKS")).thenReturn(Optional.of(fiveBooksAchievement));
        when(achievementRepository.findByCode("TEN_BOOKS")).thenReturn(Optional.of(tenBooksAchievement));

        when(userAchievementRepository.findByUserAndAchievement(testUser, firstBookAchievement)).thenReturn(Optional.empty());
        when(userAchievementRepository.findByUserAndAchievement(testUser, fiveBooksAchievement)).thenReturn(Optional.empty());
        when(userAchievementRepository.findByUserAndAchievement(testUser, tenBooksAchievement)).thenReturn(Optional.empty());
        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        achievementService.checkAchievements(testUser);

        verify(userBookRepository).countByUserAndFinishedAtIsNotNull(testUser);
        verify(achievementRepository).findByCode("FIRST_BOOK");
        verify(achievementRepository).findByCode("FIVE_BOOKS");
        verify(achievementRepository).findByCode("TEN_BOOKS");
        verify(userAchievementRepository, times(3)).save(any(UserAchievement.class));
    }

    @Test
    public void checkAchievementsWhenAchievementAlreadyUnlocked() {
        when(userBookRepository.countByUserAndFinishedAtIsNotNull(testUser)).thenReturn(5L);

        when(achievementRepository.findByCode("FIRST_BOOK")).thenReturn(Optional.of(firstBookAchievement));
        when(userAchievementRepository.findByUserAndAchievement(testUser, firstBookAchievement)).thenReturn(Optional.of(new UserAchievement()));

        when(achievementRepository.findByCode("FIVE_BOOKS")).thenReturn(Optional.of(fiveBooksAchievement));
        when(userAchievementRepository.findByUserAndAchievement(testUser, fiveBooksAchievement)).thenReturn(Optional.empty());

        when(userAchievementRepository.save(any(UserAchievement.class))).thenAnswer(inv -> inv.getArgument(0));

        achievementService.checkAchievements(testUser);

        verify(userBookRepository).countByUserAndFinishedAtIsNotNull(testUser);
        verify(achievementRepository).findByCode("FIRST_BOOK");
        verify(achievementRepository).findByCode("FIVE_BOOKS");
        verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
    }

    @Test
    public void checkAchievementsWhenAchievementNotFound() {
        when(userBookRepository.countByUserAndFinishedAtIsNotNull(testUser)).thenReturn(1L);
        when(achievementRepository.findByCode("FIRST_BOOK")).thenReturn(Optional.empty()); // Not found

        achievementService.checkAchievements(testUser);

        verify(userBookRepository).countByUserAndFinishedAtIsNotNull(testUser);
        verify(achievementRepository).findByCode("FIRST_BOOK");
        verify(userAchievementRepository, never()).findByUserAndAchievement(any(), any());
        verify(userAchievementRepository, never()).save(any(UserAchievement.class));
    }

    @Test
    public void getAchievementsForUser() {
        UserAchievement userAchievement1 = new UserAchievement();
        userAchievement1.setId(1L);
        userAchievement1.setUser(testUser);
        userAchievement1.setAchievement(firstBookAchievement);

        UserAchievement userAchievement2 = new UserAchievement();
        userAchievement2.setId(2L);
        userAchievement2.setUser(testUser);
        userAchievement2.setAchievement(fiveBooksAchievement);

        List<UserAchievement> userAchievements = List.of(userAchievement1, userAchievement2);

        AchievementDto achievementDto1 = new AchievementDto();
        achievementDto1.setId(1L);
        achievementDto1.setCode("FIRST_BOOK");

        AchievementDto achievementDto2 = new AchievementDto();
        achievementDto2.setId(2L);
        achievementDto2.setCode("FIVE_BOOKS");

        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);
        when(userAchievementRepository.findAllByUser(testUser)).thenReturn(userAchievements);
        when(achievementMapper.entityToDto(firstBookAchievement)).thenReturn(achievementDto1);
        when(achievementMapper.entityToDto(fiveBooksAchievement)).thenReturn(achievementDto2);

        List<AchievementDto> result = achievementService.getAchievementsForUser(testUserDto);

        assertEquals(2, result.size());
        assertTrue(result.contains(achievementDto1));
        assertTrue(result.contains(achievementDto2));

        verify(userMapper).dtoToEntity(testUserDto);
        verify(userAchievementRepository).findAllByUser(testUser);
        verify(achievementMapper).entityToDto(firstBookAchievement);
        verify(achievementMapper).entityToDto(fiveBooksAchievement);
    }
}
