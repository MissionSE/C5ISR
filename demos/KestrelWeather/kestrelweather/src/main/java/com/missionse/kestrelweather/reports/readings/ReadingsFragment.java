package com.missionse.kestrelweather.reports.readings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.readings.impl.DewPointListItem;
import com.missionse.kestrelweather.reports.readings.impl.HeatIndexListItem;
import com.missionse.kestrelweather.reports.readings.impl.HumidityListItem;
import com.missionse.kestrelweather.reports.readings.impl.PressureListItem;
import com.missionse.kestrelweather.reports.readings.impl.PressureTrendListItem;
import com.missionse.kestrelweather.reports.readings.impl.TemperatureListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindChillListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindDirectionListItem;
import com.missionse.kestrelweather.reports.readings.impl.WindSpeedListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fragment used to display the readings of a report.
 */
public class ReadingsFragment extends Fragment {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private int mReportId = INVALID_REPORT_ID;
	private ReadingsAdapter mReadingsAdapter;

	/**
	 * Default constructor.
	 */
	public ReadingsFragment() {
	}

	/**
	 * Creates a new readings fragment with an unspecified report id.
	 * @return A new instance of the fragment.
	 */
	public static ReadingsFragment newInstance() {
		return ReadingsFragment.newInstance(INVALID_REPORT_ID);
	}

	/**
	 * Creates a new readings fragment with a specified report id.
	 * @param reportId - The database ID associated with the report.
	 * @return A new instance of the fragment.
	 */
	public static ReadingsFragment newInstance(final int reportId) {
		ReadingsFragment fragment = new ReadingsFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(REPORT_ID, reportId);
		fragment.setArguments(bundle);

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mReportId = getArguments().getInt(REPORT_ID);
		}

		List<ReadingsListItem> readingsListItems = new ArrayList<ReadingsListItem>();
		readingsListItems.add(new DewPointListItem(mActivity));
		readingsListItems.add(new TemperatureListItem(mActivity));
		readingsListItems.add(new HumidityListItem(mActivity));
		readingsListItems.add(new PressureListItem(mActivity));
		readingsListItems.add(new PressureTrendListItem(mActivity));
		readingsListItems.add(new HeatIndexListItem(mActivity));
		readingsListItems.add(new WindSpeedListItem(mActivity));
		readingsListItems.add(new WindDirectionListItem(mActivity));
		readingsListItems.add(new WindChillListItem(mActivity));
		mReadingsAdapter = new ReadingsAdapter(mActivity, R.layout.fragment_report_detail_readings_list_entry, readingsListItems);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_detail_readings, container, false);
		if (view != null) {
			ListView readingsList = (ListView) view.findViewById(R.id.report_detail_readings_list);
			if (readingsList != null) {
				readingsList.setAdapter(mReadingsAdapter);
			}
		}

		return view;
	}
}
