package com.ifisher.views.home;

import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.models.Checklist;
import com.ifisher.models.ChecklistStore;
import com.pivotallabs.injected.InjectedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectedTestRunner.class)
public class ChecklistAdapterTest {

    @Inject ChecklistAdapter checklistAdapter;
    @Inject ChecklistStore checklistStore;

    @Test
    public void getView_shouldInflateTheChecklistView() throws Exception {
        TextView view = (TextView) checklistAdapter.getView(new Checklist().setName("Stuff"), null, null);
        assertThat(view.getText().toString(), equalTo("Stuff"));
    }

    @Test
    public void shouldRegisterItselfAsTheChecklistStoresAdapter() throws Exception {
        assertThat(checklistStore.listAdapter, equalTo((BaseAdapter) checklistAdapter));
    }

}
