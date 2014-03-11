package com.missionse.kestrelweather.map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;

import java.util.List;

/**
 * Provides a fragment that displays a google map.
 */
public class MapViewerFragment extends MapFragment {

	private GoogleMap mMap;
	private MapLoadedListener mMapLoadedListener;
	private OptionsMenuListener mOptionsMenuListener;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;

	/**
	 * Sets the listener that will receive a callback when the map is loaded.
	 * @param listener The listener that will receive the callback.
	 */
	public void setMapLoadedListener(final MapLoadedListener listener) {
		mMapLoadedListener = listener;
	}

	/**
	 * Sets the listener that will receive callbacks to handle the options menu.
	 * @param listener The listener that will receive the callbacks.
	 */
	public void setOptionsMenuListener(final OptionsMenuListener listener) {
		mOptionsMenuListener = listener;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (mOptionsMenuListener != null) {
			setHasOptionsMenu(true);
		}

		setUpMapIfNeeded();
	}

	@Override
	public void onResume() {
		super.onResume();

		setUpMapIfNeeded();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mOptionsMenuListener != null) {
			mOptionsMenuListener.onCreateOptionsMenu(menu, inflater);
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean selectionConsumed;
		if (mOptionsMenuListener != null) {
			selectionConsumed = mOptionsMenuListener.onOptionsItemSelected(item);
		} else {
			selectionConsumed = super.onOptionsItemSelected(item);
		}

		return selectionConsumed;
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) getActivity();

		if (activity != null) {
			mMarkersAdapter = new ObservationCalloutMarkersAdapter(activity, mMap);

			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			ReportTable reportTable = databaseAccessor.getReportTable();
			List<Report> reports = reportTable.queryForAll();
			mMarkersAdapter.setData(reports);
		}

		if (mMapLoadedListener != null) {
			mMapLoadedListener.mapLoaded(mMap);
		}
	}
}
