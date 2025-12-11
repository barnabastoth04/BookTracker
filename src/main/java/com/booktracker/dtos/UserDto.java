package com.booktracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    LocalDate birthDate;
    private Set<LocalDate> readingDays;
}
