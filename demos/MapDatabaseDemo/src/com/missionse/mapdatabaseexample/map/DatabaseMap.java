package com.missionse.mapdatabaseexample.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.mapdatabaseexample.R;
import com.missionse.mapdatabaseexample.dialog.CreateLocationDialogFragment;
import com.missionse.mapdatabaseexample.dialog.EditLocationDialogFragment;
import com.missionse.mapdatabaseexample.tasks.DeleteLocationTask;
import com.missionse.mapdatabaseexample.tasks.EditLocationTask;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Displays logistics information on the google map.
 */
public class DatabaseMap implements
		ActionMode.Callback,
		LocationListener,
		MapLoadedListener,
		GoogleMap.OnInfoWindowClickListener,
		GoogleMap.OnMapClickListener,
		GoogleMap.OnMapLongClickListener,
		GoogleMap.OnMarkerClickListener,
		GoogleMap.OnMarkerDragListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final String TAG = DatabaseMap.class.getName();
	private static final float INITIAL_ZOOM = 17.5f;
	private static final long UPDATE_INTERVAL = 5000;
	private static final long FASTEST_INTERVAL = 16;
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(UPDATE_INTERVAL)
			.setFastestInterval(FASTEST_INTERVAL)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private Activity mActivity;
	private GoogleMap mMap;
	private Marker mSelectedMarker;
	private ActionMode mActionMode;

	private LocationClient mLocationClient;
	private boolean mFirstLocationChange;

	private MapLocationManager mLocationManager;

	/**
	 * Constructor.
	 * @param activity The activity that holds the map.
	 */
	public DatabaseMap(final Activity activity) {
		mActivity = activity;

		mLocationManager = new MapLocationManager();
		mFirstLocationChange = true;
	}

	@Override
	public void mapLoaded(final GoogleMap map) {
		mMap = map;

		mMap.setBuildingsEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnMarkerDragListener(this);

		if (mActivity != null) {
			if (mLocationClient == null) {
				mLocationClient = new LocationClient(mActivity, this, this);
				mLocationClient.connect();
			}

			new GetAllLocationsTask(mActivity, (MapLocationListener) mActivity).execute();
		}
	}

	@Override
	public void onConnectionFailed(final ConnectionResult result) {
	}

	@Override
	public void onConnected(final Bundle bundle) {
		mLocationClient.requestLocationUpdates(REQUEST, this);
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onLocationChanged(final Location location) {
		if (mFirstLocationChange) {
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
					new LatLng(location.getLatitude(), location.getLongitude()),
					INITIAL_ZOOM)));
			mFirstLocationChange = false;
		}
	}

	/**
	 * Creates or updates a marker at the specified location.
	 * @param location The location for the marker to be placed.
	 */
	public void addMarker(final MapLocation location) {
		if (mMap != null) {
			int locationId = location.getId();
			if (!mLocationManager.containsLocation(locationId)) {
				Log.d(TAG, "Marker added: " + location);
				mLocationManager.put(locationId, mMap.addMarker(
						new MarkerOptions()
								.position(location.getLatLng())
								.title(location.getName())
								.draggable(true)));
			} else {
				Log.d(TAG, "Marker updated: " + location);
				Marker marker = mLocationManager.getMarker(locationId);
				marker.setPosition(location.getLatLng());
				marker.setTitle(location.getName());
			}

			mLocationManager.put(locationId, location);
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (mActionMode != null) {
			mActionMode.finish();
		}

		mSelectedMarker = marker;
		if (mActivity != null) {
			mActivity.startActionMode(this);
		}

		return false;
	}

	@Override
	public void onMarkerDragStart(final Marker marker) {
	}

	@Override
	public void onMarkerDrag(final Marker marker) {
	}

	@Override
	public void onMarkerDragEnd(final Marker marker) {
		MapLocation location = mLocationManager.getLocation(marker);
		if (location != null && mActivity != null) {
				new EditLocationTask((Context) mActivity, (MapLocationListener) mActivity).execute(
						Integer.toString(location.getId()),
						location.getName(),
						Double.toString(marker.getPosition().latitude),
						Double.toString(marker.getPosition().longitude));
		}
	}


	@Override
	public void onInfoWindowClick(final Marker marker) {
		MapLocation location = mLocationManager.getLocation(marker);
		if (location != null) {
			EditLocationDialogFragment.newInstance(
					location.getId(),
					location.getName(),
					location.getLatitude(),
					location.getLongitude())
							.show(mActivity.getFragmentManager(), "edit_location");
		}

		if (mActionMode != null) {
			mActionMode.finish();
		}
	}

	@Override
	public void onMapClick(final LatLng location) {
		if (mActionMode != null) {
			mActionMode.finish();
		}
	}

	@Override
	public void onMapLongClick(final LatLng location) {
		CreateLocationDialogFragment.newInstance(location.latitude, location.longitude)
				.show(mActivity.getFragmentManager(), "create_location");
	}

	@Override
	public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
		return false;
	}

	@Override
	public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
		mActionMode = mode;
		mActionMode.getMenuInflater().inflate(R.menu.actionbar_edit_marker, menu);
		return true;
	}

	@Override
	public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_edit:
				if (mSelectedMarker != null) {
					MapLocation location = mLocationManager.getLocation(mSelectedMarker);
					if (location != null) {
						EditLocationDialogFragment.newInstance(
								location.getId(),
								location.getName(),
								location.getLatitude(),
								location.getLongitude())
								.show(mActivity.getFragmentManager(), "edit_location");
					}
				}
				break;
			case R.id.action_delete:
				if (mSelectedMarker != null) {
					MapLocation location = mLocationManager.getLocation(mSelectedMarker);
					if (location != null) {
						new DeleteLocationTask((Context) mActivity, (MapLocationListener) mActivity).execute(
								Integer.toString(location.getId()));
						mSelectedMarker.remove();
						mLocationManager.remove(location.getId());
						mLocationManager.remove(location.getId());
					}
				}
				break;
			default:
				Log.d(TAG, "Unknown action mode menu item: " + item.toString());
		}

		mode.finish();
		return true;
	}

	@Override
	public void onDestroyActionMode(final ActionMode mode) {
		mSelectedMarker.hideInfoWindow();
		mSelectedMarker = null;
	}
}
