package com.management.management.controllers;

import com.management.management.domain.user.*;
import com.management.management.dtos.user.auth.ChangePasswordDTO;
import com.management.management.dtos.user.auth.LoginDTO;
import com.management.management.dtos.user.auth.LoginResponseDTO;
import com.management.management.dtos.user.auth.RegisterDTO;
import com.management.management.infra.security.TokenService;
import com.management.management.services.AuthorizationService;
import com.management.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO data) {

        try {
            userService.create(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("User successfully created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO(null));
        }
    }


    @PatchMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO data){
        try{
            User user = (User) authorizationService.getCurrentUser();
            userService.changePassword(user, data);

            return ResponseEntity.ok("Password successfully updated");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
