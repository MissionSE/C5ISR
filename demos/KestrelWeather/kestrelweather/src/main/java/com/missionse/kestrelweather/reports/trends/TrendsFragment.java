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
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.preferences.UnitPrefs;
import com.missionse.kestrelweather.reports.utils.ReportGroup;
import com.missionse.uiextensions.graph.Line;
import com.missionse.uiextensions.graph.LineGraph;
import com.missionse.uiextensions.graph.LinePoint;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Provides a fragment to show a visual representation of trending graph data.
 */
public class TrendsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
	private static final float X_PADDING = 0.10f;
	private static final float Y_PADDING = 0.20f;

	private Activity mActivity;
	private ReportGroup mReportGroup;
	private int mSelectedReport = 0;
	private LineGraph mLineGraph;
	private ArrayAdapter<CharSequence> mTrendsAdapter;
	private CharSequence mTemperatureTrend;
	private CharSequence mPressureTrend;
	private int mTemperatureColor;
	private int mPressureColor;
	private Line mTemperatureLine;
	private Line mPressureLine;

	/**
	 * Default constructor.
	 */
	public TrendsFragment() {
	}

	/**
	 * A factory method used to create a new instance of this fragment.
	 * @param reportGroup The group of reports used to create the trend.
	 * @param selectedReport The initially selected report.
	 * @return A new instance of the fragment TrendsFragment.
	 */
	public static TrendsFragment newInstance(final ReportGroup reportGroup, final int selectedReport) {
		TrendsFragment trendsFragment = new TrendsFragment();
		trendsFragment.setTrendData(reportGroup);
		trendsFragment.setSelectedReport(selectedReport);
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

		mTrendsAdapter = new TrendsAdapter(
				mActivity,
				R.layout.fragment_report_trends_title_entry,
				getResources().getTextArray(R.array.report_trend_types));

		mTemperatureTrend = getString(R.string.kestrel_simulation_temperature);
		mPressureTrend = getString(R.string.kestrel_simulation_pressure);

		mTemperatureColor = getResources().getColor(R.color.holo_blue_dark);
		mPressureColor = getResources().getColor(R.color.holo_green_dark);

		createTemperatureLine();
		createPressureLine();
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
			showLine(mTemperatureLine);
		} else if (selectedTrend.equals(mPressureTrend)) {
			showLine(mPressureLine);
		}
	}

	@Override
	public void onNothingSelected(final AdapterView<?> parent) {
	}

	/**
	 * Sets the selected report.
	 * @param reportIndex The index of the report to be selected.
	 */
	public void setSelectedReport(final int reportIndex) {
		if (mReportGroup != null && mReportGroup.getCount() > reportIndex && reportIndex >= 0) {
			mSelectedReport = mReportGroup.getCount() - reportIndex - 1;

			if (mLineGraph != null) {
				for (Line line : mLineGraph.getLines()) {
					for (LinePoint point : line.getPoints()) {
						point.setSelected(false);
					}
					line.getPoints().get(mSelectedReport).setSelected(true);
				}
				mLineGraph.refresh();
			}
		}
	}

	private void createTemperatureLine() {
		mTemperatureLine = new Line();
		mTemperatureLine.setColor(mTemperatureColor);
		mTemperatureLine.setFill(true);

		List<Report> reportList = mReportGroup.getReports();
		for (Report report : reportList) {
			mTemperatureLine.addPoint(
					new LinePoint(
							report.getCreatedAt().getMillis(),
							UnitPrefs.getPreferredTemperature(mActivity, report.getKestrelWeather().getTemperature()))
			);
		}
	}

	private void createPressureLine() {
		mPressureLine = new Line();
		mPressureLine.setColor(mPressureColor);
		mPressureLine.setFill(true);

		List<Report> reportList = mReportGroup.getReports();
		for (Report report : reportList) {
			mPressureLine.addPoint(
					new LinePoint(
							report.getCreatedAt().getMillis(),
							UnitPrefs.getPreferredPressure(mActivity, report.getKestrelWeather().getPressure()))
			);
		}
	}

	private void showLine(final Line line) {
		if (mLineGraph != null) {
			mLineGraph.removeAllLines();
			mLineGraph.addLine(line);

			float xPadding = (line.getUpperBoundX() - line.getLowerBoundX()) * X_PADDING;
			if (xPadding == 0.0f) {
				xPadding = 1f;
			}
			float xLowerBound = line.getLowerBoundX() - xPadding;
			float xUpperBound = line.getUpperBoundX() + xPadding;
			mLineGraph.setXRange(xLowerBound, xUpperBound);

			DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd");
			mLineGraph.setXAxisBounds(
					formatter.print(new DateTime((long) xLowerBound)),
					formatter.print(new DateTime((long) xUpperBound)));

			float yLowerBound = line.getLowerBoundY();
			if (yLowerBound >= 0.0f) {
				yLowerBound = 0.0f;
			} else {
				yLowerBound += yLowerBound * Y_PADDING;
			}

			float yUpperBound = line.getUpperBoundY();
			if (yUpperBound >= 0) {
				yUpperBound += yUpperBound * Y_PADDING;
			} else {
				yUpperBound = 0.0f;
			}

			if (yLowerBound == yUpperBound) {
				yUpperBound += 1.0f;
			}
			mLineGraph.setYRange(yLowerBound, yUpperBound);
			mLineGraph.setYAxisBounds(
					Integer.toString((int) yLowerBound),
					Integer.toString((int) yUpperBound));

			for (LinePoint point : line.getPoints()) {
				point.setSelected(false);
			}
			line.getPoints().get(mSelectedReport).setSelected(true);
		}
	}
}
