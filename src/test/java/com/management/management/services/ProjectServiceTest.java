package com.management.management.services;

import com.management.management.domain.project.Project;
import com.management.management.domain.user.User;
import com.management.management.dtos.project.ProjectDTO;
import com.management.management.exceptions.NotAllowedException;
import com.management.management.repositories.ProjectRepository;
import com.management.management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {

        ProjectDTO projectDTO = new ProjectDTO("Projeto Teste", "Descrição do projeto teste", LocalDate.now().plusDays(7));
        User user = new User();

        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setId(1L);
            return savedProject;
        });

        projectService.create(projectDTO, user);

        verify(projectRepository, times(1)).save(any(Project.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateProject() throws Exception {
        User manager = new User();
        Project project = new Project();
        project.setId(1L);

        project.getManagers().add(manager);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDTO projectDTO = new ProjectDTO("Curso", "Curso legal!", LocalDate.of(2025, 2, 28));

        projectService.updateProject(1L, projectDTO, manager );

        verify(projectRepository, times(1)).save(any(Project.class));

        assertEquals("Curso", project.getName());
        assertEquals("Curso legal!", project.getDescription());
        assertEquals( LocalDate.of(2025, 2, 28), project.getDeadline());
    }

    @Test
    void addEmployee() throws Exception {
        User manager = new User();
        Project project = new Project();
        User employee = new User();

        project.getManagers().add(manager);

        assertDoesNotThrow(() -> projectService.addEmployee(project, employee, manager));

        assertTrue(project.getEmployees().contains(employee));
        assertTrue(project.getManagers().contains(manager));
        assertTrue(employee.getProjectsWorked().contains(project));
    }

    @Test
    void addEmployeeNotManager() throws NotAllowedException {
        User manager = new User();
        Project project = new Project();
        User employee = new User();


        assertThrows(Exception.class,() -> projectService.addEmployee(project, employee, manager));
    }
}