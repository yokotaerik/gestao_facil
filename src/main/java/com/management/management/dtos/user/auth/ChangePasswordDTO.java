package com.management.management.dtos.user.auth;

public record ChangePasswordDTO(String oldPassword, String password, String confirmPassword) {
}
