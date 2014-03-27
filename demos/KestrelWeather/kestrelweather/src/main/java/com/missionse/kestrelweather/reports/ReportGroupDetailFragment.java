package com.missionse.kestrelweather.reports;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataFragment;
import com.missionse.kestrelweather.reports.readings.ReadingsFragment;
import com.missionse.kestrelweather.reports.utils.ReportAdapter;
import com.missionse.kestrelweather.reports.utils.ReportGroup;
import com.missionse.kestrelweather.reports.weather.WeatherOverviewFragment;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

/**
 * Provides a fragment to show the details of a report group.
 */
public class ReportGroupDetailFragment extends Fragment {
	private ReportAdapter mReportAdapter;
	private ReportGroup mReportGroup;
	private Report mCurrentReport;
	private WeatherOverviewFragment mWeatherOverviewFragment;
	private ReadingsFragment mReadingsFragment;
	private AuxiliaryDataFragment mAuxiliaryDataFragment;

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reportGroup The group of reports to show in the fragment.
	 * @return A new instance of the fragment ReportGroupDetailFragment.
	 */
	public static ReportGroupDetailFragment newInstance(final ReportGroup reportGroup) {
		ReportGroupDetailFragment fragment = new ReportGroupDetailFragment();
		fragment.setReportGroup(reportGroup);

		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mReportAdapter = new ReportAdapter(getActivity(), R.layout.fragment_report_detail_header);
		mReportAdapter.addAll(mReportGroup.getReports());
	}

	/**
	 * Default constructor.
	 */
	public ReportGroupDetailFragment() {
	}

	public void setReportGroup(final ReportGroup reportGroup) {
		mReportGroup = reportGroup;
		mCurrentReport = reportGroup.getLatestReport();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_group_detail, container, false);
		if (view != null) {
			ViewPager viewPager = (ViewPager) view.findViewById(R.id.report_detail_view_pager);
			if (viewPager != null) {
				FragmentManager fragmentManager = getChildFragmentManager();
				if (fragmentManager != null) {
					mWeatherOverviewFragment = WeatherOverviewFragment.newInstance(mCurrentReport.getId());
					mReadingsFragment = ReadingsFragment.newInstance(mCurrentReport.getId());
					mAuxiliaryDataFragment = AuxiliaryDataFragment.newInstance(mCurrentReport.getId());

					SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(fragmentManager);
					pagerAdapter.setPage(0, getString(R.string.weather), mWeatherOverviewFragment);
					pagerAdapter.setPage(1, getString(R.string.kestrel_readings), mReadingsFragment);
					pagerAdapter.setPage(2, getString(R.string.auxiliary_data), mAuxiliaryDataFragment);
					viewPager.setAdapter(pagerAdapter);
				}
			}

			Spinner reportSpinner = (Spinner) view.findViewById(R.id.report_detail_group_list);
			if (reportSpinner != null) {
				reportSpinner.setAdapter(mReportAdapter);
				reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
						mCurrentReport = mReportAdapter.getItem(position);
						mWeatherOverviewFragment.updateReport(mCurrentReport);
						mReadingsFragment.updateReport(mCurrentReport);
						mAuxiliaryDataFragment.updateReport(mCurrentReport);
					}

					@Override
					public void onNothingSelected(final AdapterView<?> adapterView) {
					}
				});
			}
		}

		return view;
	}
}
