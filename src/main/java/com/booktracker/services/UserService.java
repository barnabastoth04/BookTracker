package com.booktracker.services;

import com.booktracker.model.User;
import com.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateUser(String username, String password) {
        if (!userRepository.existsByUsername(username)) {
            return false;
        }
        User user = userRepository.findByUsername(username);
        return user.getPassword().equals(password);
    }

    public User signUp(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);

        return userRepository.save(newUser);
    }

    public User signIn(String username) {
        if (userRepository.existsByUsername(username)) {
            return userRepository.findByUsername(username);
        } else {
            return null;
        }
    }
}
