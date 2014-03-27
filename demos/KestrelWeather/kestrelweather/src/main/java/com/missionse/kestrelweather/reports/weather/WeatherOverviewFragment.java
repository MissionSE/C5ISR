package com.missionse.kestrelweather.reports.weather;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;

import java.util.Formatter;

/**
 * A fragment used to display an overview of the weather conditions of a report.
 */
public class WeatherOverviewFragment extends Fragment {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private View mView;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Constructor.
	 */
	public WeatherOverviewFragment() {
	}

	/**
	 * A factory method used to create a new instance of the fragment with the provided parameters.
	 * @param reportId The database report id that is associated with the report.
	 * @return A new instance of a PhotoOverviewFragment.
	 */
	public static WeatherOverviewFragment newInstance(final int reportId) {
		WeatherOverviewFragment fragment = new WeatherOverviewFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(REPORT_ID, reportId);
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mReportId = getArguments().getInt(REPORT_ID);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_report_detail_weather, container, false);
		if (mView != null) {
			if (mActivity != null) {
				DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
				if (databaseAccessor != null) {
					Report report = databaseAccessor.getReportById(mReportId);
					if (report != null) {
						updateView(report);
					}
				}
			}
		}

		return mView;
	}

	/**
	 * Updates the report displayed by the fragment.
	 * @param report The new report to display in the fragment.
	 */
	public void updateReport(final Report report) {
		mReportId = report.getId();
		updateView(report);
	}

	private void updateView(final Report report) {
		if (mView != null) {
			if (report.getKestrelWeather() != null) {
				TextView temperatureView = (TextView) mView.findViewById(R.id.report_detail_temperature);
				if (temperatureView != null) {
					float temperature = UnitPrefs.getPreferredTemperature(mActivity,
							report.getKestrelWeather().getTemperature());
					Formatter formatter = new Formatter();
					temperatureView.setText(formatter.format("%.1f", temperature).toString());
				}

				TextView temperatureUnitsView = (TextView) mView.findViewById(R.id.report_detail_temperature_units);
				if (temperatureUnitsView != null) {
					temperatureUnitsView.setText(UnitPrefs.getPreferredTemperatureUnit(mActivity));
				}
			}

			if (report.getOpenWeather() != null) {
				TextView weatherConditionView = (TextView) mView.findViewById(R.id.report_detail_weather_condition);
				if (weatherConditionView != null) {
					weatherConditionView.setText(report.getOpenWeather().getDescription());
				}

				ImageView weatherIconView = (ImageView) mView.findViewById(R.id.report_detail_weather_icon);
				if (weatherIconView != null) {
					weatherIconView.setImageResource(
							WeatherIconFactory.getWeatherIcon(
									report.getOpenWeather().getConditionCode())
					);
				}
			}

			TextView latitudeView = (TextView) mView.findViewById(R.id.report_detail_latitude);
			if (latitudeView != null) {
				latitudeView.setText(Double.toString(report.getLatitude()));
			}

			TextView longitudeView = (TextView) mView.findViewById(R.id.report_detail_longitude);
			if (longitudeView != null) {
				longitudeView.setText(Double.toString(report.getLongitude()));
			}
		}
	}
}
