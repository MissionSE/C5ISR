package com.missionse.mapviewer;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Creates a full screen {@link GoogleMap} view with the ability to overlay
 * fragments in the four corners of the view.  Usually, {@link MiniMapFragment} 
 * is used in one of the corners to show a zoomed out view of the current view 
 * region displayed in this {@link GoogleMap} view.
 */
public class MapViewerFragment extends MapFragment implements
LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
GoogleMap.OnCameraChangeListener, 
GoogleMap.OnInfoWindowClickListener {
	
	public interface Callbacks {
		void mapCreated(GoogleMap map);
	}
	
	private Callbacks mCallbacks;

	private static final String TAG = MapViewerFragment.class.getSimpleName();

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
	private Set<GoogleMap.OnCameraChangeListener> mCameraListeners = new HashSet<GoogleMap.OnCameraChangeListener>();
	private Set<GoogleMap.OnInfoWindowClickListener> mInfoWindowClickListeners = new HashSet<GoogleMap.OnInfoWindowClickListener>();

	// Cached size of view
	private int mWidth, mHeight;

	// Padding for #centerMap
	private int mShiftRight = 0;
	private int mShiftTop = 0;

	// Screen DPI
	private float mDPI = 0;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);

		// get DPI
		mDPI = getActivity().getResources().getDisplayMetrics().densityDpi / 160f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_map_viewer, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);

		layout.addView(mapView, 0);

		// get the height and width of the view
		mapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						final View v = getView();
						mHeight = v.getHeight();
						mWidth = v.getWidth();

						if (v.getViewTreeObserver().isAlive()) {
							// remove this layout listener
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
							} else {
								v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
							}
						}
					}
				});

		setUpMapIfNeeded();

		return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new ClassCastException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		synchronized (mInfoWindowClickListeners) {
			for (GoogleMap.OnInfoWindowClickListener listener : mInfoWindowClickListeners) {
				listener.onInfoWindowClick(marker);
			}
		}
	}

	private void centerMap(LatLng position) {
		// calculate the new center of the map, taking into account optional
		// padding
		Projection proj = mMainMap.getProjection();
		Point p = proj.toScreenLocation(position);

		// apply padding
		p.x = (int) (p.x - Math.round(mWidth * 0.5)) + mShiftRight;
		p.y = (int) (p.y - Math.round(mHeight * 0.5)) + mShiftTop;

		mMainMap.animateCamera(CameraUpdateFactory.scrollBy(p.x, p.y));
	}

	/**
	 * Set the padding around centered markers. Specified in the percentage of
	 * the screen space of the map.
	 */
	public void setCenterPadding(float xFraction, float yFraction) {
		int oldShiftRight = mShiftRight;
		int oldShiftTop = mShiftTop;

		mShiftRight = Math.round(xFraction * mWidth);
		mShiftTop = Math.round(yFraction * mWidth);

		// re-center the map, shift displayed map by x and y fraction if map is
		// ready
		if (mMainMap != null) {
			mMainMap.animateCamera(CameraUpdateFactory.scrollBy(mShiftRight - oldShiftRight, mShiftTop
					- oldShiftTop));
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// Do nothing.
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Registers a listener for to receive camera change callbacks.
	 * @param listener the listener
	 */
	public void registerOnCameraChangeListener(final GoogleMap.OnCameraChangeListener listener) {
		mCameraListeners.add(listener);

		Handler handler = new Handler();
		handler.post(new Runnable() {

			@Override
			public void run() {
				listener.onCameraChange(mMainMap.getCameraPosition());
			}
		});

	}

	/**
	 * Deregisters a listener to no longer receive camera change callbacks.
	 * @param listener the listener
	 */
	public void deregisterOnCameraChangeListener(final GoogleMap.OnCameraChangeListener listener) {
		mCameraListeners.remove(listener);
	}
	
	/**
	 * Registers a listener for to receive info window click callbacks.
	 * @param listener the listener
	 */
	public void registeOnInfoWindowClickListener(final GoogleMap.OnInfoWindowClickListener listener) {
		synchronized (mInfoWindowClickListeners) {
			mInfoWindowClickListeners.add(listener);
		}
	}

	/**
	 * Deregisters a listener to no longer receive info window click callbacks.
	 * @param listener the listener
	 */
	public void deregisterInfoWindowClickListener(final GoogleMap.OnInfoWindowClickListener listener) {
		synchronized (mInfoWindowClickListeners) {
			mInfoWindowClickListeners.remove(listener);
		}
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
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMainMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMainMap = getMap();
			// Check if we were successful in obtaining the map.
			if (mMainMap != null) {
				setUpMap();
			}
			mCallbacks.mapCreated(mMainMap);
		}
	}

	private void setUpMap() {		
		mMainMap.setMapType(MAP_TYPE_HYBRID);
		mMainMap.setOnCameraChangeListener(this);
		mMainMap.setOnInfoWindowClickListener(this);

		UiSettings settings = mMainMap.getUiSettings();
		settings.setZoomControlsEnabled(false);
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
	public void onCameraChange(CameraPosition cameraPosition) {
		synchronized (mCameraListeners) {
			for (GoogleMap.OnCameraChangeListener listener : mCameraListeners) {
				listener.onCameraChange(cameraPosition);
			}
		}
	}
}
