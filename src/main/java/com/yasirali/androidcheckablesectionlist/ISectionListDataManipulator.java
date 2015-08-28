package com.yasirali.androidcheckablesectionlist;

import android.view.View;

/**
 * Created by yasirali on 8/28/15.
 */
public interface ISectionListDataManipulator<T1, T2> {
    void onDisplaySection(View parentView, int position, T1 section);
    void onDisplayItem(View parentView, int position, T2 item);
}
