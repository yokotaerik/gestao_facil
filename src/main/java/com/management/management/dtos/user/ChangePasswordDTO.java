package com.management.management.dtos.user;

public record ChangePasswordDTO(String oldPassword, String password, String confirmPassword) {
}
