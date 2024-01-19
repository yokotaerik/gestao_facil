package com.management.management.domain.user.dtos;

public record ChangePasswordDTO(String oldPassword, String password, String confirmPassword) {
}
