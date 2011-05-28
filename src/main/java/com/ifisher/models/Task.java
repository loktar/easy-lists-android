package com.ifisher.models;

import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private boolean isChecked;

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Task setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
