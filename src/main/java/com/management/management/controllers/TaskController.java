package com.management.management.controllers;

import com.management.management.domain.project.Project;
import com.management.management.domain.task.Task;
import com.management.management.domain.user.User;
import com.management.management.dtos.task.StatusDTO;
import com.management.management.dtos.task.TaskDTO;
import com.management.management.dtos.user.UsernameDTO;
import com.management.management.services.AuthorizationService;
import com.management.management.services.ProjectService;
import com.management.management.services.TaskService;
import com.management.management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {


    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;


    @PostMapping("/create/{id}")
    ResponseEntity<?> createTask(@RequestBody TaskDTO data, @PathVariable Long id){
        try{
            User manager = authorizationService.getCurrentUser();
            Project project = projectService.findById(id);
            taskService.createTask(data, project, manager);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/add")
    ResponseEntity<?> addResponsible(@RequestBody UsernameDTO data, @PathVariable Long id){
        try{
            User manager = authorizationService.getCurrentUser();
            User responsible = userService.findByUsername(data.username());
            Task task = taskService.findById(id);

            taskService.addResponsible(task, responsible, manager);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/remove")
    ResponseEntity<?> removeResponsible(@RequestBody UsernameDTO data, @PathVariable Long id){
        try{
            User manager = authorizationService.getCurrentUser();
            User responsible =  userService.findByUsername(data.username());
            Task task = taskService.findById(id);

            taskService.removeResponsible(task, responsible, manager);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    ResponseEntity<?> updateStatus(@RequestBody StatusDTO data, @PathVariable Long id){
        try{
            User user = authorizationService.getCurrentUser();
            Task task = taskService.findById(id);

            taskService.updateStatus(task, user, data.status());
            return ResponseEntity.ok().body("OK");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTask(@PathVariable Long id){
        try{
            User manager = authorizationService.getCurrentUser();
            Task task = taskService.findById(id);
            taskService.deleteTask(task, manager);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
