package com.missionse.mapsexample;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;

public abstract class AsyncDrawable extends TransitionDrawable {

    private static final int PLACEHOLDER_ID = 1;
    private static final int LAYER_ID = 2;
    private int mDuration = 500;
    private AsyncDrawableListener mListener;

    public AsyncDrawable(Drawable placeholder) {
        super(new Drawable[]{placeholder, new ColorDrawable(Color.TRANSPARENT)});
        setId(0, PLACEHOLDER_ID);
        setId(1, LAYER_ID);
    }

    public AsyncDrawable() {
        this(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setListener(AsyncDrawableListener listener) {
        mListener = listener;
    }

    public abstract Drawable getBoundedDrawable(int width, int height);

    @Override
    public boolean isStateful() {
        return super.isStateful() || getDrawable(1).isStateful();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;
        if (width > 0 && height > 0) {
            new DrawableTask().execute(width, height);
        }
    }

    public static interface AsyncDrawableListener {
        public void onDrawableLoaded();
    }

    private class DrawableTask extends AsyncTask<Integer, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Integer... size) {
            return getBoundedDrawable(size[0], size[1]);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable == null) return;
            if (mDuration > 0) {
                setDrawableByLayerId(LAYER_ID, drawable);
                drawable.setBounds(getBounds());
                startTransition(mDuration);
            } else {
                setDrawableByLayerId(PLACEHOLDER_ID, drawable);
                drawable.setBounds(getBounds());
                resetTransition();
            }
            if (mListener != null) {
                mListener.onDrawableLoaded();
            }
        }
    }

}