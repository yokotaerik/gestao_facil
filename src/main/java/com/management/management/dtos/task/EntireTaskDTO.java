package com.management.management.dtos.task;

import com.management.management.domain.task.TaskPriority;
import com.management.management.domain.task.TaskStatus;
import com.management.management.dtos.project.ProjectInfoDTO;
import com.management.management.dtos.user.UserInfoDTO;

import java.util.List;

public record EntireTaskDTO(Long id, String name, String description, TaskPriority priority, ProjectInfoDTO project, UserInfoDTO responsible, TaskStatus status, int timeExpected) {
}