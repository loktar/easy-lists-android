package com.ifisher.views.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.inject.Inject;
import com.ifisher.R;
import com.ifisher.models.Checklist;
import com.ifisher.models.ChecklistStore;
import com.pivotallabs.GenericAdapter;

public class ChecklistAdapter extends GenericAdapter<Checklist> {

    @Inject LayoutInflater layoutInflater;

    @Inject
    public ChecklistAdapter(ChecklistStore checklistStore) {
        setItems(checklistStore.all());
        checklistStore.bindListAdapter(this);
    }

    @Override
    public View getView(Checklist item, View recycleView, ViewGroup parent) {
        TextView view = (TextView) layoutInflater.inflate(R.layout.checklist_list_item, parent, false);
        view.setText(item.getName());
        return view;
    }
}
