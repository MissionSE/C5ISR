package com.missionse.mapsexample;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.PolygonOptions;
import com.missionse.mapviewer.MiniMapFragment;

public class OverviewMapsActivity extends Activity implements MiniMapFragment.Callbacks {
	private static final String TAG = OverviewMapsActivity.class.getSimpleName();
	
	private MapFragment mMapFragment;
	private MiniMapFragment mMiniMapFragment;
	
	
	private PolygonOptions mViewPolygonOptions;

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
	public PolygonOptions getViewPolygonOptions() {
		return mViewPolygonOptions;
	}
	
	@Override
	public double getDisplayPercentage() {
		return 0.25;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_overview);
		
		mViewPolygonOptions = new PolygonOptions().fillColor(Color.argb((int) (255 * 0.75), 0, 0, 0)).strokeWidth(0);
		
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
			mMiniMapFragment = new MiniMapFragment();

			fm.beginTransaction()
			.add(R.id.fragment_container_map_overview, mMiniMapFragment, "map_overview")
			.commit();
		}
	}
	
}
