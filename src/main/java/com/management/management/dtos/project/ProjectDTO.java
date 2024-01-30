package com.management.management.dtos.project;

import java.time.LocalDate;

public record ProjectDTO(String name, String description, LocalDate deadline) {
}
