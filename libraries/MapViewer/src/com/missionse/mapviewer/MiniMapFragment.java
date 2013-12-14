package com.missionse.mapviewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
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
 */
public class MiniMapFragment extends MapFragment implements 
OnCameraChangeListener {
	private static final String TAG = MiniMapFragment.class.getSimpleName();
	private static final int DEF_SCREEN_RATIO = 3;
	
	private GoogleMap mMiniMap;
	private Callbacks mCallbacks;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mCallbacks = (Callbacks) activity;
		mCallbacks.registerOnCameraChangeListener(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		mCallbacks.deregisterOnCameraChangeListener(this);
		mCallbacks = null;
	}

	private Polygon mZoomedViewPolygon;

	/**
	 * Required interface for hosting activities.
	 *
	 */
	public interface Callbacks {
		
		/**
		 * Registers for camera change callback from another {@link GoogleMap}.
		 * @param listener the listener registering for a callback
		 */
		void registerOnCameraChangeListener(OnCameraChangeListener listener);
		
		/**
		 * Deregisters for camera change callback from another {@link GoogleMap}.
		 * @param listener the listener deregistering for a callback
		 */
		void deregisterOnCameraChangeListener(OnCameraChangeListener listener);
		
		/**
		 * @return the main map
		 */
		GoogleMap getMainMap();
		
		/**
		 * @return the polygon options for the view region
		 */
		PolygonOptions getViewPolygonOptions();
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
		
		mCallbacks.registerOnCameraChangeListener(this);

		mMiniMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
				mCallbacks.getMainMap().animateCamera(cameraUpdate);
			}
		});
		
		PolygonOptions options = mCallbacks.getViewPolygonOptions();
		options.addAll(getMainViewRegionPoints());
		mZoomedViewPolygon = mMiniMap.addPolygon(options);
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

		LatLngBounds boundsMapRight = mCallbacks.getMainMap().getProjection().getVisibleRegion().latLngBounds;
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

		setUpMapIfNeeded();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		setUpMapIfNeeded();
	}

	private List<LatLng> getMainViewRegionPoints() {
		final VisibleRegion mainVR = mCallbacks.getMainMap().getProjection().getVisibleRegion();
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
	
	public void setViewFillColor(int fillColor) {
		this.mZoomedViewPolygon.setFillColor(fillColor);
	}
	
	public void setViewStrokeColor(int strokeColor) {
		this.mZoomedViewPolygon.setStrokeColor(strokeColor);
	}
	
	public void setViewStrokeWidth(float strokeWidth) {
		this.mZoomedViewPolygon.setStrokeWidth(strokeWidth);
	}

}
