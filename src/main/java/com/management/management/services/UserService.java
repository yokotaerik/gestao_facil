package com.management.management.services;

import com.management.management.dtos.user.ChangePasswordDTO;
import com.management.management.dtos.user.RegisterEmployeeDTO;
import com.management.management.dtos.user.RegisterManagerDTO;
import com.management.management.domain.user.User;
import com.management.management.domain.user.UserRole;
import com.management.management.exceptions.PasswordException;
import com.management.management.exceptions.PasswordMismatchException;
import com.management.management.exceptions.PasswordValidationException;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void createManager(RegisterManagerDTO data) throws Exception {

        checkDuplicateEmail(data.email());

        validatePassword(data.password(), data.confirmPassword());

        String encryptedPassword = passwordEncoder.encode(data.password());

        String username = generateUsername(data.name(), data.surname());

        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.MANAGER);

        userRepository.save(user);
    }



    public String createEmployee(RegisterEmployeeDTO data) throws Exception {
        int PASSWORD_SIZE = 8;

        checkDuplicateEmail(data.email());

        String username = generateUsername(data.name(), data.surname());

        String password = gerenateRandomString(PASSWORD_SIZE);

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.EMPLOYEE);

        userRepository.save(user);

        return password;
    }


    public void changePassword(User user, ChangePasswordDTO data) throws PasswordException {
        if(passwordEncoder.matches(data.oldPassword(), user.getPassword())) {
                validatePassword(data.password(), data.confirmPassword());

                String encryptedPassword = passwordEncoder.encode(data.password());

                user.setPassword(encryptedPassword);

                userRepository.save(user);
            } else {
                throw new PasswordException("Old password is wrong");
            }
    }


    private boolean isValidPassword(String password) {
        return password.length() < 8;
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

    private void validatePassword(String password, String confirmPassword) throws PasswordException {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Password does not match");
        }

        if (isValidPassword(password)) {
            throw new PasswordValidationException("Password does not meet criteria");
        }
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

    private void checkDuplicateEmail(String email) throws Exception {
        UserDetails existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            throw new Exception("Email already in use");
        }
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

}
