package com.booktracker.repositories;

import com.booktracker.model.Achievement;
import com.booktracker.model.User;
import com.booktracker.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);

    List<UserAchievement> findAllByUser(User user);
}
