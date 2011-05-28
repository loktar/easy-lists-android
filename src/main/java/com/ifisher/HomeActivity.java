package com.ifisher;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.google.inject.Inject;
import com.ifisher.models.Checklist;
import com.ifisher.models.ChecklistStore;
import com.ifisher.views.home.ChecklistAdapter;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity extends RoboActivity {
    @InjectView(R.id.checklists_list) ListView checklistsListView;
    @InjectView(R.id.add_checklist_edit_text) EditText addChecklistsEditText;
    @Inject ChecklistAdapter checklistAdapter;
    @Inject ChecklistStore checklistStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        populateChecklists();
        bindEvents();
    }

    private void bindEvents() {
        addChecklistsEditText.setOnKeyListener(new AddChecklistKeyListener());
        checklistsListView.setOnItemClickListener(new ViewChecklistClickListener());
    }

    private void addChecklistFromView() {
        String checklistName = addChecklistsEditText.getText().toString();
        if (checklistName.length() > 0) {
            Checklist checklist = new Checklist().setName(checklistName);
            checklistStore.store(checklist);
            addChecklistsEditText.setText("");
        }
    }

    private void populateChecklists() {
        checklistsListView.setAdapter(checklistAdapter);
    }

    private class ViewChecklistClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

            Checklist checklist = (Checklist) adapterView.getItemAtPosition(i);
            intent.putExtra("checklist", checklist);

            startActivity(intent);
        }
    }

    private class AddChecklistKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i == KeyEvent.KEYCODE_ENTER) {
                addChecklistFromView();
            }
            return false;
        }
    }
}
