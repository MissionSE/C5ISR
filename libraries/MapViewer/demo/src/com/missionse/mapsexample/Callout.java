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
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;

    public Callout(Context context) {
        super(context);
    }

    public Callout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Callout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

//    protected void setDelay(View view, Date date) {
//        TextView delayValue = (TextView) view.findViewById(R.id);
//        TextView delayUnit = (TextView) view.findViewById(R.id.unit_time);
//
//        Minutes mins = Minutes.minutesBetween(new DateTime(date), DateTime.now());
//        Hours hours = mins.toStandardHours();
//        Days days = mins.toStandardDays();
//
//        if (mins.getMinutes() < MINUTES_IN_HOUR) {
//            delayValue.setText(mins.getMinutes());
//            delayUnit.setText(R.string.minAbbr);
//        } else if (hours.getHours() < HOURS_IN_DAY) {
//            delayValue.setText(hours.getHours());
//            delayUnit.setText(R.string.hAbbr);
//        } else {
//            delayValue.setText(days.getDays());
//            delayValue.setText(R.string.days);
//        }
//    }

}
