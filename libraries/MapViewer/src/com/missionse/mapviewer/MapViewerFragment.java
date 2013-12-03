package com.missionse.mapviewer;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapViewerFragment extends Fragment implements OnSharedPreferenceChangeListener, LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

	private static final String TAG = MapViewerFragment.class.getSimpleName();

	private static final int DEF_FILL_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float NO_STROKE = 0f;
	private static final String PREF_CIRCLE_FILL_COLOR = "pref_circle_fill_color";
	private static final String PREF_CIRCLE_STROKE = "pref_circle_stroke";
	private static final String PREF_CIRCLE_STROKE_COLOR = "pref_circle_stroke_color";

	// These settings are the same as the settings for the map. They will in fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public static MapViewerFragment newInstance(LatLng mockLocation) {
		MapViewerFragment fragment = new MapViewerFragment();
		fragment.mMockLatLng = mockLocation;
		return fragment;
	}

	private LocationClient mLocationClient;
	private GoogleMap mMainMap;
	private LatLng mMockLatLng;
	private SharedPreferences mPrefs;

	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_map_viewer, container, false);

		return v;
	}

	/**
	 * @param fragment
	 * @param tag
	 */
	public void replaceTopLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	/**
	 * @param fragment
	 * @param tag
	 */
	public void replaceTopRightCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_right_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	/**
	 * @param fragment
	 * @param tag
	 */
	public void replaceBottomLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.bottom_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	/**
	 * @param fragment
	 * @param tag
	 */
	public void replaceBottomRightCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.bottom_right_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	private void replaceCornerFragment(int containerViewId, Fragment fragment, String tag, int enter, int exit) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(containerViewId, fragment, tag);
		ft.commit();
	}

	public void removeCornerFragment(Fragment fragment, int enter, int exit) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commit();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

	public GoogleMap getMainMap() {
		return mMainMap;
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

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMainMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMainMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.main_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMainMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {		
		mMainMap.setMapType(MAP_TYPE_HYBRID);

		int fillColor = mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR);
		int strokeColor = mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, Color.BLACK);
		float strokeWidth = mPrefs.getBoolean(PREF_CIRCLE_STROKE, false) ? DEF_STROKE : NO_STROKE;
	}

	public void setMyLocationEnabled(boolean enabled) {
		if (enabled) {
			if (mLocationClient == null) {
				mLocationClient = new LocationClient(getActivity(), this, this); 
			}
			mLocationClient.connect();
		} else {
			if (mLocationClient != null) {
				mLocationClient.disconnect();
			}
		}
		mMainMap.setMyLocationEnabled(enabled);
	}

	@Override
	public void onDisconnected() {
		// Do nothing.
	}

}
