package com.ifisher.views.details;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.R;
import com.ifisher.models.Task;
import com.pivotallabs.injected.InjectedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectedTestRunner.class)
public class TaskAdapterTest {
    @Inject TaskAdapter taskAdapter;

    @Test
    public void getView_shouldSetTheTaskName() throws Exception {
        TextView taskView = (TextView) taskAdapter.getView(new Task().setName("Foo"), null, null).findViewById(R.id.task_name);
        assertThat(taskView.getText().toString(), equalTo("Foo"));
    }

    @Test
    public void getView_shouldSetTheCheckboxStateWhenNotChecked() throws Exception {
        Task checked = new Task().setName("Foo").setIsChecked(false);
        CheckBox checkBox = (CheckBox) taskAdapter.getView(checked, null, null).findViewById(R.id.task_checkbox);

        assertThat(checkBox.isChecked(), equalTo(false));
    }

    @Test
    public void getView_shouldSetTheCheckboxStateWhenChecked() throws Exception {
        Task checked = new Task().setName("Foo").setIsChecked(true);
        CheckBox checkBox = (CheckBox) taskAdapter.getView(checked, null, null).findViewById(R.id.task_checkbox);

        assertThat(checkBox.isChecked(), equalTo(true));
    }

    @Test
    public void getView_shouldUseTheRecycleViewIfProvided() throws Exception {
        Task checked = new Task().setName("Foo").setIsChecked(true);
        View recycleView = taskAdapter.getView(checked, null, null);

        
    }

}
