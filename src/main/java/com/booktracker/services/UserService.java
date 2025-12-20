package com.booktracker.services;

import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.User;
import com.booktracker.model.UserReadingDays;
import com.booktracker.repositories.UserReadingDaysRepository;
import com.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserReadingDaysRepository userReadingDaysRepository;

    public boolean validateUser(String username, String password) {
        Optional<User> optUser = userRepository.findByUsername(username);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            return false;
        }
        return PasswordSecurity.verify(password, user.getPassword());
    }

    public UserDto signUp(String username, String password, String email, String firstName, String lastName, String gender, LocalDate birthDate) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(PasswordSecurity.hash(password));
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
        Optional<User> optUser = userRepository.findByUsername(username);

        if (optUser.isPresent()) {
            return userMapper.entityToDto(optUser.get());
        }
        return null;
    }

    public boolean saveReading(LocalDate date, String username) {
        Optional<User> optUser = userRepository.findByUsername(username);

        if (optUser.isPresent()) {
            List<UserReadingDays> readingDaysOfUser = userReadingDaysRepository.findAllByUser(optUser.get());

            for (UserReadingDays rd : readingDaysOfUser) {
                if (rd.getDate().equals(date)) {
                    return false;
                }
            }

            UserReadingDays readingDays = new UserReadingDays();
            readingDays.setUser(optUser.get());
            readingDays.setDate(date);
            userReadingDaysRepository.save(readingDays);
            return true;
        }
        return false;
    }

    public int getDaysPerYear(UserDto userDto) {
        List<UserReadingDays> readingDays = userReadingDaysRepository.findAllByUser(userMapper.dtoToEntity(userDto));
        int counter = 0;

        if (readingDays.isEmpty()) {
            return counter;
        }

        for (UserReadingDays rd : readingDays) {
            if (rd.getDate().getYear() == LocalDate.now().getYear()) {
                counter++;
            }
        }
        return counter;
    }
}
