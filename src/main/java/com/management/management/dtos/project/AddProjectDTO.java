package com.management.management.dtos.project;

import java.time.LocalDate;
import java.util.Objects;

public record AddProjectDTO(String name, String description, LocalDate deadline) {
    public AddProjectDTO {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(deadline, "Deadline cannot be null");
    }
}
