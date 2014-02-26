package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataFragment;
import com.missionse.kestrelweather.reports.readings.ReadingsFragment;
import com.missionse.kestrelweather.reports.weather.WeatherOverviewFragment;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

/**
 * Provides a fragment to show the details of a report.
 */
public class ReportDetailFragment extends Fragment {
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Default constructor.
	 */
	public ReportDetailFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reportId The database ID associated with the report.
	 * @return A new instance of the fragment ReportDetailFragment.
	 */
	public static ReportDetailFragment newInstance(final int reportId) {
		ReportDetailFragment fragment = new ReportDetailFragment();

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
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
		if (view != null) {
			if (mActivity != null) {
				DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
				if (databaseAccessor != null) {
					Report report = databaseAccessor.getReportById(mReportId);
					if (report != null) {
						TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
						if (reportTitle != null) {
							reportTitle.setText(report.getUserName());
						}

						TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
						if (reportTimestamp != null) {
							reportTimestamp.setText(report.getCreatedAt().toString());
						}
					}
				}
			}

			ViewPager viewPager = (ViewPager) view.findViewById(R.id.report_detail_view_pager);
			if (viewPager != null) {
				FragmentManager fragmentManager = getChildFragmentManager();
				if (fragmentManager != null) {
					SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(fragmentManager);
					pagerAdapter.setPage(0, getString(R.string.weather), WeatherOverviewFragment.newInstance(mReportId));
					pagerAdapter.setPage(1, getString(R.string.kestrel_readings), ReadingsFragment.newInstance(mReportId));
					pagerAdapter.setPage(2, getString(R.string.auxiliary_data), AuxiliaryDataFragment.newInstance(mReportId));

					viewPager.setAdapter(pagerAdapter);
				}
			}
		}

		return view;
	}
}
