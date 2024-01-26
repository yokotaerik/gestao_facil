package com.management.management.domain.project;

import com.management.management.domain.task.Task;
import com.management.management.domain.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> employees;

    @ManyToMany
    @JoinTable(
            name = "project_manager",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> managers;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    private LocalDate deadline;

    private LocalDate createdAt;

    private LocalDate finishAt;

    private Instant timeExpected;


}
