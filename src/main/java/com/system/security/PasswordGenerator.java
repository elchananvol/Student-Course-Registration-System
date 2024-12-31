package com.system.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final int PASSWORD_LENGTH = 12;

    /**
     * Generate a random password with a default length of 12 characters.
     *
     * @return Generated password.
     */
    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    /**
     * Encrypt the given password using BCryptPasswordEncoder.
     *
     * @param password Plain-text password to be encrypted.
     * @return Encrypted password.
     */
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}