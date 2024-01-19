package com.management.management.domain.user.dtos;

import lombok.Getter;

public record RegisterManagerDTO(String name, String surname, String email, String password, String confirmPassword ) {
}
