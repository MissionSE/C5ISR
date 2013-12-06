package com.missionse.mapdatabaseexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.mapdatabaseexample.model.MapLocation;
import com.missionse.mapdatabaseexample.tasks.EditLocationTask;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Provides a fragment that displays a map.
 */
@SuppressLint("UseSparseArrays")
public class GoogleMapFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener, OnMyLocationButtonClickListener, OnMapLongClickListener, OnInfoWindowClickListener, OnMarkerDragListener {

	private static final String TAG = GoogleMapFragment.class.getName();

	private static final LatLng MSE = new LatLng(39.974552, -74.976844);
	private static final float INITIAL_ZOOM = 17.5f;
	private static final float INITIAL_TILT = 0.0f;
	private static final float INITIAL_BEARING = 27.0f;

	private static final long UPDATE_INTERVAL = 5000;
	private static final long FASTEST_INTERVAL = 16;
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(UPDATE_INTERVAL)
			.setFastestInterval(FASTEST_INTERVAL)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private Activity mActivity;
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private boolean mFirstLocationChange = true;
	private final Map<Integer, MapLocation> mLocations = new HashMap<Integer, MapLocation>();
	private final Map<Integer, Marker> mMarkers = new HashMap<Integer, Marker>();

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
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onLocationChanged(final Location location) {
		if (mFirstLocationChange) {
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(MSE, INITIAL_ZOOM, INITIAL_TILT, INITIAL_BEARING)));
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
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setBuildingsEnabled(true);
		mMap.setMapType(MAP_TYPE_HYBRID);
		mMap.setOnMapLongClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

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
			if (!mLocations.containsKey(locationId)) {
				Log.d(TAG, "Marker added: " + location);
				mMarkers.put(locationId, mMap.addMarker(
					new MarkerOptions()
						.position(location.getLatLng())
						.title(location.getName())
						.draggable(true)));
			} else {
				Log.d(TAG, "Marker updated: " + location);
				Marker marker = mMarkers.get(locationId);
				marker.setPosition(location.getLatLng());
				marker.setTitle(location.getName());
			}

			mLocations.put(locationId, location);
		}
	}

	@Override
	public void onMapLongClick(final LatLng location) {
		CreateLocationDialogFragment.newInstance(location.latitude, location.longitude)
			.show(getFragmentManager(), "create_location");
	}

	@Override
	public void onInfoWindowClick(final Marker marker) {
		MapLocation location = null;
		for (int locationId : mMarkers.keySet()) {
			if (mMarkers.get(locationId).equals(marker)) {
				location = mLocations.get(locationId);
				break;
			}
		}

		if (location != null) {
			EditLocationDialogFragment.newInstance(
				location.getId(),
				location.getName(),
				location.getLatitude(),
				location.getLongitude())
					.show(getFragmentManager(), "edit_location");
		}
	}

	@Override
	public void onMarkerDrag(final Marker marker) {

	}

	@Override
	public void onMarkerDragEnd(final Marker marker) {
		MapLocation location = null;
		for (int locationId : mMarkers.keySet()) {
			if (mMarkers.get(locationId).equals(marker)) {
				location = mLocations.get(locationId);
				break;
			}
		}

		if (location != null) {
			if (mActivity != null) {
				new EditLocationTask((Context) mActivity, (MapLocationListener) mActivity).execute(
						Integer.toString(location.getId()),
						location.getName(),
						Double.toString(marker.getPosition().latitude),
						Double.toString(marker.getPosition().longitude));
			}
		}
	}

	@Override
	public void onMarkerDragStart(final Marker marker) {

	}
}
