package com.missionse.mapviewer.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;

import java.util.HashMap;
import java.util.Iterator;

public abstract class DataMarkersAdapter<TData, TMarkerData extends ClusterItem, TMarkerKey>
        implements GoogleMap.InfoWindowAdapter,
        ClusterManager.OnClusterClickListener<TMarkerData>,
        ClusterManager.OnClusterInfoWindowClickListener<TMarkerData>,
        ClusterManager.OnClusterItemClickListener<TMarkerData>,
        ClusterManager.OnClusterItemInfoWindowClickListener<TMarkerData> {
    private static final String TAG = DataMarkersAdapter.class.getSimpleName();
    private Context mContext;
    private TData mData;
    private boolean mFullInfoWindowEnabled = false;
    private int mInfoLayout;
    private View mInfoView;
    private GoogleMap mMap;
    private SparseArray<BitmapDescriptor> mMarkerIcons = new SparseArray<BitmapDescriptor>();
    private HashMap<TMarkerKey, Marker> mMarkers = new HashMap<TMarkerKey, Marker>();
    private HashMap<Marker, TMarkerData> mMarkersData = new HashMap<Marker, TMarkerData>();
    private ClusterManager<TMarkerData> mClusterManager;

    protected DataMarkersAdapter(Context context, GoogleMap map, int infoLayout) {
        this.mContext = context;
        this.mMap = map;
        this.mInfoLayout = infoLayout;

        mClusterManager = new ClusterManager<TMarkerData>(mContext, mMap);
        mClusterManager.setRenderer(getRenderer());
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    private View getInfoView(Marker marker) {
        this.mInfoView = getInfoView(marker, this.mMarkersData.get(marker), this.mInfoView);
        return this.mInfoView;
    }

    public void addMarker(TMarkerData markerData) {
        mClusterManager.addItem(markerData);
        mClusterManager.cluster();
    }

    public abstract BitmapDescriptor createClusterIcon(int index, Cluster<TMarkerData> cluster);

    public abstract BitmapDescriptor createIcon(int index);

    protected Context getContext() {
        return this.mContext;
    }

    public abstract int getCount();

    public TData getData() {


        return this.mData;
    }

    public abstract int getClusterIconType(Cluster<TMarkerData> cluster);

    public abstract int getIconType(TMarkerData markerData);

    protected View getInfoView(Marker marker, TMarkerData markerData, View view) {
        if ((view == null) && (this.mInfoLayout > 0)) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mInfoLayout, null);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = null;
        if (this.mFullInfoWindowEnabled) {
            view = getInfoView(marker);
        }
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = null;
        if (!this.mFullInfoWindowEnabled) {
            view = getInfoView(marker);
        }
        return view;
    }

    protected BitmapDescriptor getClusterIcon(Cluster<TMarkerData> cluster) {
        int iconType = getClusterIconType(cluster);

        return createClusterIcon(iconType, cluster);
    }

    public ClusterManager<TMarkerData> getClusterManager() {
        return mClusterManager;
    }

    public GoogleMap getMap() {
        return this.mMap;
    }

    public Marker getMarker(TMarkerKey markerKey) {
        return this.mMarkers.get(markerKey);
    }

    public abstract TMarkerData getMarkerData(int paramInt);

    public TMarkerData getMarkerData(Marker marker) {
        return this.mMarkersData.get(marker);
    }

    public abstract TMarkerKey getMarkerKey(TMarkerData markerData);

    public abstract LatLng getMarkerPosition(TMarkerData markerData);

    public abstract double getMarkerLatitude(TMarkerData markerData);

    public abstract double getMarkerLongitude(TMarkerData markerData);

    protected BitmapDescriptor getMarkerIcon(TMarkerData markerData) {
        int iconType = getIconType(markerData);
        BitmapDescriptor bitmapDescriptor = this.mMarkerIcons.get(iconType);
        if (bitmapDescriptor == null) {
            bitmapDescriptor = createIcon(iconType);
            this.mMarkerIcons.put(iconType, bitmapDescriptor);
        }
        return bitmapDescriptor;
    }

    protected MarkerOptions getMarkerOptions(TMarkerData markerData) {
        BitmapDescriptor bitmapDescriptor = getMarkerIcon(markerData);
        return new MarkerOptions()
                .position(new LatLng(getMarkerLatitude(markerData), getMarkerLongitude(markerData)))
                .icon(bitmapDescriptor);
    }

    public abstract ClusterRenderer<TMarkerData> getRenderer();

    public void setData(TData data) {
        this.mData = data;
        HashMap<TMarkerKey, Marker> markers = new HashMap<TMarkerKey, Marker>();
        for (int i = 0; i < getCount(); i++) {
            TMarkerData markerData = getMarkerData(i);
            TMarkerKey markerKey = getMarkerKey(markerData);
            Marker marker = this.mMarkers.remove(markerKey);
            if (marker == null) {
                marker = this.mMap.addMarker(getMarkerOptions(markerData));
                setMarkerData(marker, markerData);
                this.mMarkersData.put(marker, markerData);
            }
            markers.put(markerKey, marker);
        }
        this.mMarkers = markers;
    }

    public void setFullInfoWindowEnabled(boolean enabled) {
        this.mFullInfoWindowEnabled = enabled;
    }

    public void setMarkerData(Marker marker, TMarkerData markerData) {
        if ((this.mMarkersData.put(marker, markerData) != markerData) && (marker.isInfoWindowShown())) {
            marker.showInfoWindow();
        }
    }

    @Override
    public boolean onClusterClick(Cluster<TMarkerData> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<TMarkerData> cluster) {

    }

    @Override
    public boolean onClusterItemClick(TMarkerData markerData) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(TMarkerData markerData) {

    }

}
