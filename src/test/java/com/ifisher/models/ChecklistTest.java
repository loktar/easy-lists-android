package com.ifisher.models;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ChecklistTest {
    @Test
    public void getTasks_shouldReturnSavedTasks() throws Exception {
        Checklist checklist = new Checklist();
        checklist.addTask(new Task().setName("Foo"));
        checklist.addTask(new Task().setName("Bar"));

        List<Task> tasks = checklist.getTasks();
        assertThat(tasks.size(), equalTo(2));
        assertThat(tasks.get(0).getName(), equalTo("Foo"));
        assertThat(tasks.get(1).getName(), equalTo("Bar"));
    }

}
