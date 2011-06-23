package com.ifisher.views.details;

import android.view.KeyEvent;
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

    private Delegate delegate;

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View getView(Task task, View recycleView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.task_list_item, parent, false);

        getCheckBox(view).setChecked(task.isChecked());

        TextView taskNameView = getTaskNameView(view);
        taskNameView.setText(task.getName());
        taskNameView.setOnKeyListener(new TaskNameOnKeyListener(task));

        return view;
    }

    private TextView getTaskNameView(View view) {
        return (TextView) view.findViewById(R.id.task_name);
    }

    private CheckBox getCheckBox(View view) {
        return (CheckBox) view.findViewById(R.id.task_checkbox);
    }

    private class TaskNameOnKeyListener implements View.OnKeyListener {
        private Task task;

        public TaskNameOnKeyListener(Task task) {
            this.task = task;
        }

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            String taskName = ((TextView) view).getText().toString();
            delegate.updateTask(task, taskName);
            return false;
        }
    }

    public interface Delegate {
        void updateTask(Task task, String taskName);
    }
}
