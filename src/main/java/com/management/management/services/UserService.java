package com.management.management.services;

import com.management.management.domain.user.dtos.RegisterEmployeeDTO;
import com.management.management.domain.user.dtos.RegisterManagerDTO;
import com.management.management.domain.user.User;
import com.management.management.domain.user.UserRole;
import com.management.management.exceptions.PasswordException;
import com.management.management.exceptions.PasswordMismatchException;
import com.management.management.exceptions.PasswordValidationException;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void createManager(RegisterManagerDTO data) throws PasswordException {

        if(!data.password().equals(data.confirmPassword())){
            throw new PasswordMismatchException("Password does not match");
        }


        if(isValidPassword(data.password())){
            throw new PasswordValidationException("Password does not meet criteria");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        String username = generateUsername(data.name(), data.surname());

        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.MANAGER);

        userRepository.save(user);
    }

    public String createEmployee(RegisterEmployeeDTO data) {
        int PASSWORD_SIZE = 8;


        String username = generateUsername(data.name(), data.surname());

        String password = gerenateRandomString(PASSWORD_SIZE);

        String encryptedPassword = new BCryptPasswordEncoder().encode(password);

        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.MANAGER);

        userRepository.save(user);

        return password;
    }

    private String generateUsername(String name, String surname) {
        String[] firstName = name.split(" ");
        String[] lastName = surname.split(" ");

        String username = String.join(".",firstName).toLowerCase() + "." + lastName[lastName.length - 1].toLowerCase();

        User user = userRepository.findByUsername(username);

        int count = 1;
        while (user != null) {
            username = username + count;
            user = userRepository.findByUsername(username);
            count += 1;
        }

        return username;
    }

    private String gerenateRandomString(int size) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder(size);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < size; i++) {
            int charIndex = random.nextInt(CHARS.length());
            char randomChar = CHARS.charAt(charIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

    public void changePassword(User user, String password, String confirmedPassword) throws PasswordException {
        if (password.equals(confirmedPassword)) {
            if (isValidPassword(password)) {
                throw new PasswordValidationException("Password does not meet criteria");
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(password);

            user.setPassword(encryptedPassword);

            userRepository.save(user);
        } else {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }


    private boolean isValidPassword(String password) {
        return password.length() < 8;
    }
}
