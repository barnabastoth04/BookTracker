package com.booktracker.repositories;

import com.booktracker.model.User;
import com.booktracker.model.UserReadingDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReadingDaysRepository extends JpaRepository<UserReadingDays, Long> {
    List<UserReadingDays> findAllByUser(User user);
}
