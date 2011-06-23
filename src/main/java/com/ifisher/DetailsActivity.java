package com.ifisher;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.models.Checklist;
import com.ifisher.models.Task;
import com.ifisher.views.details.TaskAdapter;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class DetailsActivity extends RoboActivity {
    @Inject TaskAdapter taskAdapter;

    @InjectView(R.id.details_name_text_view) TextView nameTextView;
    @InjectView(R.id.tasks_list_view) ListView tasksListView;
    @InjectView(R.id.add_task_edit_text) EditText addTaskEditText;

    Checklist checklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_layout);

        checklist = (Checklist) getIntent().getSerializableExtra("checklist");
        nameTextView.setText(checklist.getName());

        bindEvents();

        taskAdapter.setDelegate(new TaskAdapterDelegate());
        tasksListView.setAdapter(taskAdapter.setItems(checklist.getTasks()));
    }

    private void bindEvents() {
        addTaskEditText.setOnKeyListener(new AddTaskKeyListener());
    }

    private void addTaskFromView() {
        String taskName = addTaskEditText.getText().toString();
        if (taskName.length() > 0) {
            checklist.addTask(new Task().setName(taskName));
            taskAdapter.notifyDataSetChanged();
            addTaskEditText.setText("");
        }
    }

    private void updateTaskName(Task task, String taskName) {
        if (taskName.length() > 0) {
            task.setName(taskName);
        } else {
            checklist.removeTask(task);
            taskAdapter.notifyDataSetChanged();
        }
    }

    private class AddTaskKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i == KeyEvent.KEYCODE_ENTER) {
                addTaskFromView();
            }
            return false;
        }
    }

    private class TaskAdapterDelegate implements TaskAdapter.Delegate {
        @Override
        public void updateTask(Task task, String taskName) {
            updateTaskName(task, taskName);
        }
    }
}
