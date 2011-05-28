package com.ifisher.models;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChecklistStore {
    private List<Checklist> checklists = new ArrayList<Checklist>();
    public BaseAdapter listAdapter;

    public List<Checklist> all() {
        return checklists;
    }

    public ChecklistStore store(Checklist checklist) {
        checklists.add(checklist);
        listAdapter.notifyDataSetChanged();
        return this;
    }

    public void bindListAdapter(BaseAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }
}
