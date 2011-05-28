package com.ifisher.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Checklist implements Serializable {
    private String name;
    private List<Task> tasks = new ArrayList<Task>();

    public String getName() {
        return name;
    }

    public Checklist setName(String name) {
        this.name = name;
        return this;
    }

    public Checklist addTask(Task task) {
        tasks.add(task);
        return this;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
