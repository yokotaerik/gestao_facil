package com.management.management.domain.task;

import com.management.management.domain.project.Project;
import com.management.management.domain.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private TaskPriority priority;

    private int timeExpected;

    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "task_responsible",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> responsible;

    public Task(Long id, String name, String description, TaskPriority priority, int timeExpected, Project project) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.timeExpected = timeExpected;
        this.project = project;
        this.status = TaskStatus.TO_DO;
    }
}
