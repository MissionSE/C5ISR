package com.missionse.logisticsexample.map;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.dialog.OrderDialogFragment;
import com.missionse.logisticsexample.dialog.SiteDialogFragment;
import com.missionse.logisticsexample.model.Site;

import java.util.HashMap;
import java.util.List;

/**
 * Displays logistics information on the google map.
 */
public class LogisticsMap implements MapLoadedListener, OnMapLongClickListener,
		OnInfoWindowClickListener {

	private Activity mActivity;
	private GoogleMap mMap;

	private LocalDatabaseHelper mLocalDatabaseHelper;
	private HashMap<Integer, Marker> mMarkers;

	/**
	 * Constructor.
	 * @param activity The activity that holds the map.
	 */
	public LogisticsMap(final Activity activity) {
		mActivity = activity;

		mMarkers = new HashMap<Integer, Marker>();
	}

	/**
	 * Sets the local database helper.
	 * @param localDatabaseHelper Used to get locations from the local database.
	 */
	public void setDatabaseHelper(final LocalDatabaseHelper localDatabaseHelper) {
		mLocalDatabaseHelper = localDatabaseHelper;
	}

	@Override
	public void mapLoaded(final GoogleMap map) {
		mMap = map;

		mMap.setBuildingsEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnInfoWindowClickListener(this);

		updateLocations();
	}

	public void updateLocations() {
		if (mMap != null) {
			List<Site> sites = mLocalDatabaseHelper.getSites();
			for (Site site : sites) {
				int siteId = site.getId();
				LatLng siteLocation = new LatLng(site.getLatitude(), site.getLongitude());
				if (!mMarkers.containsKey(siteId)) {
					mMarkers.put(siteId, mMap.addMarker(
							new MarkerOptions()
									.position(siteLocation)
									.title(site.getName())));
				} else {
					Marker marker = mMarkers.get(siteId);
					marker.setPosition(siteLocation);
					marker.setTitle(site.getName());
				}
			}
		}
	}

	@Override
	public void onMapLongClick(LatLng location) {
		SiteDialogFragment.newInstance(location.latitude, location.longitude)
				.show(mActivity.getFragmentManager(), "create_new_site");
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (mLocalDatabaseHelper != null) {
			List<Site> sites = mLocalDatabaseHelper.getSites();
			for (Site site : sites) {
				if (mMarkers.get(site.getId()).equals(marker)) {
					OrderDialogFragment fragment = OrderDialogFragment.newInstance(site);
					fragment.show(mActivity.getFragmentManager(), "order_dialog_fragment");
					break;
				}
			}
		}
	}
}
