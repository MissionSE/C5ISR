package com.missionse.mapsexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.util.Date;

public abstract class Callout extends FrameLayout {

    public Callout(Context context) {
        super(context);
    }

    public Callout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Callout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

}
