package com.management.management.mapper;

import com.management.management.domain.project.Project;
import com.management.management.domain.task.Task;
import com.management.management.dtos.project.ProjectInfoDTO;
import com.management.management.dtos.task.EntireTaskDTO;
import com.management.management.dtos.task.TaskInfoDTO;
import com.management.management.dtos.user.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TaskMapper {


    @Autowired
    UserMapper userMapper;


    public List<TaskInfoDTO> taskInfoDTOList(List<Task> tasks){
        return tasks.stream().map(this::taskInfoDTO).collect(Collectors.toList());
    }

    public TaskInfoDTO taskInfoDTO(Task task){
        UserInfoDTO responsibleDTO = null;
        if (task.getResponsible() != null) {
            responsibleDTO = userMapper.userInfoDTO(task.getResponsible());
        }
        return new TaskInfoDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getTimeExpected(),
                task.getPriority(),
                task.getStatus(),
                task.getProject().getName(),
                responsibleDTO
        );
    }

    public EntireTaskDTO entireTaskDTO(Task task){
        UserInfoDTO responsibleDTO = null;
        if (task.getResponsible() != null) {
            responsibleDTO = userMapper.userInfoDTO(task.getResponsible());
        }

        return new EntireTaskDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                projectInfoDTO(task.getProject()),
                responsibleDTO,
                task.getStatus(),
                task.getTimeExpected());
    }

    private ProjectInfoDTO projectInfoDTO(Project project){
        return new ProjectInfoDTO(project.getId(), project.getName(), project.getProgress());
    }
}
