package com.booktracker.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordSecurity {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    public static boolean verify(String plainPassword, String hash) {
        return encoder.matches(plainPassword, hash);
    }
}
