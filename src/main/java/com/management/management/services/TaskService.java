package com.management.management.services;

import com.management.management.domain.project.Project;
import com.management.management.domain.task.Task;
import com.management.management.domain.task.TaskStatus;
import com.management.management.domain.user.User;
import com.management.management.dtos.task.AddTaskDTO;
import com.management.management.exceptions.NotAllowedException;
import com.management.management.repositories.ProjectRepository;
import com.management.management.repositories.TaskRepository;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectService projectService;

    public Task findById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    public void createTask(AddTaskDTO data, Project project, User manager) throws NotAllowedException {
        projectService.checkManagerPermission(manager, project);
        Task task = new Task(null, data.name(), data.description(), data.taskPriority(), data.timeExpected(), project);
        project.getTasks().add(task);
        project.calculateProgress();
        project.calculateTimeExpected();
        projectRepository.save(project);
        taskRepository.save(task);
    }

    public void addResponsible(Task task, User responsible, User manager) throws NotAllowedException {
        Project project = task.getProject();
        projectService.checkManagerPermission(manager, project);

        projectService.userIsOnProject(responsible, project);

        if (!isOnTask(task, responsible)) {
            task.getResponsible().add(responsible);
            responsible.getTasks().add(task);

            userRepository.save(responsible);
            taskRepository.save(task);
        }
    }

    public void removeResponsible(Task task, User responsible, User manager) throws NotAllowedException {
        Project project = task.getProject();
        projectService.checkManagerPermission(manager, project);

        if (isOnTask(task, responsible)) {
            task.getResponsible().remove(responsible);
            responsible.getTasks().remove(task);

            userRepository.save(responsible);
            taskRepository.save(task);
        }
    }


    public void updateStatus(Task task, User user, String status){

        isOnTask(task, user);

        try{
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(taskStatus);
            Project project = task.getProject();
            project.calculateProgress();
            projectRepository.save(project);
            taskRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status");
        }
    }

    public void deleteTask(Task task, User manager) throws NotAllowedException {

        if (!projectService.isManager(manager, task.getProject())) {
            throw new NotAllowedException("You need to be a manager of this project");
        }

        taskRepository.delete(task);
    }


    private boolean isOnTask(Task task, User responsible){
        boolean userOnTask = task.getResponsible().contains(responsible);
        boolean taskOnUser = responsible.getTasks().contains(task);

        return userOnTask && taskOnUser;
    }

}
