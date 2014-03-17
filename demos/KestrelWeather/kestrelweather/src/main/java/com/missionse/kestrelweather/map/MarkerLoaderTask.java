package com.missionse.kestrelweather.map;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a background task that loads reports into the marker adapter.
 */
public class MarkerLoaderTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = MarkerLoaderTask.class.getSimpleName();
	private static final double SEARCH_RANGE = 10;

	private DatabaseAccessor mDatabaseAccessor;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;
	private LatLngBounds mVisibleBounds;

	private List<Report> mReportList;

	/**
	 * Constructor.
	 * @param databaseAccessor An accessor to the database.
	 * @param markersAdapter The report adapter to populate.
	 * @param visibleBounds The bounds of the visible region.
	 */
	public MarkerLoaderTask(final DatabaseAccessor databaseAccessor, final ObservationCalloutMarkersAdapter markersAdapter,
			final LatLngBounds visibleBounds) {
		mDatabaseAccessor = databaseAccessor;
		mMarkersAdapter = markersAdapter;
		mVisibleBounds = visibleBounds;

		mReportList = new ArrayList<Report>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.d(TAG, "Starting search for reports in: " + mVisibleBounds.toString());
	}

	@Override
	protected Void doInBackground(final Void... parameters) {
		ReportTable reportTable = mDatabaseAccessor.getReportTable();
		if (reportTable != null) {
			for (Report report : reportTable.queryForAll()) {
				if (mVisibleBounds.contains(
						new LatLng(report.getLatitude(), report.getLongitude()))) {
					mReportList.add(report);
				}
			}
			Log.d(TAG, "Found " + mReportList.size() + " reports within the range.");
		}
		return null;
	}

	@Override
	protected void onPostExecute(final Void parameter) {
		super.onPostExecute(parameter);
		mMarkersAdapter.setData(mReportList);
	}
}
