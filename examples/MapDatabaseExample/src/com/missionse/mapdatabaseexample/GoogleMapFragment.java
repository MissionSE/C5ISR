package com.missionse.mapdatabaseexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.mapdatabaseexample.model.MapLocation;
import com.missionse.mapdatabaseexample.tasks.GetAllLocationsTask;

/**
 * Provides a fragment that displays a map.
 */
public class GoogleMapFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener, OnMyLocationButtonClickListener, MapLocationAdder {

	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private boolean mFirstLocationChange = true;

	private static final LatLng MSE = new LatLng(39.974552, -74.976844);
	private static final float INITIAL_ZOOM = 17.5f;
	private static final float INITIAL_TILT = 0.0f;
	private static final float INITIAL_BEARING = 27.0f;

	private static final long UPDATE_INTERVAL = 5000;
	private static final long FASTEST_INTERVAL = 16;
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(UPDATE_INTERVAL)
			.setFastestInterval(FASTEST_INTERVAL)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onLocationChanged(final Location arg0) {
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
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
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
		new GetAllLocationsTask(getActivity(), this).execute();
	}

	@Override
	public void addLocation(final MapLocation location) {
		if (mMap != null) {
			mMap.addMarker(new MarkerOptions().position(location.getLatLng()));
		}
	}
}
