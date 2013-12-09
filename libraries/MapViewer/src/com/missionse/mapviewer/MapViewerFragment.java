package com.missionse.mapviewer;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.apps.iosched.R;
import com.google.android.apps.iosched.ui.MapFragment.MarkerModel;
import com.google.android.apps.iosched.ui.MapFragment.MarkerQuery;
import com.google.android.apps.iosched.util.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Creates a full screen {@link GoogleMap} view with the ability to overlay
 * fragments in the four corners of the view.  Usually, {@link MiniMapFragment} 
 * is used in one of the corners to show a zoomed out view of the current view 
 * region displayed in this {@link GoogleMap} view.
 */
public class MapViewerFragment extends MapFragment implements
SharedPreferences.OnSharedPreferenceChangeListener,
LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
GoogleMap.OnCameraChangeListener, 
GoogleMap.OnInfoWindowClickListener {

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

	private MapView mMapOverview;

	private LocationClient mLocationClient;
	private GoogleMap mMainMap;
	private GoogleMap mMiniMap;
	private LatLng mInitialLatLng;
	private SharedPreferences mPrefs;
	private Set<GoogleMap.OnCameraChangeListener> mCameraListeners = new HashSet<GoogleMap.OnCameraChangeListener>();

	private ArrayList<Button> mFilterButtons = new ArrayList<Button>();
	private View mFilterControls;

	// Markers stored by id
	private HashMap<String, MarkerModel> mMarkers = null;
	// Markers stored by floor
	private ArrayList<ArrayList<Marker>> mMarkersFilter = null;

	private boolean mMarkersLoaded = false;

	// Cached size of view
	private int mWidth, mHeight;

	// Padding for #centerMap
	private int mShiftRight = 0;
	private int mShiftTop = 0;

	// Screen DPI
	private float mDPI = 0;

	public interface Callbacks {
		public void onLocationSelected(String locationId, String locationTitle);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onLocationSelected(String locationId, String locationTitle) {
			Log.d(TAG, "dummy callback.");
		}
	};

	private Callbacks mCallbacks = sDummyCallbacks;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		clearMap();

		// get DPI
		mDPI = getActivity().getResources().getDisplayMetrics().densityDpi / 160f;

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
		//TODO need to instantiate
		//		mMapOverview = findViewById(R.id.map_overview); 
		mMapOverview.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_map_viewer, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);

		layout.addView(mapView, 0);

		mFilterControls = layout.findViewById(R.id.map_filtercontrol);

		// setup filter button handlers
		mFilterButtons.add((Button) v.findViewById(R.id.map_filter1));
		mFilterButtons.add((Button) v.findViewById(R.id.map_filter2));
		mFilterButtons.add((Button) v.findViewById(R.id.map_filter3));

		for (int i = 0; i < mFilterButtons.size(); i++) {
			final int j = i;
			mFilterButtons.get(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showMarkers(j);
				}
			});
		}

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

						// also requires width and height
						enableMarkers();

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
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	private void showMarkers(int filter) {
		// show all markers
		for (Marker m : mMarkersFilter.get(filter)) {
			boolean visible = !m.isVisible();
			m.setVisible(visible);
		}
		// mark button active
		mFilterButtons.get(filter).setBackgroundResource(R.drawable.map_filter_button_active_background);
		mFilterButtons.get(filter).setTextColor(getResources().getColor(
				R.color.map_filterselect_active));
	}

	/**
	 * Enable filter controls and display map features when all loaders have
	 * finished. This ensures that only complete data for the correct filter is
	 * shown.
	 */
	private void enableMarkers() {
		if (mMarkersLoaded && mWidth > 0 && mHeight > 0) {
			mFilterControls.setVisibility(View.VISIBLE);

			showAllMarkers();
		}
	}

	private void showAllMarkers() {
		for (int i = 0; i < mMarkersFilter.size(); i++) {
			showMarkers(i);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		final String locationId = marker.getTitle();
		mCallbacks.onLocationSelected(locationId, mMarkers.get(locationId).label);
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

	// Loaders
    private void onMarkerLoaderComplete(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // get data
                String id = cursor.getString(MarkerQuery.MARKER_ID);
                int floor = cursor.getInt(MarkerQuery.MARKER_FLOOR);
                float lat = cursor.getFloat(MarkerQuery.MARKER_LATITUDE);
                float lon = cursor.getFloat(MarkerQuery.MARKER_LONGITUDE);
                String type = cursor.getString(MarkerQuery.MARKER_TYPE);
                String label = cursor.getString(MarkerQuery.MARKER_LABEL);
                String track = cursor.getString(MarkerQuery.MARKER_TRACK);

                BitmapDescriptor icon = null;
                if (TYPE_SESSION.equals(type)) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_session);
                } else if (TYPE_SANDBOX.equals(type)) {
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_sandbox);
                } else if (TYPE_LABEL.equals(type)) {
                    Bitmap b = MapUtils.createTextLabel(label, mDPI);
                    if (b != null) {
                        icon = BitmapDescriptorFactory.fromBitmap(b);
                    }
                }

                // add marker to map
                if (icon != null) {
                    Marker m = mMainMap.addMarker(
                            new MarkerOptions().position(new LatLng(lat, lon)).title(id)
                                    .snippet(type).icon(icon)
                                    .visible(false));

                    MarkerModel model = new MarkerModel(id, floor, type, label, track, m);

                    mMarkersFloor.get(floor).add(m);
                    mMarkers.put(id, model);
                }

                cursor.moveToNext();
            }
            // no more markers to load
            mMarkersLoaded = true;
            enableFloors();
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

	/**
	 * Clears the map and initialises all map variables that hold markers and overlays.
	 */
	private void clearMap() {
		if (mMainMap != null) {
			mMainMap.clear();
		}
		if (mMiniMap != null) {
			mMiniMap.clear();
		}

		mMarkers = new HashMap<String, MarkerModel>();
		mMarkersFilter = new ArrayList<ArrayList<Marker>>();
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
			for (GoogleMap.OnCameraChangeListener listener : mCameraListeners) {
				listener.onCameraChange(arg0);
			}
		}
	}

	/**
	 * A structure to store information about a Marker.
	 */
	public static class MarkerModel {
		String id;
		String type;
		String label;
		Marker marker;

		public MarkerModel(String id, String type, String label, Marker marker) {
			this.id = id;
			this.type = type;
			this.label = label;
			this.marker = marker;
		}
	}

}
