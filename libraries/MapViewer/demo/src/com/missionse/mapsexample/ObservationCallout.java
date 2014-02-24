package com.missionse.mapsexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.missionse.mapsexample.openweathermap.OpenWeatherMapData;

public class ObservationCallout extends Callout {
    private OpenWeatherMapData mData;

    public ObservationCallout(Context context) {
        super(context);
    }

    public ObservationCallout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ObservationCallout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setData(OpenWeatherMapData data) {
        this.mData = data;

        setTemperature();
        setHumidity();
        setPressure();
        setWindSpeed();
//        setBottomCenter();
    }

    private void setTemperature() {
        View view = findViewById(R.id.observation_data_top_left);
        TextView value = (TextView) view.findViewById(R.id.observation_data_top_left_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_top_left_unit);

        //TODO convert value with respect to unit
        value.setText(Double.toString(this.mData.getMain().getTemp() - 273.15));
        unit.setText(getResources().getStringArray(R.array.temperature_units)[0]);

        view.setVisibility(VISIBLE);
    }

    private void setHumidity() {
        View view = findViewById(R.id.observation_data_top_right);
        TextView value = (TextView) view.findViewById(R.id.observation_data_top_right_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_top_right_unit);

        value.setText(Integer.toString(this.mData.getMain().getHumidity()));
        unit.setText("%");

        view.setVisibility(VISIBLE);
    }

    private void setPressure() {
        View view = findViewById(R.id.observation_data_bottom_right);
        TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_right_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_right_unit);

        value.setText(Double.toString(this.mData.getMain().getPressure()));
        unit.setText("hPa");

        view.setVisibility(VISIBLE);
    }

    private void setWindSpeed() {
        View view = findViewById(R.id.observation_data_bottom_left);
        TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_left_value);
        TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_left_unit);

        //TODO convert value with respect to unit
        value.setText(Double.toString(mData.getWind().getSpeed()));
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

}