package com.missionse.mapdatabaseexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.mapdatabaseexample.model.MapLocation;
import com.missionse.mapdatabaseexample.tasks.DeleteLocationTask;
import com.missionse.mapdatabaseexample.tasks.EditLocationTask;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Provides a fragment that displays a map.
 */
public class GoogleMapFragment extends Fragment implements
		ActionMode.Callback,
		GoogleMap.OnInfoWindowClickListener,
		GoogleMap.OnMapLongClickListener,
		GoogleMap.OnMapClickListener,
		GoogleMap.OnMarkerClickListener,
		GoogleMap.OnMarkerDragListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		LocationListener {

	private static final String TAG = GoogleMapFragment.class.getName();

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
	private boolean mFirstLocationChange = true;
	private final MapLocationManager mLocationManager = new MapLocationManager();

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
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, null);

		MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (null == mapFragment) {
			mapFragment = MapFragment.newInstance();
			getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
		}

		return view;
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
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((MapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getActivity(), this, this);
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setBuildingsEnabled(true);
		mMap.setMapType(MAP_TYPE_HYBRID);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);
		mMap.setOnMapClickListener(this);
		mMap.setOnMarkerClickListener(this);

		if (mActivity != null) {
			new GetAllLocationsTask((Context) mActivity, (MapLocationListener) mActivity).execute();
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
	public void onMapLongClick(final LatLng location) {
		CreateLocationDialogFragment.newInstance(location.latitude, location.longitude)
				.show(getFragmentManager(), "create_location");
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
							.show(getFragmentManager(), "edit_location");
		}

		if (mActionMode != null) {
			mActionMode.finish();
		}
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
	public void onMarkerDragStart(final Marker marker) {

	}

	@Override
	public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.action_edit:
				if (mSelectedMarker != null) {
					MapLocation location = mLocationManager.getLocation(mSelectedMarker);
					if (location != null) {
						EditLocationDialogFragment.newInstance(
								location.getId(),
								location.getName(),
								location.getLatitude(),
								location.getLongitude())
										.show(getFragmentManager(), "edit_location");
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
				Log.d(TAG, "Unknown action mode menu item: " + menuItem.toString());
		}

		actionMode.finish();
		return true;
	}

	@Override
	public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
		mActionMode = actionMode;
		mActionMode.getMenuInflater().inflate(R.menu.actionbar_edit_marker, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(final ActionMode actionMode) {
		mSelectedMarker.hideInfoWindow();
		mSelectedMarker = null;
	}

	@Override
	public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
		return false;
	}

	@Override
	public void onMapClick(final LatLng location) {
		if (mActionMode != null) {
			mActionMode.finish();
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
}
