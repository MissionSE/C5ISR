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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class MiniMapFragment extends MapFragment implements OnSharedPreferenceChangeListener {

	private static final String TAG = MiniMapFragment.class.getSimpleName();
	
	private static final String PREF_VIEW_AREA_FILL_COLOR = "pref_view_area_fill_color";
	private static final String PREF_VIEW_AREA_STROKE = "pref_view_area_stroke";
	private static final String PREF_VIEW_AREA_STROKE_COLOR = "pref_view_area_stroke_color";
	private static final int DEF_VIEW_AREA_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float NO_STROKE = 0f;

	private GoogleMap mMainMap;	

	private Polygon mZoomedViewPolygon;

	private SharedPreferences mPrefs;
	
	public static MiniMapFragment newInstance(GoogleMap mainMap) {
		MiniMapFragment fragment = new MiniMapFragment();
		fragment.mMainMap = mainMap;
		
		return fragment;
	}

	public MiniMapFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		View containerParentView = (View) container.getParent();
		v.setLayoutParams(new FrameLayout.LayoutParams(containerParentView.getWidth()/3, containerParentView.getHeight()/3));

		return v;
	}

	private void setUpMap() {
		GoogleMap miniMap = getMap();
		UiSettings settings = miniMap.getUiSettings();

		settings.setCompassEnabled(false);
		settings.setZoomControlsEnabled(false);

		miniMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
				mMainMap.animateCamera(cameraUpdate);
			}
		});

		mMainMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				animateZoomedView(cameraPosition);
			}
		});

		int fillColor = mPrefs.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_VIEW_AREA_COLOR);
		float strokeWidth = mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false) ? DEF_STROKE : NO_STROKE;
		int strokeColor = mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, DEF_VIEW_AREA_COLOR);

		mZoomedViewPolygon = getMap().addPolygon(new PolygonOptions()
		.addAll(getMainViewRegionPoints())
		.fillColor(fillColor)
		.strokeWidth(strokeWidth)
		.strokeColor(strokeColor));
	}

	private void animateZoomedView(CameraPosition mainMapCameraPosition) {

		float maxZoomDiff = 6f;
		float minZoomDiff = 2f;
		float zoomDiff = mainMapCameraPosition.zoom - getMap().getCameraPosition().zoom;
		if (zoomDiff > maxZoomDiff || zoomDiff < minZoomDiff ) {
			getMap().animateCamera(CameraUpdateFactory.zoomTo(mainMapCameraPosition.zoom - 4f), new GoogleMap.CancelableCallback() {

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
		LatLngBounds boundsMapLeft = getMap().getProjection().getVisibleRegion().latLngBounds;

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
			getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(swMapLeft, neMapLeft), 0));
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
			float strokeWidth = mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false) ? DEF_STROKE : NO_STROKE;
			mZoomedViewPolygon.setStrokeColor(strokeColor);
			mZoomedViewPolygon.setStrokeWidth(strokeWidth);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		setUpMap();
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

}
