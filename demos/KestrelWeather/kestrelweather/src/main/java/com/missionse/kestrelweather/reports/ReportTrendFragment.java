package com.missionse.kestrelweather.reports;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;

import java.util.List;

/**
 * Provides a fragment to show a visual representation of trending graph data.
 */
public class ReportTrendFragment extends Fragment {
	private static final String TAG = ReportTrendFragment.class.getSimpleName();
	private Activity mActivity;
	private DatabaseAccessor mDatabaseAccessor;

	private List<Report> mReportData;

	/**
	 * Default constructor.
	 */
	public ReportTrendFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reports a list of reports to use for the data to be shown
	 * @return A new instance of the fragment ReportTrendFragment.
	 */
	public static ReportTrendFragment newInstance(final List<Report> reports) {
		ReportTrendFragment reportTrendFragment = new ReportTrendFragment();

		reportTrendFragment.setTrendData(reports);

		return reportTrendFragment;
	}

	private void setTrendData(final List<Report> reports) {
		mReportData = reports;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_trend, container, false);
		LinearLayout chartContainer = (LinearLayout) contentView.findViewById(R.id.chart_container);

		GraphViewSeries exampleSeries = new GraphViewSeries(new GraphView.GraphViewData[] {
			new GraphView.GraphViewData(1, 2.0d),
			new GraphView.GraphViewData(2, 1.5d),
			new GraphView.GraphViewData(3, 2.5d),
			new GraphView.GraphViewData(4, 1.0d)
		});

		GraphView graphView = new LineGraphView(getActivity(), "GraphViewDemo");
		graphView.addSeries(exampleSeries); // data

		chartContainer.addView(graphView);

		return contentView;
	}
}
