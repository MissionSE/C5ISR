package com.missionse.kestrelweather.reports.readings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.List;

/**
 * Provides a fragment used to display the readings of a report.
 */
public class ReadingsFragment extends Fragment {
	private static final String REPORT_ID = "report_id";

	private Activity mActivity;
	private int mReportId;
	private ReadingsAdapter mReadingsAdapter;

	/**
	 * Default constructor.
	 */
	public ReadingsFragment() {
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

		if (mActivity != null) {
			DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
			Report report = databaseAccessor.getReportById(mReportId);
			if (report != null) {
				List<ReadingsListItem> readingsListItems = ReadingsListFactory.getListItems(mActivity, report);
				mReadingsAdapter = new ReadingsAdapter(mActivity, R.layout.fragment_report_detail_readings_list_entry, readingsListItems);
			}
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_detail_readings, container, false);
		if (view != null) {
			ListView readingsList = (ListView) view.findViewById(R.id.report_detail_readings_list);
			if (readingsList != null) {
				readingsList.setAdapter(mReadingsAdapter);

				TextView emptyView = (TextView) view.findViewById(R.id.report_detail_readings_list_empty);
				if (emptyView != null) {
					readingsList.setEmptyView(emptyView);
				}
			}
		}

		return view;
	}
}
