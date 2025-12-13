package com.booktracker.mappers;

import com.booktracker.dtos.UserDto;
import com.booktracker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Mapping(target = "id", source = "userId")
    public abstract UserDto entityToDto(User user);

    @Mapping(target = "userId", source = "id")
    public abstract User dtoToEntity(UserDto userDto);
}
