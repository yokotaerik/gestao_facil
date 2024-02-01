package com.management.management.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.management.domain.task.Task;
import com.management.management.domain.task.TaskStatus;
import com.management.management.domain.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
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
    private List<User> employees  = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "project_manager",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> managers  = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    private LocalDate deadline;

    private LocalDate createdAt;

    private LocalDate finishAt;

    private double progress;

    private int timeExpected;

    public Project(Long id, String name, String description, LocalDate deadline, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public void calculateTimeExpected() {
        List<Task> tasks = getTasks();

        int timeExpected = tasks.stream()
                .mapToInt(Task::getTimeExpected)
                .sum();

        setTimeExpected(timeExpected);
    }

    public void calculateProgress(){
        List<Task> tasks = getTasks();

        int howMuchIsDone = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.DONE)
                .mapToInt(Task::getTimeExpected)
                .sum();

        if (timeExpected > 0) {
            double progress = (double) howMuchIsDone / timeExpected;
            setProgress(progress);
        } else {
            setProgress(0.0);
        }
    }

}
