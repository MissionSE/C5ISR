package com.missionse.mapviewer.clustering;

import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;

public interface ClusterInfoWindowAdapter<T extends ClusterItem> {

    public View getClusterInfoWindow(Marker marker, Cluster<T> cluster);

    public View getClusterInfoContents(Marker marker, Cluster<T> cluster);
}
