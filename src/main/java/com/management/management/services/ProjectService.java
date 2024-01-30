package com.management.management.services;

import com.management.management.domain.project.Project;
import com.management.management.domain.user.User;
import com.management.management.dtos.project.ProjectDTO;
import com.management.management.exceptions.NotAllowedException;
import com.management.management.repositories.ProjectRepository;
import com.management.management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    public void create(ProjectDTO data, User user) {

        LocalDate now = LocalDate.now();

        Project project = new Project(null, data.name(), data.description(), data.deadline(), now);

        project.getManagers().add(user);
        user.getProjectsManaged().add(project);
        projectRepository.save(project);
        userRepository.save(user);
    }

    public void checkManagerPermission(User manager, Project project) throws NotAllowedException {
        if (!isManager(manager, project)) {
            throw new NotAllowedException("You need to be a manager of this project");
        }
    }

    public void updateProject(Long projectId, ProjectDTO newData, User manager) throws Exception {
        Project project = findById(projectId);

        checkManagerPermission(manager, project);

        if (newData != null) {
            if (newData.name() != null) {
                project.setName(newData.name());
            }

            if (newData.description() != null) {
                project.setDescription(newData.description());
            }

            if (newData.deadline() != null) {
                project.setDeadline(newData.deadline());
            }

            projectRepository.save(project);
        }
    }

    public void addEmployee(Project project, User employee, User manager) throws NotAllowedException {
        checkManagerPermission(manager, project);
        handleWorkedProjects(employee, project);
    }

    public void addManager(Project project, User newManager, User manager) throws NotAllowedException {
        checkManagerPermission(manager, project);
        handleManagedProjects(newManager, project);
    }

    public void removeEmployee(Project project, User employee, User manager) throws NotAllowedException {
        checkManagerPermission(manager, project);
        handleRemoveEmployee(employee, project);
    }

    public void removeManager(Project project, User managerToRemove, User manager) throws NotAllowedException {
        checkManagerPermission(manager, project);
        handleRemoveManager(managerToRemove, project);
    }


    public Boolean isManager(User manager, Project project){
        return project.getManagers().contains(manager);
    }

    private void handleManagedProjects(User user, Project project) throws NotAllowedException {

        if (!user.getProjectsManaged().contains(project)) {
            user.getProjectsManaged().add(project);
            project.getManagers().add(user);
            userRepository.save(user);
            projectRepository.save(project);
        } else {
            throw new NotAllowedException("User is already manager on this project");
        }
    }

    private void handleWorkedProjects(User user, Project project) throws NotAllowedException {
        if (!user.getProjectsWorked().contains(project)) {
            user.getProjectsWorked().add(project);
            project.getEmployees().add(user);
            userRepository.save(user);
            projectRepository.save(project);
        } else {
            throw new NotAllowedException("Employee is already working on this project");
        }
    }

    private void handleRemoveEmployee(User user, Project project) {
        user.getProjectsWorked().remove(project);
        project.getEmployees().remove(user);
        userRepository.save(user);
        projectRepository.save(project);
    }

    private void handleRemoveManager(User user, Project project) {
        user.getProjectsManaged().remove(project);
        project.getManagers().remove(user);
        userRepository.save(user);
        projectRepository.save(project);
    }


}
