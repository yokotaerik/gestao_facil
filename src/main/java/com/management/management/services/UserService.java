package com.management.management.services;

import com.management.management.domain.user.RegisterEmployeeDTO;
import com.management.management.domain.user.RegisterManagerDTO;
import com.management.management.domain.user.User;
import com.management.management.domain.user.UserRole;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void createManager(RegisterManagerDTO data) {

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        String username = generateUsername(data.name(), data.surname());


        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.MANAGER);

        userRepository.save(user);
    }

    public String createEmployee(RegisterEmployeeDTO data) {

        String username = generateUsername(data.name(), data.surname());

        String password = generatedRandomPassword();

        User user = new User(null, data.name(), data.surname(), username, data.email(), password, UserRole.MANAGER);

        userRepository.save(user);

        return password;
    }

    private String generateUsername(String name, String surname) {
        String[] firstName = name.split(" ");
        String[] lastName = surname.split(" ");

        String username = firstName[0].toLowerCase() + "." + lastName[lastName.length - 1].toLowerCase();

        User user = userRepository.findByUsername(username);

        int count = 1;
        while (user != null) {
            username = username + count;
            user = userRepository.findByUsername(username);
            count += 1;
        }

        return username;
    }

    private String generatedRandomPassword() {
        int SIZE = 8;
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder(SIZE);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < SIZE; i++) {
            int charIndex = random.nextInt(CHARS.length());
            char randomChar = CHARS.charAt(charIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

    public void changePassword(User user, String password){

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }

}
