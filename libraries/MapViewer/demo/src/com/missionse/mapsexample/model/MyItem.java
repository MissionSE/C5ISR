package com.missionse.mapsexample.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    public final String mName;
    public final int mPhoto;
    private final LatLng mPosition;

    public MyItem(LatLng position, String name, int pictureResource) {
        mName = name;
        mPhoto = pictureResource;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

