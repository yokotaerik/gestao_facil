package com.management.management.dtos.project;

import com.management.management.dtos.task.TaskInfoDTO;
import com.management.management.dtos.user.UserInfoDTO;

import java.time.LocalDate;
import java.util.List;

public record EntireProjectDTO(Long id,
                               String name,
                               String description,
                               LocalDate deadline,
                               int timeExpected,
                               Double progress,
                               LocalDate createdAt,
                               LocalDate finishAt,
                               List<UserInfoDTO> employee,
                               List<UserInfoDTO> managers,
                               List<TaskInfoDTO> tasks

) {
}
