package com.missionse.mapviewer.adapters;

import android.view.View;
import android.view.ViewGroup;

public abstract class DataViewBinder<TData> extends ViewBinder {

    public abstract void setViewValue(View view, TData data);

    public void setViewValue(View view, TData data, ViewGroup parent, int position) {
        setViewValue(view, data);
    }

    public void setViewValue(View view, TData data, ViewGroup parent, int groupPosition, boolean isExpanded) {
        setViewValue(view, data);
    }

    public void setViewValue(View view, TData data, ViewGroup parent, int groupPosition, int childPosition) {
        setViewValue(view, data);
    }
}