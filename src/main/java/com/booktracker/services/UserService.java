package com.booktracker.services;

import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.User;
import com.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserMapper userMapper;

    private final UserRepository userRepository;

    public boolean validateUser(String username, String password) {
        if (!userRepository.existsByUsername(username)) {
            return false;
        }

        Optional<User> optUser = userRepository.findByUsername(username);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public UserDto signUp(String username, String password, String email, String firstName, String lastName, String gender, LocalDate birthDate) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setGender(gender);
        newUser.setBirthDate(birthDate);
        newUser.setReadingDays(new HashSet<>());

        userRepository.save(newUser);

        return userMapper.entityToDto(newUser);
    }

    public UserDto signIn(String username) {
        if (userRepository.existsByUsername(username)) {
            Optional<User> optUser = userRepository.findByUsername(username);
            if (optUser.isPresent()) {
                return userMapper.entityToDto(optUser.get());
            }
        }
        return null;
    }
}
