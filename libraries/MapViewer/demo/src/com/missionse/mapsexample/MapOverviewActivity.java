package com.missionse.mapsexample;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.missionse.mapviewer.MiniMapFragment;

public class MapOverviewActivity extends Activity implements MiniMapFragment.Callbacks {
	private static final String TAG = MapOverviewActivity.class.getSimpleName();
	private static final int DEF_DISPLAY_PERCENT = 33;
	private static final int DEF_VIEW_FILL_COLOR = Color.argb(150, 0, 0, 0);
	private static final int DEF_VIEW_STROKE_COLOR = Color.argb(255, 0, 0, 0);
	private static final float DEF_VIEW_STROKE_WIDTH = 2f;
	
	private MapFragment mMapFragment;
	private MiniMapFragment mMiniMapFragment;

	@Override
	public void registerOnCameraChangeListener(OnCameraChangeListener listener) {
		getMapViewerFragment().getMap().setOnCameraChangeListener(listener);
	}

	@Override
	public void deregisterOnCameraChangeListener(OnCameraChangeListener listener) {
		// Do nothing.	
	}

	@Override
	public GoogleMap getMainMap() {
		return getMapViewerFragment().getMap();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_overview);

        getActionBar().setDisplayHomeAsUpEnabled(true);
		
		getMapViewerFragment();
		setUpMiniMapFragment();
	}
	
	private MapFragment getMapViewerFragment() {
		FragmentManager fm = getFragmentManager();
		mMapFragment = (MapFragment) fm.findFragmentByTag("map");
		if (mMapFragment == null) {
			mMapFragment = new MapFragment();

			fm.beginTransaction()
			.add(R.id.fragment_container_map_view, mMapFragment, "map")
			.commit();
		}
		return mMapFragment;
	}
	
	private void setUpMiniMapFragment() {
		FragmentManager fm = getFragmentManager();
		mMiniMapFragment = (MiniMapFragment) fm.findFragmentByTag("map_overview");
		if (mMiniMapFragment == null) {
			mMiniMapFragment = MiniMapFragment.newInstance(
					DEF_DISPLAY_PERCENT, 
					DEF_VIEW_FILL_COLOR, 
					DEF_VIEW_STROKE_COLOR, 
					DEF_VIEW_STROKE_WIDTH);

			fm.beginTransaction()
			.add(R.id.fragment_container_map_overview, mMiniMapFragment, "map_overview")
			.commit();
		}
	}
	
}
