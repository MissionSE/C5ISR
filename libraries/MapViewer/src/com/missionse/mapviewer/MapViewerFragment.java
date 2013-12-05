package com.missionse.mapviewer;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.HashSet;
import java.util.Set;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Creates a full screen {@link GoogleMap} view with the ability to overlay
 * fragments in the four corners of the view.  Usually, {@link MiniMapFragment} 
 * is used in one of the corners to show a zoomed out view of the current view 
 * region displayed in this {@link GoogleMap} view.
 */
public class MapViewerFragment extends Fragment implements
OnSharedPreferenceChangeListener,
LocationListener,
ConnectionCallbacks,
OnConnectionFailedListener,
OnCameraChangeListener {
	public static final String FRAGMENT_TAG = "map_viewer_fragment";

	private static final String TAG = MapViewerFragment.class.getSimpleName();
	
	private static final int DEF_FILL_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float NO_STROKE = 0f;
	private static final String PREF_CIRCLE_FILL_COLOR = "pref_circle_fill_color";
	private static final String PREF_CIRCLE_STROKE = "pref_circle_stroke";
	private static final String PREF_CIRCLE_STROKE_COLOR = "pref_circle_stroke_color";
	private static final long DEF_LOCATION_REQUEST_INTERVAL = 5000; // 5 seconds
	private static final long DEF_FASTEST_REQUEST_INTERVAL = 16; // 16ms = 60fps


	/*
	 * These settings are the same as the settings for the map. They will in
	 * fact give you updates at the maximal rates currently possible.
	 */
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(DEF_LOCATION_REQUEST_INTERVAL)
			.setFastestInterval(DEF_FASTEST_REQUEST_INTERVAL) 
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	/**
	 * Creates a new instance of the {@link MapViewerFragment} and sets an initial
	 * location for the map to position the camera at.
	 * @param initialLatLng initial location to center map camera
	 * @return a new instance of the {@link MapViewerFragment}
	 */
	public static MapViewerFragment newInstance(LatLng initialLatLng) {
		MapViewerFragment fragment = new MapViewerFragment();
		fragment.mInitialLatLng = initialLatLng;
		return fragment;
	}

	private LocationClient mLocationClient;
	private GoogleMap mMainMap;
	private LatLng mInitialLatLng;
	private SharedPreferences mPrefs;
	private Set<OnCameraChangeListener> mCameraListeners = new HashSet<OnCameraChangeListener>();

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_map_viewer, container, false);

		return v;
	}

	public void replaceTopLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	public void replaceTopRightCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.top_right_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

	public void replaceBottomLeftCornerFragment(Fragment fragment, String tag) {
		replaceCornerFragment(R.id.bottom_left_container, fragment, tag, android.R.animator.fade_in, android.R.animator.fade_out);
	}

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
	
	public void registerOnCameraChangeListener(final OnCameraChangeListener listener) {
		mCameraListeners.add(listener);
		
		Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				listener.onCameraChange(mMainMap.getCameraPosition());
			}
		});
		
	}
	
	public void deregisterOnCameraChangeListener(OnCameraChangeListener listener) {
		mCameraListeners.remove(listener);
	}

	/**
	 * Gets the {@link GoogleMap} main map from this fragment.
	 * @return the main map
	 */
	public GoogleMap getMainMap() {
		return mMainMap;
	}

	/**
	 * Animates the camera position to the initial location.
	 */
	public void resetLatLng() {
		mMainMap.animateCamera(CameraUpdateFactory.newLatLng(mInitialLatLng));
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
			mMainMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.main_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMainMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {		
		mMainMap.setMapType(MAP_TYPE_HYBRID);
		mMainMap.setOnCameraChangeListener(this);

		int fillColor = mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR);
		int strokeColor = mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, Color.BLACK);
		float strokeWidth;
		if (mPrefs.getBoolean(PREF_CIRCLE_STROKE, false)) {
			strokeWidth = DEF_STROKE;
		} else {
			strokeWidth = NO_STROKE;
		}
	}

	/**
	 * Enables use of google's {@link LocationClient} service which provides user's
	 * location based on GPS, WIFI, and/or mobile networks.
	 * @param enabled whether to enable or disable the service
	 */
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

	@Override
	public void onCameraChange(CameraPosition arg0) {
		synchronized (mCameraListeners) {
			for (OnCameraChangeListener listener : mCameraListeners) {
				listener.onCameraChange(arg0);
			}
		}
	}

}
