package com.management.management.controllers;

import com.management.management.domain.project.Project;
import com.management.management.domain.user.User;
import com.management.management.dtos.project.ProjectDTO;
import com.management.management.dtos.user.UsernameDTO;
import com.management.management.services.AuthorizationService;
import com.management.management.services.ProjectService;
import com.management.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProjectDTO data) {
        try {
            User manager = authorizationService.getCurrentUser();
            projectService.create(data, manager);
            return ResponseEntity.ok(SuccessResponse.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody ProjectDTO data, @PathVariable Long id){
        try {
            User manager = authorizationService.getCurrentUser();
            projectService.updateProject(id, data, manager);
            return ResponseEntity.ok(SuccessResponse.UPDATED);
        } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    @PatchMapping("/addEmployee/{id}")
    public ResponseEntity<?> addEmployee(@RequestBody UsernameDTO data, @PathVariable Long id) {
        try {
            User manager = authorizationService.getCurrentUser();
            Project project = projectService.findById(id);
            User employee =  userService.findByUsername(data.username());
            projectService.addEmployee(project, employee, manager);
            return ResponseEntity.ok(SuccessResponse.ADDED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/addManager/{id}")
    public ResponseEntity<?> addManager(@RequestBody UsernameDTO data, @PathVariable Long id) {
        try {
            User manager = authorizationService.getCurrentUser();
            Project project = projectService.findById(id);
            User newManager = userService.findByUsername(data.username());
            projectService.addManager(project, newManager, manager);
            return ResponseEntity.ok(SuccessResponse.ADDED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/removeEmployee/{id}")
    public ResponseEntity<?> removeEmployee(@RequestBody UsernameDTO data, @PathVariable Long id) {
        try {
            User manager = authorizationService.getCurrentUser();
            Project project = projectService.findById(id);
            User employee =  userService.findByUsername(data.username());
            projectService.removeEmployee(project, employee, manager);
            return ResponseEntity.ok(SuccessResponse.REMOVED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/removeManager/{id}")
    public ResponseEntity<?> removeManager(@RequestBody UsernameDTO data, @PathVariable Long id) {
        try {
            User manager = authorizationService.getCurrentUser();
            Project project = projectService.findById(id);
            User newManager =  userService.findByUsername(data.username());
            projectService.removeManager(project, newManager, manager);
            return ResponseEntity.ok(SuccessResponse.REMOVED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    private enum SuccessResponse {
        CREATED("Successfully created"),
        ADDED("Successfully added"),
        REMOVED("Successfully removed"),
        UPDATED("Successfully updated");

        private final String message;

        SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
