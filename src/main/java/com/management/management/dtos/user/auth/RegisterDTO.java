package com.management.management.dtos.user.auth;

public record RegisterDTO(String name, String surname, String email, String password, String confirmPassword ) {
}
