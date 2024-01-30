package com.management.management.domain.task;

public enum TaskStatus {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return this == DONE;
    }
}
