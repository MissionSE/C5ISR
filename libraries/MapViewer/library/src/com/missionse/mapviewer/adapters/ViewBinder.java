package com.missionse.mapviewer.adapters;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.WeakHashMap;

public class ViewBinder {

    private WeakHashMap<View, SparseArray<View>> mViewHolders = new WeakHashMap<View, SparseArray<View>>();

    public View getView(View parent, int viewId) {
        SparseArray<View> viewHolder = mViewHolders.get(parent);
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>(1);
            mViewHolders.put(parent, viewHolder);
        }
        View view = viewHolder.get(viewId);
        if (view == null) {
            view = parent.findViewById(viewId);
            viewHolder.put(viewId, view);
        }
        return view;
    }

    public static void setViewVisible(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    public void setViewVisible(View parent, int viewId, boolean visible) {
        setViewVisible(getView(parent, viewId), visible);
    }

    public static void setViewText(View view, String text) {
        setViewVisible(view, text != null && !"".equals(text));
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }
    public void setViewText(View parent, int viewId, String text) {
        setViewText((TextView) getView(parent, viewId), text);
    }

    public static void setViewBold(View view, boolean bold) {
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
        }
    }
    public void setViewBold(View parent, int viewId, boolean bold) {
        setViewBold(getView(parent, viewId), bold);
    }

    public static void setViewImageLevel(View view, int level) {
        setViewVisible(view, level > -1);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageLevel(level);
        }
    }
    public void setViewImageLevel(View parent, int viewId, int level) {
        setViewImageLevel(getView(parent, viewId), level);
    }

    public static void setViewBackgroundLevel(View view, int level) {
        Drawable background = view.getBackground();
        if (background != null) background.setLevel(level);
    }
    public void setViewBackgroundLevel(View parent, int viewId, int level) {
        setViewBackgroundLevel(getView(parent, viewId), level);
    }
}
