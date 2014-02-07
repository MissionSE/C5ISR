package com.missionse.mapviewer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.google.android.gms.maps.model.Marker;
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
	private static final String KEY_DISPLAY_PERCENT = "key_display_percent";
	private static final String KEY_VIEW_FILL_COLOR = "key_view_fill_color";
	private static final String KEY_VIEW_STROKE_COLOR = "key_view_stroke_color";
	private static final String KEY_VIEW_STROKE_WIDTH = "key_view_stroke_width";
	private static final int DEF_DISPLAY_PERCENT = 25;
	private static final int PERCENT_TO_DECIMAL = 100;

	private GoogleMap mMiniMap;
	private Callbacks mCallbacks;
	private Polygon mZoomedViewPolygon;

	/**
	 * Factory method to generate a new instance of the fragment given a contact Uri. A factory
     * method is preferable to simply using the constructor as it handles creating the bundle and
     * setting the bundle as an argument.
     * 
	 * @param displayPercent percent of display the mini map will fill; 0 -> 100
	 * @param viewFillColor color to set view polygon fill
	 * @param viewStrokeColor color to set view polygon stroke
	 * @param viewStrokeWidth width to set view polygon stroke
	 * @return new instance of {@link MiniMapFragment} with arguments set
	 */
	public static MiniMapFragment newInstance(int displayPercent, int viewFillColor, int viewStrokeColor, float viewStrokeWidth) {
		// Create new instance of this fragment
		final MiniMapFragment miniMapFragment = new MiniMapFragment();

		// Create and populate the args bundle
		final Bundle args = new Bundle();
		args.putInt(KEY_DISPLAY_PERCENT, Math.min(PERCENT_TO_DECIMAL, Math.max(displayPercent, 0)));
		args.putInt(KEY_VIEW_FILL_COLOR, viewFillColor);
		args.putInt(KEY_VIEW_STROKE_COLOR, viewStrokeColor);
		args.putFloat(KEY_VIEW_STROKE_WIDTH, viewStrokeWidth);
		
		// Assign the args bundle to the new fragment
		miniMapFragment.setArguments(args);

		// Return fragment
		return miniMapFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mCallbacks = (Callbacks) activity;
		try {
			mCallbacks = (Callbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MiniMapFragment.Callbacks");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		mCallbacks.deregisterOnCameraChangeListener(this);
		mCallbacks = null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mCallbacks.registerOnCameraChangeListener(this);
	}

	/**
	 * Required interface for hosting activities.
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);

		v.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						final View v = getView();

						int displayPercent = getArguments().getInt(KEY_DISPLAY_PERCENT, DEF_DISPLAY_PERCENT);

						FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
						layoutParams.width = (int) (v.getWidth() * (displayPercent / (float) PERCENT_TO_DECIMAL));
						layoutParams.height = (int) (v.getHeight() * (displayPercent / (float) PERCENT_TO_DECIMAL));
						v.setLayoutParams(layoutParams);

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

		mMiniMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(marker.getPosition());
				mCallbacks.getMainMap().animateCamera(cameraUpdate);
				return true;
			}
		});
		
		Bundle args = getArguments();
		int fillColor = args.getInt(KEY_VIEW_FILL_COLOR, getActivity().getResources().getColor(R.color.map_polygon_fill_color));
		int strokeColor = args.getInt(KEY_VIEW_STROKE_COLOR, getActivity().getResources().getColor(R.color.map_polygon_stroke_color));
		float strokeWidth = args.getFloat(KEY_VIEW_STROKE_WIDTH, 0);

		mZoomedViewPolygon = mMiniMap.addPolygon(new PolygonOptions()
		.fillColor(fillColor)
		.strokeColor(strokeColor)
		.strokeWidth(strokeWidth)
		.addAll(getMainViewRegionPoints()));
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

	/**
	 * Sets the fill color of the view polygon.
	 * @param fillColor color for fill
	 */
	public void setViewFillColor(int fillColor) {
		this.mZoomedViewPolygon.setFillColor(fillColor);
	}

	/**
	 * Sets the stroke color of the view polygon.
	 * @param strokeColor color for stroke
	 */
	public void setViewStrokeColor(int strokeColor) {
		this.mZoomedViewPolygon.setStrokeColor(strokeColor);
	}

	/**
	 * Sets the stroke width of the view polygon.
	 * @param strokeWidth width for stroke
	 */
	public void setViewStrokeWidth(float strokeWidth) {
		this.mZoomedViewPolygon.setStrokeWidth(strokeWidth);
	}

}
