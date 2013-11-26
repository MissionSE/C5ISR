package com.missionse.mapsexample;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class DualMapsFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener, OnSharedPreferenceChangeListener {

	private static final int DEF_FILL_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float MSE_BEARING = 0f;
	private static final float NO_STROKE = 0f;
	private static final String PREF_CIRCLE_FILL_COLOR = "pref_circle_fill_color";
	private static final String PREF_CIRCLE_STROKE = "pref_circle_stroke";
	private static final String PREF_CIRCLE_STROKE_COLOR = "pref_circle_stroke_color";

	// These settings are the same as the settings for the map. They will in fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private static final String TAG = DualMapsFragment.class.getSimpleName();
	private static final String TAG_MINI_MAP = "mini_map";
	private static final LatLng ZONE_A = new LatLng(39.974074, -74.977462);
	private static final LatLng ZONE_B = new LatLng(39.975233, -74.977328);

	private static final LatLng ZONE_C = new LatLng(39.975085, -74.976164);

	public static DualMapsFragment newInstance(LatLng mockLocation) {
		DualMapsFragment fragment = new DualMapsFragment();
		fragment.mMockLatLng = mockLocation;
		return fragment;
	}

	private ArrayList<Circle> mCircles = new ArrayList<Circle>();

	private LocationClient mLocationClient;

	private GoogleMap mMainMap;

	private MiniMapFragment mMiniMapFragment;

	private LatLng mMockLatLng;

	private SharedPreferences mPrefs;

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// Do nothing.
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.dual_maps_example, menu);
		setMyLocationEnabled(menu.findItem(R.id.myLocation).isChecked());

		Switch miniMapSwitch = (Switch) menu.findItem(R.id.showMiniMap).getActionView().findViewById(R.id.mini_map_switch);
		miniMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mMiniMapFragment == null) {
					mMiniMapFragment = MiniMapFragment.newInstance(mMainMap);
					getFragmentManager().beginTransaction()
					.replace(R.id.bottom_left_container, mMiniMapFragment, TAG_MINI_MAP)
					.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
					.commit();
				} else {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					if (isChecked) {
						ft.show(mMiniMapFragment);
					} else {
						ft.hide(mMiniMapFragment);
					}
					ft.commit();
//					mMiniMapFragment.getView().setVisibility(isChecked == true ? View.VISIBLE : View.INVISIBLE);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_dual_maps, container, false);

		return v;
	}

	@Override
	public void onDisconnected() {
		// Do nothing.
	}

	@Override
	public void onLocationChanged(Location location) {
		// Do nothing.
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean enabled = !item.isChecked();
		switch (item.getItemId()) {
		case R.id.myLocation:
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

	@Override
	public void onPause() {
		super.onPause();
		//		mPrefs.unregisterOnSharedPreferenceChangeListener(this);
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//		mPrefs.registerOnSharedPreferenceChangeListener(this);
		setUpMapIfNeeded();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (PREF_CIRCLE_STROKE.equals(key) || PREF_CIRCLE_STROKE_COLOR.equals(key)) {
			int strokeColor =  mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, Color.BLACK);
			float strokeWidth = mPrefs.getBoolean(PREF_CIRCLE_STROKE, false) ? DEF_STROKE : NO_STROKE;
			for (Circle circle : mCircles) {
				circle.setStrokeColor(strokeColor);
				circle.setStrokeWidth(strokeWidth);
			}
		}
	}


	private void resetMaps() {
		if (mMockLatLng != null) {
			mMainMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(mMockLatLng, 18f, 0, MSE_BEARING)));
		}

	}

	private void setMyLocationEnabled(boolean enabled) {
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
		mMainMap.setMyLocationEnabled(enabled);
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMainMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMainMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map_right)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMainMap != null) {
				setUpRightMap();
			}
		}
	}

	private void setUpRightMap() {		
		mMainMap.setMapType(MAP_TYPE_HYBRID);

		double radius = 50;
		int fillColor = mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR);
		int strokeColor = mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, Color.BLACK);
		float strokeWidth = mPrefs.getBoolean(PREF_CIRCLE_STROKE, false) ? DEF_STROKE : NO_STROKE;

		mCircles.add(mMainMap.addCircle(new CircleOptions()
		.center(ZONE_A)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor)));

		mCircles.add(mMainMap.addCircle(new CircleOptions()
		.center(ZONE_B)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor)));

		mCircles.add(mMainMap.addCircle(new CircleOptions()
		.center(ZONE_C)
		.radius(radius)
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor)));
	}

}
