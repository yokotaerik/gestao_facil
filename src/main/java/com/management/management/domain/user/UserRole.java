package com.management.management.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Administrator"),
    EMPLOYEE("Employee"),
    MANAGER ("Manager");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

}
