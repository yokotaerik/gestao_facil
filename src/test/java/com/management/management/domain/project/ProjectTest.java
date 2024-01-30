package com.management.management.domain.project;

import com.management.management.domain.task.Task;
import com.management.management.domain.task.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectTest {

    @Test
    void calculateTimeExpected() {
        int task1ExpectedTime = 60 * 60 * 2;
        int task2ExpectedTime = 60 * 60 * 4;

        Task task1 = new Task();
        task1.setStatus(TaskStatus.DONE);
        task1.setTimeExpected(task1ExpectedTime);

        Task task2 = new Task();
        task2.setTimeExpected(task2ExpectedTime);

        Project project = new Project();
        project.getTasks().add(task1);
        project.getTasks().add(task2);

        project.calculateTimeExpected();

        assertEquals(task1ExpectedTime + task2ExpectedTime, project.getTimeExpected());
    }

    @Test
    void calculateProgress() {
        int task1ExpectedTime = 60 * 60 * 2;
        int task2ExpectedTime = 60 * 60 * 4;
        int projectExpectedTime = 60 * 60 * 10;

        Task task1 = new Task();
        task1.setStatus(TaskStatus.DONE);
        task1.setTimeExpected(task1ExpectedTime);

        Task task2 = new Task();
        task2.setStatus(TaskStatus.DONE);
        task2.setTimeExpected(task2ExpectedTime);

        Project project = new Project();
        project.setTimeExpected(projectExpectedTime);
        project.getTasks().add(task1);
        project.getTasks().add(task2);

        project.calculateProgress();

        assertEquals((double) (task1ExpectedTime + task2ExpectedTime) / projectExpectedTime, project.getProgress());
    }


}