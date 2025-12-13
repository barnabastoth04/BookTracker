package com.booktracker.services;

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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AchievementMapper achievementMapper;

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserBookRepository userBookRepository;

    public void checkAchievements(User user) {
        long finishedCount = userBookRepository.countByUserAndFinishedAtIsNotNull(user);

        if (finishedCount >= 1) {
            unlock("FISRT_BOOK", user);
        }

        if (finishedCount >= 5) {
            unlock("FIVE_BOOKS", user);
        }

        if (finishedCount >= 10) {
            unlock("TEN_BOOKS", user);
        }
    }

    private void unlock(String achievementCode, User user) {
        Achievement achievement = achievementRepository.findByCode(achievementCode).orElse(null);

        if (achievement == null) {
            return;
        }

        boolean alreadyUnlocked = userAchievementRepository.findByUserAndAchievement(user, achievement).isPresent();

        if (!alreadyUnlocked) {
            UserAchievement ua = new UserAchievement();
            ua.setUser(user);
            ua.setAchievement(achievement);
            userAchievementRepository.save(ua);
        }
    }

    public List<AchievementDto> getAchievementsForUser(UserDto userDto) {
        List<UserAchievement> achievements = userAchievementRepository.findAllByUser(userMapper.dtoToEntity(userDto));
        List<AchievementDto> result = new ArrayList<>();

        for (UserAchievement ua : achievements) {
            result.add(achievementMapper.entityToDto(ua.getAchievement()));
        }
        return result;
    }
}
