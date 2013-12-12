package com.missionse.mapdatabaseexample.map;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Provides a fragment that displays a google map.
 */
public class MapViewerFragment extends MapFragment {

	private GoogleMap mMap;
	private MapLoadedListener mMapLoadedListener;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		setUpMapIfNeeded();
	}

	@Override
	public void onResume() {
		super.onResume();

		setUpMapIfNeeded();
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
		if (mMapLoadedListener != null) {
			mMapLoadedListener.mapLoaded(mMap);
		}
	}

	/**
	 * Sets the listener that will receive a callback when the map is loaded.
	 * @param listener The listener that will receive the callback.
	 */
	public void setMapLoadedListener(final MapLoadedListener listener) {
		mMapLoadedListener = listener;
	}
}
