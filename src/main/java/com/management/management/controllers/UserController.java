package com.management.management.controllers;

import com.management.management.domain.user.User;
import com.management.management.dtos.user.UserInfoDTO;
import com.management.management.exceptions.NotAllowedException;
import com.management.management.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    AuthorizationService authorizationService;

    @GetMapping("/me")
    ResponseEntity<?> getUserInfo() throws Exception {
        try {
            User user = authorizationService.getCurrentUser();

            return ResponseEntity.ok().body(new UserInfoDTO(user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
