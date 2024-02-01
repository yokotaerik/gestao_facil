package com.management.management.dtos.task;

import com.management.management.domain.task.TaskPriority;
import com.management.management.domain.task.TaskStatus;

public record TaskInfoDTO(String name, String description , int timeExpected, TaskPriority priority, TaskStatus status) {
}
