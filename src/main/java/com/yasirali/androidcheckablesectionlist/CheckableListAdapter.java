package com.yasirali.androidcheckablesectionlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yasirali on 8/25/15.
 */
public class CheckableListAdapter extends ArrayAdapter<CheckableListItem> {

    private int resourceIdItem;
    private int resourceIdSection;
    private int resourceSectionCheckBox;
    private int resourceIdItemCheckBox;

    private ArrayList<CheckableListItem> items;
    private LayoutInflater layoutInflater;
    private ISectionListDataManipulator manipulator;

    public CheckableListAdapter(Context context, int resourceIdItemLayout,
                                int resourceIdSectionLayout,
                                int resourceIdItemCheckBox,
                                int resourceSectionCheckBox,
                                ArrayList<CheckableListItem> items) {
        super(context, resourceIdItemLayout, items);
        this.resourceIdItem = resourceIdItemLayout;
        this.resourceIdSection = resourceIdSectionLayout;
        this.resourceSectionCheckBox = resourceSectionCheckBox;
        this.resourceIdItemCheckBox = resourceIdItemCheckBox;

        this.items = items;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        manipulator = (ISectionListDataManipulator) context;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CheckableListItem item = items.get(position);
        final ListView listView = (ListView)parent;

        // Display Section
        if(item.isSection()){
            convertView = layoutInflater.inflate(resourceIdSection, null);

            if(manipulator == null)
                throw new NullPointerException("Parent Activity must implement ISectionListDataManipulator");
            manipulator.onDisplaySection(convertView, position, item);


            CheckBox checkBox = (CheckBox) convertView.findViewById(resourceSectionCheckBox);
            checkBox.setChecked(item.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(position + 1 >= getCount())
                        return;

                    item.setChecked(isChecked);

                    for(int i = position + 1; i < getCount(); i++) {
                        CheckableListItem currentItem = items.get(i);
                        if(currentItem.isSection()) {
                            break;
                        } else {
                            currentItem.setChecked(isChecked);

                        }
                    }

                    notifyDataSetChanged();

                    // save index and top position
                    int index = listView.getFirstVisiblePosition();
                    View firstChild = listView.getChildAt(0);
                    int top = (firstChild == null) ? 0 : firstChild.getTop();
                    listView.setSelectionFromTop(index, top);
                }
            });
        }
        // Display Post
        else{
            convertView = layoutInflater.inflate(resourceIdItem, null);

            if(manipulator == null)
                throw new NullPointerException("Parent Activity must implement ISectionListDataManipulator");

            manipulator.onDisplayItem(convertView, position, item);

            CheckBox cb = (CheckBox)convertView.findViewById(resourceIdItemCheckBox);
            cb.setChecked(item.isChecked());

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    boolean areAllChecked = true;

                    int size = getCount();

                    if(position + 1 >= size)
                        return;

                    item.setChecked(isChecked);

                    // Forward Iteration
                    for (int i = position; i < size; i++) {

                        CheckableListItem item = items.get(i);
                        if (item.isSection()) {
                            break;
                        } else {
                            areAllChecked = item.isChecked();

                            // if any item is unChecked, stop further iteration
                            if (!areAllChecked)
                                break;
                        }
                    }


                    CheckableListItem section = null;

                    // Backward Iteration, until find the SectionHeader
                    // Find the SectionHeader for this item and set that SectionHeader Unchecked
                    for (int i = position; i >= 0; i--) {

                        CheckableListItem item = items.get(i);
                        if (item.isSection()) {
                            section = item;
                            break;
                        } else {
                            if (areAllChecked) {
                                areAllChecked = item.isChecked();

                                // if any item is unChecked, stop further iteration
                                if (!areAllChecked)
                                    break;
                            }
                        }
                    }


                    if (section != null) {
                        section.setChecked(areAllChecked);
                    }

                    notifyDataSetChanged();

                    // save index and top position
                    int index = listView.getFirstVisiblePosition();
                    View firstChild = listView.getChildAt(0);
                    int top = (firstChild == null) ? 0 : firstChild.getTop();
                    listView.setSelectionFromTop(index, top);


                }
            });
        }

        return convertView;
    }
}
