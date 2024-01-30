package com.management.management.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Administrator"),
    USER ("USER");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

}
