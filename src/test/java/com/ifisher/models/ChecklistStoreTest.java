package com.ifisher.models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.inject.Inject;
import com.pivotallabs.injected.InjectedTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(InjectedTestRunner.class)
public class ChecklistStoreTest {

    @Inject ChecklistStore store;
    FakeListAdapter fakeListAdapter;

    @Before
    public void setUp() throws Exception {
        fakeListAdapter = new FakeListAdapter();
        store.bindListAdapter(fakeListAdapter);
    }

    @Test
    public void all_shouldReturnAnEmptyCollectionWhenThereAreNoChecklists() throws Exception {
        assertThat(store.all().size(), equalTo(0));
    }

    @Test
    public void all_shouldReturnAllStoredChecklists() throws Exception {
        store.store(new Checklist().setName("foo"));
        store.store(new Checklist().setName("bar"));
        assertThat(store.all().size(), equalTo(2));
    }

    @Test
    public void shouldNotifyItsAdapterWhenNewChecklistsAreStored() throws Exception {
        store.store(new Checklist());
        assertThat(fakeListAdapter.notifyDataSetChangedCalled, equalTo(true));
    }

    @Test
    public void shouldNotifyItsListenerAfterTheNewChecklistIsStored() throws Exception {
        final int[] sizeWhenNotified = {-1};
        store.bindListAdapter(new FakeListAdapter() {
            @Override
            public void notifyDataSetChanged() {
                sizeWhenNotified[0] = store.all().size();
            }
        });
        store.store(new Checklist());
        assertThat(sizeWhenNotified[0], equalTo(1));
    }

    private class FakeListAdapter extends BaseAdapter {
        boolean notifyDataSetChangedCalled;

        @Override
        public void notifyDataSetChanged() {
            notifyDataSetChangedCalled = true;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
