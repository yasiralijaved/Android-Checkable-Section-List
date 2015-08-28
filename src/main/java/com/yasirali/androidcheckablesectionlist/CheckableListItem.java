package com.yasirali.androidcheckablesectionlist;

/**
 * Created by yasirali on 8/26/15.
 */
public class CheckableListItem implements ICheckableListItem{

    private boolean isSection = false;
    private boolean isChecked = false;

    public CheckableListItem(boolean isSection, boolean isChecked) {
        this.isSection = isSection;
        this.isChecked = isChecked;
    }

    @Override
    public boolean isSection() {
        return isSection;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked){
        this.isChecked = checked;
    }
}
