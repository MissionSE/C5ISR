package com.missionse.mapsexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.MarkerManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.missionse.mapsexample.openweathermap.OpenWeatherMapData;


public class CustomMarkerInfoWindowActivity extends Activity implements
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = CustomMarkerInfoWindowActivity.class.getSimpleName();
    private static final float CALLOUT_ZOOM = 10.0F;
    private GoogleMap mMap;
    private Marker mCurrentMarker;
    private WeatherMarkersAdapter mMarkersAdapter;
    private Marker mMarker;
    private MarkerManager mMarkerManager;

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideObservationCallout();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick");
        showObservationCallout(marker);
        return true;
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
        final double ten = 10;
        final double five = 5;
        final double radius = 400000;

        this.mMarkersAdapter = new WeatherMarkersAdapter(this, this.mMap);

        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(this.mMarkersAdapter);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    private void hideObservationCallout() {
        if (this.mCurrentMarker != null) {
            this.mCurrentMarker.hideInfoWindow();
            this.mCurrentMarker = null;
        }
    }

    private void showObservationCallout(Marker marker) {
        this.mCurrentMarker = marker;
        marker.showInfoWindow();
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
                        mMarkersAdapter.addMarker(openWeatherMapData);
                    } else {
                        Log.e(TAG, "No data obtained from openweathermap.org");
                    }
                }
            }
        });
    }

}
