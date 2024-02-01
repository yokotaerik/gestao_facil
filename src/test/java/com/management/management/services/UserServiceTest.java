package com.management.management.services;

import com.management.management.dtos.user.auth.ChangePasswordDTO;
import com.management.management.dtos.user.auth.RegisterDTO;
import com.management.management.domain.user.User;
import com.management.management.exceptions.PasswordException;
import com.management.management.exceptions.PasswordMismatchException;
import com.management.management.exceptions.PasswordValidationException;
import com.management.management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        RegisterDTO userDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "strongPassword123", "strongPassword123");

        assertDoesNotThrow(() -> userService.create(userDTO));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWithPasswordMismatch() {
        RegisterDTO userDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "strongPassword123", "wrongPassword");

        assertThrows(PasswordMismatchException.class, () -> userService.create(userDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserWithInvalidPassword() {
        RegisterDTO userDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "weak", "weak");



        assertThrows(PasswordValidationException.class, () -> userService.create(userDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserWithEmailInUse(){
        RegisterDTO userDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "weak", "weak");


        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(new User());

        assertThrows(Exception.class, () -> userService.create(userDTO));

        verify(userRepository, never()).save(any(User.class));

    }


    @Test
    void testChangePassword() {
        User user = new User();

        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "strongPassword", "strongPassword");

        assertDoesNotThrow(() -> userService.changePassword(user, changePasswordDTO));

        verify(userRepository, times(1)).save(any(User.class));

        assertTrue(new BCryptPasswordEncoder().matches(changePasswordDTO.password(), user.getPassword()));
    }

    @Test
    void testChangePasswordWithInvalidPassword() {
        User user = new User();

        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "weak", "weak");

        assertThrows(PasswordValidationException.class, () -> userService.changePassword(user, changePasswordDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePasswordMismatchPassword() {
        User user = new User();

        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "strongPassword", "wrongPassword");

        assertThrows(PasswordMismatchException.class, () -> userService.changePassword(user, changePasswordDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePasswordWrongPassword() {
        User user = new User();

        user.setPassword(new BCryptPasswordEncoder().encode("oldPassword"));

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("1233123113", "strongPassword", "wrongPassword");

        assertThrows(PasswordException.class, () -> userService.changePassword(user, changePasswordDTO));

        verify(userRepository, never()).save(any(User.class));
    }

}
