package com.management.management.dtos.task;

import com.management.management.domain.task.TaskPriority;
import com.management.management.domain.task.TaskStatus;
import com.management.management.dtos.user.UserInfoDTO;

public record   TaskInfoDTO(Long id, String name, String description , int timeExpected, TaskPriority priority, TaskStatus status, String project, UserInfoDTO responsible) {
}
