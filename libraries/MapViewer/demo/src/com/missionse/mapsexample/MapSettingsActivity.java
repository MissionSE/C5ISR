/**
 * 
 */
package com.missionse.mapsexample;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class MapSettingsActivity extends Activity implements
SharedPreferences.OnSharedPreferenceChangeListener {
	private static final String TAG = MapSettingsActivity.class.getSimpleName();

	private static final String PREF_VIEW_AREA_FILL_COLOR = "pref_view_area_fill_color";
	private static final String PREF_VIEW_AREA_STROKE = "pref_view_area_stroke";
	private static final String PREF_VIEW_AREA_STROKE_COLOR = "pref_view_area_stroke_color";
	private static final int DEF_VIEW_AREA_COLOR = Color.argb(150, 0, 0, 0);
	private static final float DEF_STROKE = 2f;
	private static final float NO_STROKE = 0f;
	private static final int DEF_FILL_COLOR = Color.argb(150, 0, 0, 0);
	private static final String PREF_CIRCLE_FILL_COLOR = "pref_circle_fill_color";
	private static final String PREF_CIRCLE_STROKE = "pref_circle_stroke";
	private static final String PREF_CIRCLE_STROKE_COLOR = "pref_circle_stroke_color";

	private SharedPreferences mPrefs;
	private GoogleMap mMap;

	private Marker mMarker;
	private Circle mCircle;
	private Polygon mPolygon;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_settings);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mPrefs.registerOnSharedPreferenceChangeListener(this);

		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.maps_settings_example, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (PREF_VIEW_AREA_FILL_COLOR.equals(key)) {
			mPolygon.setFillColor(sharedPreferences.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_VIEW_AREA_COLOR));
		} else if (PREF_VIEW_AREA_STROKE.equals(key)) {
			float strokeWidth;
			if (mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false)) {
				strokeWidth = DEF_STROKE;
			} else {
				strokeWidth = NO_STROKE;
			}
			mPolygon.setStrokeWidth(strokeWidth);
		} else if (PREF_VIEW_AREA_STROKE_COLOR.equals(key)) {
			mPolygon.setStrokeColor(mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, DEF_FILL_COLOR));
		} else if (PREF_CIRCLE_FILL_COLOR.equals(key)) {
			mCircle.setFillColor(mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR));
		} else if (PREF_CIRCLE_STROKE_COLOR.equals(key)) {
			mCircle.setStrokeColor(mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, DEF_FILL_COLOR));
		} else if (PREF_CIRCLE_STROKE.equals(key)) {
			float strokeWidth;
			if (mPrefs.getBoolean(PREF_CIRCLE_STROKE, false)) {
				strokeWidth = DEF_STROKE;
			} else {
				strokeWidth = NO_STROKE;
			}
			mCircle.setStrokeWidth(strokeWidth);
		}
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p>
	 * If it isn't installed {@link MapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
	 * install/update the Google Play services APK on their device.
	 * <p>
	 * A user can return to this FragmentActivity after following the prompt and correctly
	 * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
	 * have been completely destroyed during this process (it is likely that it would only be
	 * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case, we
	 * just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() {
		final double ten = 10;
		final double five = 5;
		final double radius = 400000;
		
		int circleFillColor = mPrefs.getInt(PREF_CIRCLE_FILL_COLOR, DEF_FILL_COLOR);
		int circleStrokeColor = mPrefs.getInt(PREF_CIRCLE_STROKE_COLOR, DEF_FILL_COLOR);
		float circleStrokeWidth;
		if (mPrefs.getBoolean(PREF_CIRCLE_STROKE, false)) {
			circleStrokeWidth = DEF_STROKE;
		} else {
			circleStrokeWidth = NO_STROKE;
		}
		
		int polygonFillColor = mPrefs.getInt(PREF_VIEW_AREA_FILL_COLOR, DEF_FILL_COLOR);
		int polygonStrokeColor = mPrefs.getInt(PREF_VIEW_AREA_STROKE_COLOR, DEF_FILL_COLOR);
		float polygonStrokeWidth;
		if (mPrefs.getBoolean(PREF_VIEW_AREA_STROKE, false)) {
			polygonStrokeWidth = DEF_STROKE;
		} else {
			polygonStrokeWidth = NO_STROKE;
		}
		
		mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(ten, 0)).title("Marker"));
		
		mCircle = mMap.addCircle(new CircleOptions()
		.center(new LatLng(-ten, 0))
		.radius(radius)
		.fillColor(circleFillColor)
		.strokeColor(circleStrokeColor)
		.strokeWidth(circleStrokeWidth));
		
		mPolygon = mMap.addPolygon(new PolygonOptions()
		.fillColor(polygonFillColor)
		.strokeColor(polygonStrokeColor)
		.strokeWidth(polygonStrokeWidth)
		.add(new LatLng(five, five), 
				new LatLng(-five, five), 
				new LatLng(-five, -five), 
				new LatLng(five, -five)));
	}

}
