package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.database.sync.DatabaseSync;

import java.util.List;

/**
 * Created by rvieras on 2/26/14.
 */
public class RemoteReportListFragment  extends Fragment {
	private Activity mActivity;
	private ReportAdapter mReportAdapter;

	/**
	 * Default constructor.
	 */
	public RemoteReportListFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @return A new instance of the fragment ReportListFragment.
	 */
	public static RemoteReportListFragment newInstance() {
		return new RemoteReportListFragment();
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

		setHasOptionsMenu(true);

		if (mActivity != null) {
			DatabaseAccessor databaseAccessor = ((KestrelWeatherActivity) mActivity).getDatabaseAccessor();
			ReportTable reportTable = databaseAccessor.getReportTable();
			List<Report> reports = reportTable.queryForAll();
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header, reports);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_list, container, false);
		if (contentView != null) {
			ListView reportList = (ListView) contentView.findViewById(R.id.fragment_report_list);
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
								  R.animator.slide_from_right, R.animator.slide_to_left,
								  R.animator.slide_from_left, R.animator.slide_to_right)
							   .replace(R.id.content, reportDetailFragment, "report_detail")
							   .addToBackStack("report_detail")
							   .commit();
						}
					}
				});

				TextView emptyView = (TextView) contentView.findViewById(R.id.fragment_report_list_empty);
				if (emptyView != null) {
					reportList.setEmptyView(emptyView);
				}
			}
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.report_sync, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_sync_reports) {
			(new DatabaseSync(getActivity(),
			   ((KestrelWeatherActivity) getActivity()).getDatabaseAccessor())).execute(true,true,false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
