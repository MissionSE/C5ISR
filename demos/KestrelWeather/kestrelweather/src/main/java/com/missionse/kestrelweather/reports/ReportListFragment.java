package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.kestrelweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fragment to show a list of reports.
 */
public class ReportListFragment extends Fragment {
	private Activity mActivity;
	private ReportAdapter mReportAdapter;

	/**
	 * Default constructor.
	 */
	public ReportListFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @return A new instance of the fragment ReportListFragment.
	 */
	public static ReportListFragment newInstance() {
		return new ReportListFragment();
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

		List<Integer> reports = new ArrayList<Integer>();
		reports.add(1);
		reports.add(2);
		reports.add(3);
		mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header, reports);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_list, container, false);
		if (contentView != null) {
			ListView reportList = (ListView) contentView.findViewById(R.id.fragment_report_list);
			reportList.setAdapter(mReportAdapter);
			reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					FragmentManager fragmentManager = getFragmentManager();
					if (fragmentManager != null) {
						Fragment reportDetailFragment = ReportDetailFragment.newInstance(mReportAdapter.getItem(position));
						fragmentManager.beginTransaction()
								.setCustomAnimations(
										R.animator.slide_from_right, R.animator.slide_to_left,
										R.animator.slide_from_left, R.animator.slide_to_right)
								.replace(R.id.content, reportDetailFragment, "report_detail")
								.addToBackStack("report_detail")
								.commit();
					}
				}
			});

			TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_photos_empty);
			if (emptyView != null) {
				reportList.setEmptyView(emptyView);
			}
		}

		return contentView;
	}
}
