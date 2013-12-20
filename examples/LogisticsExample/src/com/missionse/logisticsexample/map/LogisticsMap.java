package com.missionse.logisticsexample.map;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.logisticsexample.database.GetAllLocationsTask;
import com.missionse.logisticsexample.dialog.SiteDialogFragment;

/**
 * Displays logistics information on the google map.
 */
public class LogisticsMap implements MapLoadedListener, MapLocationListener,
		OnMapLongClickListener {

	private Activity mActivity;
	private GoogleMap mMap;

	private MapLocationManager mLocationManager;

	/**
	 * Constructor.
	 * @param activity The activity that holds the map.
	 */
	public LogisticsMap(final Activity activity) {
		mActivity = activity;

		mLocationManager = new MapLocationManager();
	}

	@Override
	public void mapLoaded(final GoogleMap map) {
		mMap = map;

		mMap.setBuildingsEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setOnMapLongClickListener(this);

		if (mActivity != null) {
			new GetAllLocationsTask(mActivity, this).execute();
		}
	}

	@Override
	public void locationReceived(final MapLocation location) {
		if (mMap != null) {
			int locationId = location.getId();
			if (!mLocationManager.containsLocation(locationId)) {
				mLocationManager.put(locationId, mMap.addMarker(
						new MarkerOptions()
								.position(location.getLatLng())
								.title(location.getName())));
			} else {
				Marker marker = mLocationManager.getMarker(locationId);
				marker.setPosition(location.getLatLng());
				marker.setTitle(location.getName());
			}

			mLocationManager.put(locationId, location);
		}
	}

	@Override
	public void onMapLongClick(LatLng location) {
		SiteDialogFragment.newInstance(location.latitude, location.longitude)
				.show(mActivity.getFragmentManager(), "create_new_site");
	}
}
