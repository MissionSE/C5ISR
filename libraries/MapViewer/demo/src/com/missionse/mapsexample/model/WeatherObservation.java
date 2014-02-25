package com.missionse.mapsexample.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.missionse.mapsexample.openweathermap.OpenWeatherMapData;

public class WeatherObservation implements ClusterItem {
    private OpenWeatherMapData mData;

    public WeatherObservation(OpenWeatherMapData data) {
        mData = data;
    }

    public OpenWeatherMapData getData() {
        return mData;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mData.getCoord().getLat(), mData.getCoord().getLon());
    }
}
