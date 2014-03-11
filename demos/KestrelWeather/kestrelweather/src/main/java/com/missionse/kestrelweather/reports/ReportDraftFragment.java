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

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.ArrayList;

/**
 * Provides a fragment to show a list of reports.
 */
public class ReportDraftFragment extends Fragment {
	private static final String TAG = ReportDraftFragment.class.getSimpleName();
	private Activity mActivity;
	private DatabaseAccessor mDatabaseAccessor;
	private ReportAdapter mReportAdapter;
	private TextView mDraftCountView;

	/**
	 * Default constructor.
	 */
	public ReportDraftFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @return A new instance of the fragment ReportDraftFragment.
	 */
	public static ReportDraftFragment newInstance() {
		return new ReportDraftFragment();
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mDatabaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
		mDatabaseAccessor = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mActivity != null) {
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header,
					new ArrayList<Report>());
		}
	}

	private void updateDraftView() {
		if (mDraftCountView != null && mDatabaseAccessor != null) {
			int draftCount = mDatabaseAccessor.getDraftCount();
			mDraftCountView.setText(getResources().getString(R.string.report_draft_count) + " " + draftCount);
			if (draftCount == 0) {
				mDraftCountView.setTextColor(getResources().getColor(R.color.gray_medium));
				mDraftCountView.setBackgroundColor(getResources().getColor(R.color.gray_light));
			} else {
				mDraftCountView.setTextColor(getResources().getColor(R.color.white));
				mDraftCountView.setBackgroundColor(getResources().getColor(R.color.holo_green_dark));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_draft, container, false);
		if (contentView != null) {
			mDraftCountView = (TextView) contentView.findViewById(R.id.fragment_report_draft_count);
			updateDraftView();

			ListView reportList = (ListView) contentView.findViewById(R.id.fragment_report_draft_list);
			if (reportList != null) {
				reportList.setAdapter(mReportAdapter);
				reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
						FragmentManager fragmentManager = getFragmentManager();
						if (fragmentManager != null) {
							Fragment reportDetailFragment = ReportDetailFragment.newInstance(mReportAdapter.getItem(position).getId());
							fragmentManager.beginTransaction()
									.setCustomAnimations(
											R.animator.fade_in, R.animator.fade_out,
											R.animator.fade_in, R.animator.fade_out)
									.replace(R.id.content, reportDetailFragment, "report_detail")
									.addToBackStack("report_detail")
									.commit();
						}
					}
				});

				TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_draft_list_empty);
				if (emptyView != null) {
					reportList.setEmptyView(emptyView);
				}

				new ReportLoaderTask(mDatabaseAccessor, mReportAdapter).execute(true);
			}
		}

		return contentView;
	}
}
