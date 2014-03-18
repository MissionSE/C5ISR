package com.missionse.kestrelweather.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

/**
 * Provides an info window to be used to represent a report on the map.
 */
public class ObservationCallout extends FrameLayout {
	private static final String TAG = ObservationCallout.class.getSimpleName();
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;
	private Report mReport;

	/**
	 * Constructor.
	 * @param context The current context.
	 */
	public ObservationCallout(final Context context) {
		super(context);
	}

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param attributeSet The set of attributes used to define the layout.
	 */
	public ObservationCallout(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	/**
	 * Constructor.
	 * @param context The current context.
	 * @param attributeSet The set of attributes used to define the layout.
	 * @param defStyle The default style of the layout.
	 */
	public ObservationCallout(final Context context, final AttributeSet attributeSet, final int defStyle) {
		super(context, attributeSet, defStyle);
	}

	public void setData(Report report) {
		this.mReport = report;

		setTemperature();
		setHumidity();
		setPressure();
		setWindSpeed();
		setWindDirection();
		setDelay();
		setWeatherIcon();
		setDewPoint();
		setWindChill();
	}

	private void setTemperature() {
		View view = findViewById(R.id.observation_data_top_left);
		TextView value = (TextView) view.findViewById(R.id.observation_data_top_left_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_top_left_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_top_left_icon);

		float temperature = UnitPrefs.getPreferredTemperature(getContext(), mReport.getKestrelWeather().getTemperature());
		value.setText(Integer.toString((int) temperature));
		unit.setText(UnitPrefs.getPreferredTemperatureUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_temperature);

		view.setVisibility(VISIBLE);
	}

	private void setHumidity() {
		View view = findViewById(R.id.observation_data_top_right);
		TextView value = (TextView) view.findViewById(R.id.observation_data_top_right_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_top_right_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_top_right_icon);

		value.setText(Integer.toString(mReport.getKestrelWeather().getHumidity()));
		unit.setText("%");
		icon.setImageResource(R.drawable.ic_humidity);

		view.setVisibility(VISIBLE);
	}

	private void setPressure() {
		View view = findViewById(R.id.observation_data_middle_right);
		TextView value = (TextView) view.findViewById(R.id.observation_data_middle_right_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_middle_right_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_middle_right_icon);

		float pressure = UnitPrefs.getPreferredPressure(getContext(), mReport.getKestrelWeather().getPressure());
		value.setText(Integer.toString((int) pressure));
		unit.setText(UnitPrefs.getPreferredPressureUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_pressure);

		view.setVisibility(VISIBLE);
	}

	private void setWindSpeed() {
		View view = findViewById(R.id.observation_data_middle_left);
		TextView value = (TextView) view.findViewById(R.id.observation_data_middle_left_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_middle_left_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_middle_left_icon);

		float windSpeed = UnitPrefs.getPreferredWindSpeed(getContext(), mReport.getKestrelWeather().getWindSpeed());
		value.setText(Integer.toString((int) windSpeed));
		unit.setText(UnitPrefs.getPreferredWindSpeedUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_wind_speed);

		view.setVisibility(VISIBLE);
	}

	private void setWindDirection() {
		View view = findViewById(R.id.observation_data_bottom_left);
		TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_left_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_left_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_bottom_left_icon);

		String windDirection = UnitPrefs.getPreferredWindDirection(getContext(), mReport.getKestrelWeather().getWindDirection());
		value.setText(windDirection);
		unit.setText(UnitPrefs.getPreferredWindDirectionUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_wind_direction);

		view.setVisibility(VISIBLE);
	}

	private void setWindChill() {
		View view = findViewById(R.id.observation_data_bottom_right);
		TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_right_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_right_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_bottom_right_icon);

		float temperature = UnitPrefs.getPreferredTemperature(getContext(), mReport.getKestrelWeather().getTemperature());
		value.setText(Integer.toString((int) temperature));
		unit.setText(UnitPrefs.getPreferredTemperatureUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_wind_chill);

		view.setVisibility(VISIBLE);
	}

	private void setDewPoint() {
		View view = findViewById(R.id.observation_data_bottom_center);
		TextView value = (TextView) view.findViewById(R.id.observation_data_bottom_center_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_bottom_center_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_bottom_center_icon);

		float temperature = UnitPrefs.getPreferredTemperature(getContext(), mReport.getKestrelWeather().getTemperature());
		value.setText(Integer.toString((int) temperature));
		unit.setText(UnitPrefs.getPreferredTemperatureUnitAbbr(getContext()));
		icon.setImageResource(R.drawable.ic_dew_point);

		view.setVisibility(VISIBLE);
	}

	private void setDelay() {
		View view = findViewById(R.id.observation_data_top_center);
		TextView value = (TextView) view.findViewById(R.id.observation_data_top_center_value);
		TextView unit = (TextView) view.findViewById(R.id.observation_data_top_center_unit);
		ImageView icon = (ImageView) view.findViewById(R.id.observation_data_top_center_icon);

		DateTime dataTime = mReport.getUpdateAt();
		DateTime now = DateTime.now();
		Seconds sec = Seconds.secondsBetween(dataTime, now);
		Minutes min = sec.toStandardMinutes();
		Hours hours = sec.toStandardHours();
		Days days = sec.toStandardDays();
		if (sec.getSeconds() < SECONDS_IN_MINUTE) {
			if (sec.getSeconds() < 0) {
				value.setText("0");
			} else {
				value.setText(Integer.toString(sec.getSeconds()));
			}
			unit.setText(R.string.secAbbr);
		} else if (min.getMinutes() < MINUTES_IN_HOUR) {
			value.setText(Integer.toString(min.getMinutes()));
			unit.setText(R.string.minAbbr);
		} else if (hours.getHours() < HOURS_IN_DAY) {
			value.setText(Integer.toString(hours.getHours()));
			unit.setText(R.string.hAbbr);
		} else {
			value.setText(Integer.toString(days.getDays()));
			unit.setText(R.string.days);
		}
		icon.setImageResource(R.drawable.ic_action_clock);

		view.setVisibility(VISIBLE);
	}

	private void setWeatherIcon() {
		View view = findViewById(R.id.observation_icon);
		if (view != null && mReport.getOpenWeather() != null) {
			Drawable background = view.getBackground();
			if (background != null) {
				background.setLevel(mReport.getOpenWeather().getConditionCode());
			}
		}
	}
}
