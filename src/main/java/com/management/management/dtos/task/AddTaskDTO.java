package com.management.management.dtos.task;

import com.management.management.domain.task.TaskPriority;

public record AddTaskDTO(String name, String description, TaskPriority taskPriority, int timeExpected) {
}
