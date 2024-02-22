package com.management.management.services;

import com.management.management.dtos.user.auth.ChangePasswordDTO;
import com.management.management.dtos.user.auth.RegisterDTO;
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


    public User findByUsername(String username){
       return userRepository.findByUsername(username)
               .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    private Optional<User> findByUsernameToGenerateName(String username){

        return userRepository.findByUsername(username);

    }

    public void create(RegisterDTO data) throws Exception {

        checkDuplicateEmail(data.email());

        validatePassword(data.password(), data.confirmPassword());

        String encryptedPassword = passwordEncoder.encode(data.password());

        String username = generateUsername(data.name(), data.surname());

        User user = new User(null, data.name(), data.surname(), username, data.email(), encryptedPassword, UserRole.USER);

        userRepository.save(user);
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

        String username = String.join(".", firstName).toLowerCase() + "." + lastName[lastName.length - 1].toLowerCase();

        Optional<User> optionalUser = findByUsernameToGenerateName(username);

        if (optionalUser.isPresent()) {
            int count = 1;
            while (optionalUser.isPresent()) {
                username = username + count;
                optionalUser = findByUsernameToGenerateName(username);
                count += 1;
            }
        }

        return username;
    }

    private void checkDuplicateEmail(String email) throws Exception {
        UserDetails existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            throw new Exception("Email already in use");
        }
    }

    public UserDetails findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

}
