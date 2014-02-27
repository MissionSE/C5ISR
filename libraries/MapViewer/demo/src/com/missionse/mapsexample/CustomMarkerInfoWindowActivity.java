package com.missionse.mapsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.mapsexample.model.WeatherObservation;
import com.missionse.mapsexample.openweathermap.OpenWeatherMapData;


public class CustomMarkerInfoWindowActivity extends Activity implements
        GoogleMap.OnMapLongClickListener {

    private static final String TAG = CustomMarkerInfoWindowActivity.class.getSimpleName();
    private GoogleMap mMap;
    private WeatherMarkersAdapter mMarkersAdapter;

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
                Intent i = new Intent(this, WeatherSettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_marker_info_window);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.MapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
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
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMarkersAdapter = new WeatherMarkersAdapter(this, mMap);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + latLng.latitude + "&lon=" + latLng.longitude;
        Log.d(TAG, "url=" + url);
        Ion.with(this, url).as(new TypeToken<OpenWeatherMapData>() {
        }).setCallback(new FutureCallback<OpenWeatherMapData>() {
            @Override
            public void onCompleted(Exception e, OpenWeatherMapData openWeatherMapData) {
                if (e != null) {
                    Log.e(TAG, "Error getting openweathermap data.", e);
                } else {
                    if (openWeatherMapData != null) {
                        Log.d(TAG, openWeatherMapData.toString());
                        mMarkersAdapter.addMarker(new WeatherObservation(openWeatherMapData));
                    } else {
                        Log.e(TAG, "No data obtained from openweathermap.org");
                    }
                }
            }
        });
    }

}
