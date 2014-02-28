package com.missionse.kestrelweather.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * Provides a handler for the location service.
 */
public class LocationHandler implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private static final String TAG = LocationHandler.class.getSimpleName();
	private LocationClient mLocationClient;

	/**
	 * Constructor.
	 * @param context The current context.
	 */
	public LocationHandler(final Context context) {
		mLocationClient = new LocationClient(context, this, this);
	}

	/**
	 * Called to start the location client.
	 */
	public void onStart() {
		mLocationClient.connect();
	}

	/**
	 * Called to stop the location client.
	 */
	public void onStop() {
		mLocationClient.disconnect();
	}

	/**
	 * Get the last location provided by the location client.
	 * @return The last location obtained by the location client.
	 */
	public Location getLocation() {
		Location location = null;
		if (mLocationClient != null && mLocationClient.isConnected()) {
			location = mLocationClient.getLastLocation();
		}

		return location;
	}

	@Override
	public void onConnected(final Bundle bundle) {
		Log.d(TAG, "Location client connected.");
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "Location client disconnected.");
	}

	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult) {
		Log.e(TAG, "Location client connection failed.");
	}
}
