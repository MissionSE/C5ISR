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

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.auxiliary.AuxiliaryDataFragment;
import com.missionse.kestrelweather.reports.readings.ReadingsFragment;
import com.missionse.uiextensions.viewpager.SectionFragmentPagerAdapter;

/**
 * This fragment will be how the user will create new reports.
 */
public class ReportDetailFragment extends Fragment {
	private static final String EDITABLE_REPORT = "edit_report";
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private Activity mActivity;
	private boolean mEditable = false;
	private int mReportId = INVALID_REPORT_ID;

	/**
	 * Default constructor.
	 */
	public ReportDetailFragment() {
	}

	/**
	 * @param editable - Boolean to determine if the report is editable.
	 * @return A new instance of fragment ReportDetailFragment.
	 * @see ReportDetailFragment#newInstance(boolean, int)
	 */
	public static ReportDetailFragment newInstance(boolean editable) {
		return newInstance(editable, INVALID_REPORT_ID);
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 * @param editable - Boolean to determine if the report is editable.
	 * @param reportId - The database ID associated with the report.
	 * @return A new instance of fragment ReportDetailFragment.
	 */
	public static ReportDetailFragment newInstance(boolean editable, int reportId) {
		ReportDetailFragment fragment = new ReportDetailFragment();

		Bundle bundle = new Bundle();
		bundle.putBoolean(EDITABLE_REPORT, editable);
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
			mEditable = getArguments().getBoolean(EDITABLE_REPORT);
			mReportId = getArguments().getInt(REPORT_ID);
		}

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_detail, container, false);
		if (view != null) {
			TextView reportTitle = (TextView) view.findViewById(R.id.report_detail_title);
			if (reportTitle != null) {
				reportTitle.setText("Test Report");
			}

			TextView reportTimestamp = (TextView) view.findViewById(R.id.report_detail_timestamp);
			if (reportTimestamp != null) {
				reportTimestamp.setText("Fakeday 2014");
			}

			ViewPager viewPager = (ViewPager) view.findViewById(R.id.report_detail_view_pager);
			if (viewPager != null) {
				FragmentManager fragmentManager = getChildFragmentManager();
				if (fragmentManager != null) {
					SectionFragmentPagerAdapter pagerAdapter = new SectionFragmentPagerAdapter(fragmentManager);
					pagerAdapter.setPage(0, mActivity.getString(R.string.kestrel_readings), ReadingsFragment.newInstance(mReportId));
					pagerAdapter.setPage(1, mActivity.getString(R.string.auxiliary_data), AuxiliaryDataFragment.newInstance(mReportId));

					viewPager.setAdapter(pagerAdapter);
				}
			}
		}

		return view;
	}
}
