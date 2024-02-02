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

import java.util.List;

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

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findByResponsible(user);
    }

    public void createTask(AddTaskDTO data, Project project, User manager) throws NotAllowedException {
        projectService.checkManagerPermission(manager, project);
        Task task = new Task(null, data.name(), data.description(), data.taskPriority(), data.timeExpected(), project);
        project.getTasks().add(task);
        project.calculateTimeExpected();
        project.calculateProgress();
        projectRepository.save(project);
        taskRepository.save(task);
    }

    public void addResponsible(Task task, User responsible, User manager) throws Exception {
        Project project = task.getProject();
        projectService.checkManagerPermission(manager, project);

        projectService.userIsOnProject(responsible, project);

        if(task.getResponsible() != null){
            throw new Exception("Task already have a responsible");
        }

        task.setResponsible(responsible);
        responsible.getTasksResponsibleFor().add(task);

        userRepository.save(responsible);
        taskRepository.save(task);
    }

    public void removeResponsible(Task task, User manager) throws NotAllowedException {
        Project project = task.getProject();
        projectService.checkManagerPermission(manager, project);

            User responsible = task.getResponsible();
            responsible.getTasksResponsibleFor().remove(task);
            task.setResponsible(null);

            userRepository.save(responsible);
            taskRepository.save(task);
    }


    public void updateStatus(Task task, User user, String status) throws Exception {

        checkUserResponsibility(task, user);

        try {
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

        Project project = task.getProject();
        project.getTasks().remove(task);
        User user = task.getResponsible();
        user.getTasksResponsibleFor().remove(task);
        task.setResponsible(null);
        project.calculateTimeExpected();
        project.calculateProgress();
        taskRepository.delete(task);
    }

    private void checkUserResponsibility(Task task, User responsible) throws NotAllowedException {
        if (taskHasNoResponsible(task) || userIsNotResponsibleForTask(task, responsible)) {
            throw new NotAllowedException("User must be responsible for the task");
        }
    }

    private boolean taskHasNoResponsible(Task task) {
        return task.getResponsible() == null;
    }

    private boolean userIsNotResponsibleForTask(Task task, User responsible) {
        return !task.getResponsible().equals(responsible) && !responsible.getTasksResponsibleFor().contains(task);
    }

}
