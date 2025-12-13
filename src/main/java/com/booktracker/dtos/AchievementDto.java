package com.booktracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private long id;
    private String code;
    private String name;
    private String description;

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
