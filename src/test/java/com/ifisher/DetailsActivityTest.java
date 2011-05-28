package com.ifisher;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.models.Checklist;
import com.ifisher.models.Task;
import com.pivotallabs.injected.InjectedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.inject.InjectView;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectedTestRunner.class)
public class DetailsActivityTest {
    @Inject DetailsActivity activity;
    @InjectView(R.id.details_name_text_view) TextView nameTextView;
    @InjectView(R.id.tasks_list_view) ListView tasksListView;
    @InjectView(R.id.add_task_edit_text) EditText addTaskEditText;

    @Test
    public void shouldShowTheSpecifiedChecklistsNameFromTheIntent() throws Exception {
        setupActivityFor(new Checklist().setName("Foo"));

        assertThat(nameTextView.getText().toString(), equalTo("Foo"));
    }

    @Test
    public void shouldShowTheChecklistsTasksInAList() throws Exception {
        Checklist checklist = new Checklist().setName("Foo")
                .addTask(new Task().setName("Stuff"))
                .addTask(new Task().setName("Things"))
                ;

        setupActivityFor(checklist);

        assertThat(((TextView) tasksListView.getChildAt(0).findViewById(R.id.task_name)).getText().toString(), equalTo("Stuff"));
        assertThat(((TextView) tasksListView.getChildAt(1).findViewById(R.id.task_name)).getText().toString(), equalTo("Things"));
    }

    @Test
    public void onCreate_shouldBeAbleToAddTasks() throws Exception {
        setupActivityFor(new Checklist().setName("Foo"));

        addTaskEditText.setText("Stuff");
        shadowOf(addTaskEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);
        addTaskEditText.setText("Things");
        shadowOf(addTaskEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(((TextView) tasksListView.getChildAt(0).findViewById(R.id.task_name)).getText().toString(), equalTo("Stuff"));
        assertThat(((TextView) tasksListView.getChildAt(1).findViewById(R.id.task_name)).getText().toString(), equalTo("Things"));
    }

    @Test
    public void addTask_shouldClearTheTaskFieldAfterAdding() throws Exception {
        setupActivityFor(new Checklist().setName("Foo"));

        addTaskEditText.setText("Stuff");
        shadowOf(addTaskEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(addTaskEditText.getText().toString(), equalTo(""));
    }

    @Test
    public void addTask_shouldNotAddATaskIfTheFieldIsBlank() throws Exception {
        Checklist checklist = new Checklist();
        setupActivityFor(checklist.setName("Foo"));

        addTaskEditText.setText("");
        shadowOf(addTaskEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(activity.checklist.getTasks().size(), equalTo(0));
        assertThat(tasksListView.getChildCount(), equalTo(0));
    }

    private void setupActivityFor(Checklist checklist) {
        Bundle bundle = new Bundle();
        activity.setIntent(new Intent().putExtra("checklist", checklist));

        activity.onCreate(bundle);
    }

}
