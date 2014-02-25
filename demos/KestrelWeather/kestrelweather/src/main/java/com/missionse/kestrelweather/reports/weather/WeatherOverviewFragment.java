package com.missionse.kestrelweather.reports.weather;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;

/**
 * A fragment used to display an overview of the weather conditions of a report.
 */
public class WeatherOverviewFragment extends Fragment {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
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
		View contentView = inflater.inflate(R.layout.fragment_report_detail_weather, container, false);
		if (contentView != null) {
			ImageView weatherStatusIcon = (ImageView) contentView.findViewById(R.id.report_detail_weather_icon);
			if (weatherStatusIcon != null) {
				weatherStatusIcon.setImageResource(R.drawable.weather_pict_cloudy);
			}

			TextView temperatureView = (TextView) contentView.findViewById(R.id.report_detail_temperature);
			if (temperatureView != null) {
				temperatureView.setText("70º");
			}

			TextView weatherConditionView = (TextView) contentView.findViewById(R.id.report_detail_weather_condition);
			if (weatherConditionView != null) {
				weatherConditionView.setText("Cloudy");
			}

			TextView latitudeView = (TextView) contentView.findViewById(R.id.report_detail_latitude);
			if (latitudeView != null) {
				latitudeView.setText("34.82938293");
			}

			TextView longitudeView = (TextView) contentView.findViewById(R.id.report_detail_longitude);
			if (longitudeView != null) {
				longitudeView.setText("-75.23590239");
			}
		}

		return contentView;
	}
}
