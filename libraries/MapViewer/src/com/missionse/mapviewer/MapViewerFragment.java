package com.missionse.mapviewer;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;

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

	private static final String PREF_VIEW_AREA_FILL_COLOR = "pref_view_area_fill_color";
	private static final String PREF_VIEW_AREA_STROKE = "pref_view_area_stroke";
	private static final String PREF_VIEW_AREA_STROKE_COLOR = "pref_view_area_stroke_color";
	private static final int DEF_VIEW_AREA_COLOR = Color.argb(150, 0, 0, 0);
	private static final int DEF_SCREEN_RATIO = 3;
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
	private Polygon mOverviewPolygon;
	private LocationClient mLocationClient;
	private GoogleMap mMainMap;
	private GoogleMap mMiniMap;
	private LatLng mInitialLatLng;
	private SharedPreferences mPrefs;
	private Set<GoogleMap.OnCameraChangeListener> mCameraListeners = new HashSet<GoogleMap.OnCameraChangeListener>();

	private SparseArray<Button> mFilterButtons = new SparseArray<Button>();
	private LinearLayout mFilterControls;

	// Markers stored by id
	private HashMap<String, MarkerModel> mMarkers = null;
	// Markers stored by floor
	private SparseArray<ArrayList<Marker>> mMarkersFilter = null;

	private SparseBooleanArray mFilterLoaded = new SparseBooleanArray();
	private boolean mMarkersLoaded = false;

	// Cached size of view
	private int mWidth, mHeight;

	// Padding for #centerMap
	private int mShiftRight = 0;
	private int mShiftTop = 0;

	// Screen DPI
	private float mDPI = 0;

	public interface Callbacks {
		void onLocationSelected(String locationId, String locationTitle);
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
		setRetainInstance(true);
		clearMap();

		// get DPI
		mDPI = getActivity().getResources().getDisplayMetrics().densityDpi / 160f;

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_map_viewer, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);
		
		Log.d(TAG, "layout.w=" + layout.getWidth());
		Log.d(TAG, "layout.h=" + layout.getHeight());

		layout.addView(mapView, 0);

		mMapOverview = (MapView) v.findViewById(R.id.map_overview);
		mMapOverview.onCreate(savedInstanceState);

		mFilterControls = (LinearLayout) layout.findViewById(R.id.map_filtercontrols);

		// setup filter button handlers
		mFilterButtons.put(StructuredPostal.TYPE_HOME, (Button) v.findViewById(R.id.map_filter1));
		mFilterButtons.put(StructuredPostal.TYPE_WORK, (Button) v.findViewById(R.id.map_filter2));
		mFilterButtons.put(StructuredPostal.TYPE_OTHER, (Button) v.findViewById(R.id.map_filter3));

		Button button = mFilterButtons.get(StructuredPostal.TYPE_HOME);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_HOME);
			}
		});

		button = mFilterButtons.get(StructuredPostal.TYPE_WORK);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_WORK);
			}
		});

		button = mFilterButtons.get(StructuredPostal.TYPE_OTHER);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showMarkers(StructuredPostal.TYPE_OTHER);
			}
		});

		// get the height and width of the view
		mapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						Log.d(TAG, "In onGlobalLayout...");
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
		boolean visible = false;
		for (Marker m : mMarkersFilter.get(filter)) {
			visible = !m.isVisible();
			m.setVisible(visible);
		}
		// mark button active
		int backgroundResource;
		int textColorResource;
		if (visible) {
			backgroundResource = R.drawable.map_filter_button_active_background;
			textColorResource = R.color.map_filterselect_active;
		} else {
			backgroundResource = R.drawable.map_filter_button_background;
			textColorResource = R.color.map_filterselect_inactive;
		}
		mFilterButtons.get(filter).setBackgroundResource(backgroundResource);
		mFilterButtons.get(filter).setTextColor(getResources().getColor(textColorResource));
	}

	/**
	 * Enable filter controls and display map features when all loaders have
	 * finished. This ensures that only complete data for the correct filter is
	 * shown.
	 */
	private void enableMarkers() {
		for (int i = 0; i < mFilterLoaded.size(); i++) {
			Boolean value = mFilterLoaded.valueAt(i);
			if (value == null || value.booleanValue() == false) {
				return;
			}
		}
		
		if (mWidth > 0 && mHeight > 0) {
			mFilterControls.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		final String locationId = marker.getTitle();
		//		mCallbacks.onLocationSelected(locationId, mMarkers.get(locationId).mName);
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
		mMarkersFilter = new SparseArray<ArrayList<Marker>>();

		mMarkersFilter.put(StructuredPostal.TYPE_HOME, new ArrayList<Marker>());
		mMarkersFilter.put(StructuredPostal.TYPE_WORK, new ArrayList<Marker>());
		mMarkersFilter.put(StructuredPostal.TYPE_OTHER, new ArrayList<Marker>());
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
		mMapOverview.onPause();
		//		mPrefs.unregisterOnSharedPreferenceChangeListener(this);
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapOverview.onResume();
		//		mPrefs.registerOnSharedPreferenceChangeListener(this);
		setUpMapIfNeeded();
	}

	@Override
	public void onDestroy() {
		mMapOverview.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapOverview.onLowMemory();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapOverview.onSaveInstanceState(outState);
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
		}
		if (mMiniMap == null) {
			mMiniMap = mMapOverview.getMap();
			if (mMiniMap != null) {
				setUpOverviewMap();
			}
		}
	}

	private void setUpMap() {		
		mMainMap.setMapType(MAP_TYPE_HYBRID);
		mMainMap.setOnCameraChangeListener(this);

		UiSettings settings = mMainMap.getUiSettings();
		settings.setZoomControlsEnabled(false);

		int fillColor = mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR);
		int strokeColor = mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, Color.BLACK);
		float strokeWidth;
		if (mPrefs.getBoolean(PREF_CIRCLE_STROKE, false)) {
			strokeWidth = DEF_STROKE;
		} else {
			strokeWidth = NO_STROKE;
		}
	}

	private void setUpOverviewMap() {
		UiSettings settings = mMiniMap.getUiSettings();

		settings.setCompassEnabled(false);
		settings.setZoomControlsEnabled(false);

		mMiniMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
				mMainMap.animateCamera(cameraUpdate);
			}
		});

		int fillColor = mPrefs.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_VIEW_AREA_COLOR);

		float strokeWidth;
		if (mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false)) {
			strokeWidth = DEF_STROKE;
		} else {
			strokeWidth = NO_STROKE;
		}

		int strokeColor = mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, DEF_VIEW_AREA_COLOR);

		mOverviewPolygon = mMiniMap.addPolygon(new PolygonOptions()
		.addAll(getMainViewRegionPoints())
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));
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
		animateZoomedView(cameraPosition);
		synchronized (mCameraListeners) {
			for (GoogleMap.OnCameraChangeListener listener : mCameraListeners) {
				listener.onCameraChange(cameraPosition);
			}
		}
	}

	/**
	 * A structure to store information about a Marker.
	 */
	public static class MarkerModel {
		MarkerOptions mOptions;
		Marker mMarker;

		public MarkerModel(MarkerOptions options, Marker marker) {
			mOptions = options;
			mMarker = marker;
		}
	}

	private void animateZoomedView(CameraPosition mainMapCameraPosition) {

		final float maxZoomDiff = 6f;
		final float minZoomDiff = 2f;
		final float aveZoomDiff = 4f;
		float zoomDiff = mainMapCameraPosition.zoom - mMiniMap.getCameraPosition().zoom;
		if (zoomDiff > maxZoomDiff || zoomDiff < minZoomDiff) {
			mMiniMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainMapCameraPosition.target, 
					mainMapCameraPosition.zoom - aveZoomDiff),
					new GoogleMap.CancelableCallback() {

				@Override
				public void onFinish() {
					boundZoomedView();
				}

				@Override
				public void onCancel() {
					// Do nothing.
				}

			});
		} else {
			boundZoomedView();
		}

		mOverviewPolygon.setPoints(getMainViewRegionPoints());
	}

	private void boundZoomedView() {
		boolean adjustMap = false;

		LatLngBounds boundsMapRight = mMainMap.getProjection().getVisibleRegion().latLngBounds;
		LatLngBounds boundsMapLeft = mMiniMap.getProjection().getVisibleRegion().latLngBounds;

		LatLng swMapRight = boundsMapRight.southwest;
		LatLng swMapLeft = boundsMapLeft.southwest;

		LatLng neMapRight = boundsMapRight.northeast;
		LatLng neMapLeft = boundsMapLeft.northeast;

		double diff;
		if (swMapLeft.latitude > swMapRight.latitude) {
			diff = swMapLeft.latitude - swMapRight.latitude;
			swMapLeft = new LatLng(swMapRight.latitude, swMapLeft.longitude);
			neMapLeft = new LatLng(neMapLeft.latitude - diff, neMapLeft.longitude);
			adjustMap = true;
		} else if (neMapRight.latitude > neMapLeft.latitude) {
			diff = neMapRight.latitude - neMapLeft.latitude;
			neMapLeft = new LatLng(neMapRight.latitude, neMapLeft.longitude);
			swMapLeft = new LatLng(swMapLeft.latitude + diff, swMapLeft.longitude);
			adjustMap = true;
		}

		if (swMapLeft.longitude > swMapRight.longitude) {
			diff = swMapLeft.longitude - swMapRight.longitude;
			swMapLeft = new LatLng(swMapLeft.latitude, swMapRight.longitude);
			neMapLeft = new LatLng(neMapLeft.latitude, neMapLeft.longitude - diff);
			adjustMap = true;
		} else if (neMapRight.longitude > neMapLeft.longitude) {
			diff = neMapRight.longitude - neMapLeft.longitude;
			neMapLeft = new LatLng(neMapLeft.latitude, neMapRight.longitude);
			swMapLeft = new LatLng(swMapLeft.latitude, swMapLeft.longitude + diff);
			adjustMap = true;
		}

		if (adjustMap) {
			mMiniMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(swMapLeft, neMapLeft), 0));
		}
	}

	private List<LatLng> getMainViewRegionPoints() {
		final VisibleRegion mainVR = mMainMap.getProjection().getVisibleRegion();
		final List<LatLng> mainVRpoints = new ArrayList<LatLng>();
		mainVRpoints.add(mainVR.farLeft);
		mainVRpoints.add(mainVR.farRight);
		mainVRpoints.add(mainVR.nearRight);
		mainVRpoints.add(mainVR.nearLeft);

		return mainVRpoints;
	}

	public void addPendingMarkers(int type, Map<String, MarkerOptions> pendingMarkers) {

		for (Entry<String, MarkerOptions> options : pendingMarkers.entrySet()) {
			addMarker(type, options.getValue());
		}
		
		mFilterLoaded.put(type, true);
		showMarkers(type);
		enableMarkers();
	}

	private void addMarker(int type, MarkerOptions options) {
		
		Marker m = mMainMap.addMarker(options);

		MarkerModel model = new MarkerModel(options, m);

		mMarkersFilter.get(type).add(m);
		mMarkers.put(options.getSnippet(), model);
	}
}
