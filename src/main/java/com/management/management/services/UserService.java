package com.management.management.services;

import com.management.management.domain.user.RegisterManagerDTO;
import com.management.management.domain.user.User;
import com.management.management.enums.UserRole;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email);
    }

}
