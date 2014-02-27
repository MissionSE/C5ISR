package com.missionse.mapviewer.clustering;

import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

public interface ClusterItemInfoWindowAdapter<T extends ClusterItem> {

    public View getClusterItemInfoWindow(Marker marker, T item);

    public View getClusterItemInfoContents(Marker marker, T item);

}
