package com.missionse.kestrelweather.reports.trends;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.reports.utils.ReportGroup;
import com.missionse.uiextensions.graph.Line;
import com.missionse.uiextensions.graph.LineGraph;
import com.missionse.uiextensions.graph.LinePoint;

/**
 * Provides a fragment to show a visual representation of trending graph data.
 */
public class TrendsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
	private Activity mActivity;
	private ReportGroup mReportGroup;
	private ArrayAdapter<CharSequence> mTrendsAdapter;
	private CharSequence mTemperatureTrend;
	private CharSequence mPressureTrend;
	private LineGraph mLineGraph;

	/**
	 * Default constructor.
	 */
	public TrendsFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reportGroup The group of reports used to create the trend.
	 * @return A new instance of the fragment TrendsFragment.
	 */
	public static TrendsFragment newInstance(final ReportGroup reportGroup) {
		TrendsFragment trendsFragment = new TrendsFragment();
		trendsFragment.setTrendData(reportGroup);
		return trendsFragment;
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

		mTrendsAdapter = ArrayAdapter.createFromResource(mActivity,
				R.array.report_trend_types, android.R.layout.simple_spinner_dropdown_item);
		mTrendsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mTemperatureTrend = getString(R.string.kestrel_simulation_temperature);
		mPressureTrend = getString(R.string.kestrel_simulation_pressure);
	}

	private void setTrendData(final ReportGroup reportGroup) {
		mReportGroup = reportGroup;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_report_trends, container, false);
		if (contentView != null) {
			Spinner trendSpinner = (Spinner) contentView.findViewById(R.id.report_trends_spinner);
			if (trendSpinner != null) {
				trendSpinner.setAdapter(mTrendsAdapter);
				trendSpinner.setOnItemSelectedListener(this);
			}

			mLineGraph = (LineGraph) contentView.findViewById(R.id.report_trends_graph);
		}

		return contentView;
	}

	@Override
	public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
		CharSequence selectedTrend = mTrendsAdapter.getItem(position);
		if (selectedTrend.equals(mTemperatureTrend)) {
			showTemperatureTrend();
		} else if (selectedTrend.equals(mPressureTrend)) {
			showPressureTrend();
		}
	}

	@Override
	public void onNothingSelected(final AdapterView<?> parent) {
	}

	private void showTemperatureTrend() {
		if (mLineGraph != null) {
			Line line = new Line();
			line.addPoint(new LinePoint(1, 1));
			line.addPoint(new LinePoint(2, 2));
			line.addPoint(new LinePoint(3, 3));
			line.setColor(mActivity.getResources().getColor(R.color.holo_blue_dark));
			line.setFill(true);

			mLineGraph.removeAllLines();
			mLineGraph.addLine(line);
			mLineGraph.setXRange(0, 4);
			mLineGraph.setYRange(0, 4);
		}
	}

	private void showPressureTrend() {
		if (mLineGraph != null) {
			Line line = new Line();
			line.addPoint(new LinePoint(1, 3));
			line.addPoint(new LinePoint(2, 2));
			line.addPoint(new LinePoint(3, 1));
			line.setColor(mActivity.getResources().getColor(R.color.holo_green_dark));
			line.setFill(true);

			mLineGraph.removeAllLines();
			mLineGraph.addLine(line);
			mLineGraph.setXRange(0, 4);
			mLineGraph.setYRange(0, 4);
		}
	}
}
