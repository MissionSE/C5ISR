package com.missionse.mapviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 * A {@link GoogleMap} view in a fragment that is sized to a configurable
 * percentage of the display that shows a zoomed out view of a {@link GoogleMap} 
 * that is considered the main map.  The view region of the main map is highlighted
 * on this map which is configurable through shared preferences.  This map registers
 * as a {@link GoogleMap.OnCameraChangeListener} to keep it's view region in sync with the 
 * main map.  It also listens for clicks as a {@link GoogleMap.OnMapClickListener} on the
 * smaller map in order to change the main map to display the desired view area.
 *
 */
public class MiniMapFragment extends MapFragment implements 
OnSharedPreferenceChangeListener,
OnCameraChangeListener {
	

	private static final String TAG = MiniMapFragment.class.getSimpleName();

	private static final String PREF_VIEW_AREA_FILL_COLOR = "pref_view_area_fill_color";
	private static final String PREF_VIEW_AREA_STROKE = "pref_view_area_stroke";
	private static final String PREF_VIEW_AREA_STROKE_COLOR = "pref_view_area_stroke_color";
	private static final int DEF_VIEW_AREA_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float NO_STROKE = 0f;
	private static final int DEF_SCREEN_RATIO = 3;

	private MapViewerFragment mMapViewerFragment;
	private GoogleMap mMainMap;	
	private GoogleMap mMiniMap;

	private Polygon mZoomedViewPolygon;

	private SharedPreferences mPrefs;

	/**
	 * Creates a new instance of the {@link MiniMapFragment} and sets the 
	 * <code>mMainMap</code> which will be used to update this map.
	 * @param mapViewerFragment the {@link MapViewerFragment} use to update this map
	 * @return a new instance of the {@link MiniMapFragment}
	 */
	public static MiniMapFragment newInstance(MapViewerFragment mapViewerFragment) {
		MiniMapFragment fragment = new MiniMapFragment();
		fragment.mMapViewerFragment = mapViewerFragment;

		return fragment;
	}

	/**
	 * Constructs a {@link MiniMapFragment}.
	 */
	public MiniMapFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);

		View containerParentView = (View) container.getParent();
		v.setLayoutParams(new FrameLayout.LayoutParams(
				containerParentView.getWidth() / DEF_SCREEN_RATIO, 
				containerParentView.getHeight() / DEF_SCREEN_RATIO));

		return v;
	}

	private void setUpMapIfNeeded() {
		if (mMainMap == null) {
			mMainMap = mMapViewerFragment.getMainMap();
		}
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMiniMap == null) {
			// Try to obtain the map.
			mMiniMap = getMap();
			// Check if we were successful in obtaining the map.
			if (mMiniMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
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

		mMapViewerFragment.registerOnCameraChangeListener(this);

		int fillColor = mPrefs.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_VIEW_AREA_COLOR);

		float strokeWidth;
		if (mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false)) {
			strokeWidth = DEF_STROKE;
		} else {
			strokeWidth = NO_STROKE;
		}

		int strokeColor = mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, DEF_VIEW_AREA_COLOR);

		mZoomedViewPolygon = mMiniMap.addPolygon(new PolygonOptions()
		.addAll(getMainViewRegionPoints())
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));
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

		mZoomedViewPolygon.setPoints(getMainViewRegionPoints());
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (PREF_VIEW_AREA_FILL_COLOR.equals(key)) {
			mZoomedViewPolygon.setFillColor(sharedPreferences.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_VIEW_AREA_COLOR));
		}  else if (PREF_VIEW_AREA_STROKE.equals(key) || PREF_VIEW_AREA_STROKE_COLOR.equals(key)) {
			int strokeColor =  mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, Color.BLACK);
			float strokeWidth;
			if (mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false)) {
				strokeWidth = DEF_STROKE;
			} else {
				strokeWidth = NO_STROKE;
			}
			mZoomedViewPolygon.setStrokeColor(strokeColor);
			mZoomedViewPolygon.setStrokeWidth(strokeWidth);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		setUpMapIfNeeded();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mMapViewerFragment.removeOnCameraChangeListener(this);
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

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		animateZoomedView(cameraPosition);
	}

}
