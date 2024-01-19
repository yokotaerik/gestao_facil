package com.management.management.controllers;

import com.management.management.domain.user.*;
import com.management.management.infra.security.TokenService;
import com.management.management.services.AuthorizationService;
import com.management.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/register_manager")
    public ResponseEntity<String> register(@RequestBody RegisterManagerDTO data) {

        User user = userService.findByEmail(data.email());

        if (user != null) return ResponseEntity.badRequest().body("This email is in use");

        try {
            userService.createManager(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body("Manager successfully created");
    }

    @PostMapping("/register_employee")
    public ResponseEntity<?> createEmployee(@RequestBody RegisterEmployeeDTO data) {
        User user = userService.findByEmail(data.email());

        if (user != null) return ResponseEntity.badRequest().body("This email is in use");

        try {
            String password = userService.createEmployee(data);
            String successMessage = String.format("Employee successfully created with password: %s", password);
            return ResponseEntity.ok().body(successMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        try {

            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO(null));
        }
    }


    @PatchMapping("/change_password")
    public ResponseEntity changePassword(@RequestBody String password){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = (User) authorizationService.loadUserByUsername(userDetails.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            userService.changePassword(user, password);

            return ResponseEntity.ok("Password successfully updated");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
