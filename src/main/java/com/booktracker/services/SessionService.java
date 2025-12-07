package com.booktracker.services;

import com.booktracker.model.User;
import com.booktracker.repositories.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserRepository userRepository;
    private static User currentUser;

    public void setCurrentUser(User user) {
        currentUser = userRepository.findByUsername(user.getUsername());
    }

    public static User findCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
