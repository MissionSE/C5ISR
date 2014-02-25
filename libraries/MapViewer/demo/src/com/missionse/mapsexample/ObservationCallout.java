package com.missionse.mapsexample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.missionse.mapsexample.model.WeatherObservation;
import com.missionse.mapsexample.openweathermap.Weather;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

public class ObservationCallout extends Callout {
    private static final String TAG = ObservationCallout.class.getSimpleName();
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private WeatherObservation mObservation;

    public ObservationCallout(Context context) {
        super(context);
    }

    public ObservationCallout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ObservationCallout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setData(WeatherObservation observation) {
        this.mObservation = observation;

        setTemperature();
        setHumidity();
        setPressure();
        setWindSpeed();
        setDelay();
        setWeatherIcon();
//        setBottomCenter();
    }

    private void setTemperature() {
        View view = findViewById(R.id.observation_data_top_left);
        TextView value = (TextView) view.findViewById(R.id.observation_data_top_left_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_top_left_unit);

        //TODO convert value with respect to unit
        value.setText(Integer.toString((int) (this.mObservation.getData().getMain().getTemp() - 273.15)));
        unit.setText(getResources().getStringArray(R.array.temperature_units)[0]);

        view.setVisibility(VISIBLE);
    }

    private void setHumidity() {
        View view = findViewById(R.id.observation_data_top_right);
        TextView value = (TextView) view.findViewById(R.id.observation_data_top_right_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_top_right_unit);

        value.setText(Integer.toString(this.mObservation.getData().getMain().getHumidity()));
        unit.setText("%");

        view.setVisibility(VISIBLE);
    }

    private void setPressure() {
        View view = findViewById(R.id.observation_data_middle_right);
        TextView value = (TextView) view.findViewById(R.id.observation_data_middle_right_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_middle_right_unit);

        value.setText(Integer.toString((int) this.mObservation.getData().getMain().getPressure()));
        unit.setText("hPa");

        view.setVisibility(VISIBLE);
    }

    private void setWindSpeed() {
        View view = findViewById(R.id.observation_data_middle_left);
        TextView value = (TextView) view.findViewById(R.id.observation_data_middle_left_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_middle_left_unit);

        //TODO convert value with respect to unit
        value.setText(Double.toString(mObservation.getData().getWind().getSpeed()));
        unit.setText("mps");

        view.setVisibility(VISIBLE);
    }

    private void setBottomCenter() {
        View view = findViewById(R.id.observation_data_bottom_center);
        TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_center_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_center_unit);

        value.setText("1234567890");
        view.setVisibility(VISIBLE);
    }

    private void setDelay() {
        View view = findViewById(R.id.observation_data_top_center);
        TextView value = (TextView) view.findViewById(R.id.observation_data_top_center_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_top_center_unit);

        long dataDt = mObservation.getData().getDt() * 1000L;
        Log.d(TAG, "mObservation.getDt()=" + mObservation.getData().getDt());
        DateTime dataTime = new DateTime(dataDt);
        DateTime now = DateTime.now();
        Log.d(TAG, "dataTime=" + dataTime.toString());
        Log.d(TAG, "now=" + now);
        Seconds secs = Seconds.secondsBetween(dataTime, now);
        Minutes mins = secs.toStandardMinutes();
        Hours hours = secs.toStandardHours();
        Days days = secs.toStandardDays();
        if (secs.getSeconds() < SECONDS_IN_MINUTE) {
            if (secs.getSeconds() < 0) {
                value.setText("0");
            } else {
                value.setText(Integer.toString(secs.getSeconds()));
            }
            unit.setText(R.string.secAbbr);
        } else if (mins.getMinutes() < MINUTES_IN_HOUR) {
            value.setText(Integer.toString(mins.getMinutes()));
            unit.setText(R.string.minAbbr);
        } else if (hours.getHours() < HOURS_IN_DAY) {
            value.setText(Integer.toString(hours.getHours()));
            unit.setText(R.string.hAbbr);
        } else {
            value.setText(Integer.toString(days.getDays()));
            value.setText(R.string.days);
        }

        view.setVisibility(VISIBLE);
    }

    private void setWeatherIcon() {
        View view = findViewById(R.id.observation_button);
        Weather weather = mObservation.getData().getWeathers().get(0);
        if (weather != null) {
            view.getBackground().setLevel(weather.getId());
        }
    }

}
