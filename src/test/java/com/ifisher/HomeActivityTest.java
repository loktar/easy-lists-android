package com.ifisher;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.models.Checklist;
import com.ifisher.models.ChecklistStore;
import com.pivotallabs.injected.InjectedTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(InjectedTestRunner.class)
public class HomeActivityTest {
    @Inject HomeActivity activity;
    @Inject ChecklistStore checklistStore;

    @Test
    public void shouldHaveAnActivityViaInjection() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveAListOfChecklists() throws Exception {
        activity.onCreate(null);
        assertTrue(activity.findViewById(R.id.checklists_list) instanceof ListView);
    }

    @Test
    public void shouldPopulateTheChecklistsList() throws Exception {
        Checklist things = new Checklist().setName("Things");
        Checklist stuff = new Checklist().setName("Stuff");
        checklistStore.store(things).store(stuff);

        activity.onCreate(null);

        ListView checklists = (ListView) activity.findViewById(R.id.checklists_list);
        assertThat(((TextView) checklists.getChildAt(0)).getText().toString(), equalTo("Things"));
        assertThat(((TextView) checklists.getChildAt(1)).getText().toString(), equalTo("Stuff"));
    }

    @Test
    public void shouldAddAChecklist() throws Exception {
        activity.onCreate(null);

        activity.addChecklistsEditText.setText("New checklist");
        shadowOf(activity.addChecklistsEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(checklistStore.all().size(), equalTo(1));
        assertThat(checklistStore.all().get(0).getName(), equalTo("New checklist"));
    }

    @Test
    public void shouldClearTheChecklistFieldAfterAdding() throws Exception {
        activity.onCreate(null);

        activity.addChecklistsEditText.setText("");
        shadowOf(activity.addChecklistsEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(checklistStore.all().size(), equalTo(0));
    }

    @Test
    public void addChecklist_shouldNotAddIfFieldIsBlank() throws Exception {
        activity.onCreate(null);

        activity.addChecklistsEditText.setText("New checklist");
        shadowOf(activity.addChecklistsEditText).onKeyDown(KeyEvent.KEYCODE_ENTER, null);

        assertThat(activity.addChecklistsEditText.getText().toString(), equalTo(""));
    }

    @Test
    public void shouldGoToDetailsViewAfterClickingAChecklist() throws Exception {
        checklistStore.store(new Checklist().setName("Foo"));
        activity.onCreate(null);

        shadowOf(activity.checklistsListView).clickFirstItemContainingText("Foo");

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(DetailsActivity.class.getName()));
    }

    @Test
    public void shouldPassTheChecklistToBeViewedToTheDetailsActivity() throws Exception {
        checklistStore.store(new Checklist().setName("Foo")).store(new Checklist().setName("Bar"));
        activity.onCreate(null);

        shadowOf(activity.checklistsListView).clickFirstItemContainingText("Bar");

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);

        Checklist checklist = (Checklist) shadowIntent.getExtras().getSerializable("checklist");
        assertThat(checklist.getName(), equalTo("Bar"));
    }

}
