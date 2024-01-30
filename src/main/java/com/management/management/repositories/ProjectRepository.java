package com.management.management.repositories;

import com.management.management.domain.project.Project;
import com.management.management.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
