package com.ifisher.views.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.R;
import com.ifisher.models.Task;
import com.pivotallabs.GenericAdapter;

public class TaskAdapter extends GenericAdapter<Task> {

    @Inject LayoutInflater layoutInflater;

    @Override
    public View getView(Task task, View recycleView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.task_list_item, parent, false);
        ((TextView) view.findViewById(R.id.task_name)).setText(task.getName());
        ((CheckBox) view.findViewById(R.id.task_checkbox)).setChecked(task.isChecked());
        return view;
    }
}
