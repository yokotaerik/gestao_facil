package domain.task;

import domain.project.Project;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Instant timeExpected;

    private Instant timeDone;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
