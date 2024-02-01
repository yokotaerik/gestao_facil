package com.management.management.dtos.project;

import java.time.LocalDate;

public record AddProjectDTO(String name, String description, LocalDate deadline) {
}
