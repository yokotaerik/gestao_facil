package com.management.management.mapper;

import com.management.management.domain.project.Project;
import com.management.management.dtos.project.EntireProjectDTO;
import com.management.management.dtos.project.ProjectInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    UserMapper userMapper;


    public ProjectInfoDTO projectInfoDTO(Project project){
        return new ProjectInfoDTO(project.getId(), project.getName(), project.getProgress());
    }


    public List<ProjectInfoDTO> listProjectsInfoDTO(List<Project> projects) {
        return projects.stream()
                .map(this::projectInfoDTO)
                .collect(Collectors.toList());
    }

    public EntireProjectDTO projectToDTO(Project project) {
        return new EntireProjectDTO(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDeadline(),
                project.getTimeExpected(),
                project.getProgress(),
                project.getCreatedAt(),
                project.getFinishAt(),
                project.getEmployees().stream().map(employee -> userMapper.userInfoDTO(employee)).collect(Collectors.toList()),
                project.getManagers().stream().map(manager -> userMapper.userInfoDTO(manager)).collect(Collectors.toList()),
                project.getTasks().stream().map(task -> taskMapper.taskInfoDTO(task)).collect(Collectors.toList()));
    }
}
