package com.missionse.mapsexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class DualMapsFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener {
	private static final LatLng MSE = new LatLng(39.974552, -74.976844);
	private static final LatLng ZONE_A = new LatLng(39.974074, -74.977462);
	private static final LatLng ZONE_B = new LatLng(39.975233, -74.977328);
	private static final LatLng ZONE_C = new LatLng(39.975085, -74.976164);
	private static final float MSE_BEARING = 27f;

	private GoogleMap mMapLeft;
	private GoogleMap mMapRight;

	private Polygon mZoomedViewPolygon;

	private LocationClient mLocationClient;

	// These settings are the same as the settings for the map. They will in fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_dual_maps, container, false);

		return v;
	}

	@Override
	public void onLocationChanged(Location location) {
		// Do nothing.
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// Do nothing.
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// Do nothing.
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.myLocation:
			boolean enabled = !item.isChecked();
			item.setChecked(enabled);
			setMyLocationEnabled(enabled);
			return true;
		case R.id.resetMaps:
			resetMaps();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void resetMaps() {
		mMapLeft.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(MSE, 15f, 0, MSE_BEARING)));
		mMapRight.animateCamera(CameraUpdateFactory
				.newCameraPosition(new CameraPosition(MSE, 18f, 0, MSE_BEARING)));
	}

	private void setMyLocationEnabled(boolean enabled) {
		Log.d("Maps Fragment", "setMyLocationEnabled: enabled=" + enabled);
		if (enabled) {
			if (mLocationClient == null) {
				mLocationClient = new LocationClient(getActivity(), this, // ConnectionCallbacks
						this); // OnConnectionFailedListener
			}
			mLocationClient.connect();
		} else {
			if (mLocationClient != null) {
				mLocationClient.disconnect();
			}
		}
		mMapLeft.setMyLocationEnabled(enabled);
		mMapRight.setMyLocationEnabled(enabled);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.dual_maps_example, menu);
		setMyLocationEnabled(menu.findItem(R.id.myLocation).isChecked());
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMapLeft == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMapLeft = ((MapFragment)getFragmentManager().findFragmentById(R.id.map_left)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMapLeft != null) {
				setUpLeftMap();
			}
		}
		if (mMapRight == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMapRight = ((MapFragment)getFragmentManager().findFragmentById(R.id.map_right)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMapRight != null) {
				setUpRightMap();
			}
		}
	}

	private void setUpLeftMap() {
		mMapLeft.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				mMapLeft.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(MSE, 15f, 0, MSE_BEARING)));
			}
		});
		
		mMapLeft.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				mMapRight.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				drawZoomedViewPolygon();
			}
		});

		mMapLeft.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				drawZoomedViewPolygon();
			}
		});

		mMapLeft.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				mMapRight.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
				drawZoomedViewPolygon();
				return false;
			}
		});

		mMapLeft.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		mMapLeft.addMarker(new MarkerOptions()
		.position(ZONE_A));

		mMapLeft.addMarker(new MarkerOptions()
		.position(ZONE_B));

		mMapLeft.addMarker(new MarkerOptions()
		.position(ZONE_C));
	}

	private void setUpRightMap() {
		mMapRight.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				mMapRight.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(MSE, 18f, 0, MSE_BEARING)));
			}
		});

		mMapRight.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition posRight) {
				if (mMapLeft.getCameraPosition().zoom > posRight.zoom) {
					mMapLeft.animateCamera(CameraUpdateFactory.zoomTo(posRight.zoom - 3));
				}
				drawZoomedViewPolygon();
			}
		});

		mMapRight.setMapType(MAP_TYPE_HYBRID);

		double radius = 50;
		int fillColor = Color.argb(75, 255, 0, 0);
		int strokeColor = Color.RED;
		float strokeWidth = 2f;

		mMapRight.addCircle(new CircleOptions()
		.center(ZONE_A)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));

		mMapRight.addCircle(new CircleOptions()
		.center(ZONE_B)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));

		mMapRight.addCircle(new CircleOptions()
		.center(ZONE_C)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));
	}

	private void drawZoomedViewPolygon() {
		int fillColor = Color.argb(150, 0, 0, 0);
		float strokeWidth = 0f;
		int strokeColor = Color.GRAY;

		final VisibleRegion rightVR = mMapRight.getProjection().getVisibleRegion();
		final List<LatLng> rightVRpoints = new ArrayList<LatLng>();
		rightVRpoints.add(rightVR.farLeft);
		rightVRpoints.add(rightVR.farRight);
		rightVRpoints.add(rightVR.nearRight);
		rightVRpoints.add(rightVR.nearLeft);

		if (mZoomedViewPolygon == null) {
			mZoomedViewPolygon = mMapLeft.addPolygon(new PolygonOptions()
			.addAll(rightVRpoints)
			.fillColor(fillColor)
			.strokeWidth(strokeWidth)
			.strokeColor(strokeColor));
		} else {
			mZoomedViewPolygon.setFillColor(fillColor);
			mZoomedViewPolygon.setStrokeWidth(strokeWidth);
			mZoomedViewPolygon.setPoints(rightVRpoints);
			mZoomedViewPolygon.setStrokeColor(strokeColor);
		}
	}

}
