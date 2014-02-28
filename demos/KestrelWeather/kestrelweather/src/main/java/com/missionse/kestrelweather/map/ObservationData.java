package com.missionse.kestrelweather.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.joda.time.DateTime;

public class ObservationData implements ClusterItem {

    @Override
    public LatLng getPosition() {
        return null;
    }

    public double getTemp() {
        return 0.0;
    }

    public double getPressure() {
        return 0.0;
    }

    public double getWindSpeed() {
        return 0.0;
    }

    public int getHumidity() {
        return 0;
    }

    public DateTime getTime() {
        return DateTime.now();
    }

    public int getConditionCode() {
        return 0;
    }
}
