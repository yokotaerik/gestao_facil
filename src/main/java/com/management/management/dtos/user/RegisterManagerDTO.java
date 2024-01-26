package com.management.management.dtos.user;

import lombok.Getter;

public record RegisterManagerDTO(String name, String surname, String email, String password, String confirmPassword ) {
}
