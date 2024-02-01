package com.management.management.mapper;

import com.management.management.domain.task.Task;
import com.management.management.dtos.task.EntireTaskDTO;
import com.management.management.dtos.task.TaskInfoDTO;

import java.util.stream.Collectors;

public class TaskMapper {

    ProjectMapper projectMapper;

    UserMapper userMapper;

    public TaskInfoDTO taskInfoDTO(Task task){
        return new TaskInfoDTO(task.getName(), task.getDescription(), task.getTimeExpected(), task.getPriority(), task.getStatus());
    }

    public EntireTaskDTO entireTaskDTO(Task task){
        return new EntireTaskDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                projectMapper.projectInfoDTO(task.getProject()),
                task.getResponsible().stream().map(responsible -> userMapper.userInfoDTO(responsible)).collect(Collectors.toList()),
                task.getStatus(),
                task.getTimeExpected());
    }
}
