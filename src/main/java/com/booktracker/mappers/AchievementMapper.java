package com.booktracker.mappers;

import com.booktracker.dtos.AchievementDto;
import com.booktracker.model.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AchievementMapper {
    @Mapping(target = "id", source = "id")
    public abstract AchievementDto entityToDto(Achievement achievement);
}
