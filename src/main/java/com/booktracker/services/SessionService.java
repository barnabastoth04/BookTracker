package com.booktracker.services;

import com.booktracker.dtos.UserDto;
import com.booktracker.mappers.UserMapper;
import com.booktracker.model.User;
import com.booktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private static User currentUser;

    public void setCurrentUser(UserDto userDto) {
        User user = userMapper.dtoToEntity(userDto);
        Optional<User> optUser = userRepository.findByUsername(user.getUsername());
        optUser.ifPresent(value -> currentUser = value);
    }

    public UserDto getCurrentUser() {
        return userMapper.entityToDto(currentUser);
    }

    public static void clear() {
        currentUser = null;
    }
}
