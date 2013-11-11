package com.missionse.mapsexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapsFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener, OnMyLocationButtonClickListener {
	
	private static final LatLng MSE = new LatLng(39.974552, -74.976844);
	private static final LatLng ZONE_A = new LatLng(39.974074, -74.977462);
	private static final LatLng ZONE_B = new LatLng(39.975233, -74.977328);
	private static final LatLng ZONE_C = new LatLng(39.975085, -74.976164);

	private GoogleMap mMap;
	
	private GroundOverlay mGroundOverlay;
	
	private int mCurrentLevel = 0;

	private final List<BitmapDescriptor> mLevelImages = new ArrayList<BitmapDescriptor>();

	private LocationClient mLocationClient;

	private boolean mFirstLocationChange = true;

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
		
		View v = inflater.inflate(R.layout.fragment_maps, container, false);
		
		return v;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if (mFirstLocationChange) {
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(MSE, 17.5f, 0, 27)));
			mFirstLocationChange = false;
		}
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.switchLevel:
			switchLevel();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void switchLevel() {
        mCurrentLevel = (mCurrentLevel + 1) % mLevelImages.size();
        mGroundOverlay.setImage(mLevelImages.get(mCurrentLevel));
    }
	
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}
	
	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getActivity(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}
	
	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setBuildingsEnabled(true);
		mMap.setMapType(MAP_TYPE_HYBRID);
		
		mLevelImages.clear();
        mLevelImages.add(BitmapDescriptorFactory.fromResource(R.drawable.floor1));
        mLevelImages.add(BitmapDescriptorFactory.fromResource(R.drawable.floor2));
		
        mCurrentLevel = 0;
        mGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(mLevelImages.get(mCurrentLevel))
                .position(MSE, 90)
                .bearing(27));

		double radius = 10;
		int colorFill = Color.argb(50, Color.red(Color.RED), Color.green(Color.RED), Color.blue(Color.RED));

		mMap.addCircle(new CircleOptions().center(ZONE_A).radius(radius).fillColor(colorFill));

		mMap.addCircle(new CircleOptions().center(ZONE_B).radius(radius).fillColor(colorFill));

		mMap.addCircle(new CircleOptions().center(ZONE_C).radius(radius).fillColor(colorFill));

	}
	
}
