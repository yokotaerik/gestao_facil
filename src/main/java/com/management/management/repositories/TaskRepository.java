package com.management.management.repositories;

import com.management.management.domain.task.Task;
import com.management.management.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByResponsible(User responsible);
}
