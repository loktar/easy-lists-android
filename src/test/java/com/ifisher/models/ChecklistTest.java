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

    @Test
    public void removeTask_shouldRemoveTheTaskEvenIfItsNameHasChanged() throws Exception {
        Checklist checklist = new Checklist();
        Task foo = new Task().setName("Foo");
        checklist.addTask(foo);
        checklist.addTask(new Task().setName("Bar"));

        foo.setName("More foo");
        checklist.removeTask(foo);

        assertThat(checklist.getTasks().size(), equalTo(1));
        assertThat(checklist.getTasks().get(0).getName(), equalTo("Bar"));
    }
}
