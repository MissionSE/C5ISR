package com.missionse.mapviewer.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.missionse.mapviewer.clustering.ClusterInfoWindowAdapter;
import com.missionse.mapviewer.clustering.ClusterItemInfoWindowAdapter;

public abstract class DataMarkersAdapter<TData, TMarkerData extends ClusterItem, TMarkerKey> implements
        ClusterManager.OnClusterClickListener<TMarkerData>,
        ClusterManager.OnClusterInfoWindowClickListener<TMarkerData>,
        ClusterManager.OnClusterItemClickListener<TMarkerData>,
        ClusterManager.OnClusterItemInfoWindowClickListener<TMarkerData>,
        ClusterInfoWindowAdapter<TMarkerData>,
        ClusterItemInfoWindowAdapter<TMarkerData> {
    private static final String TAG = DataMarkersAdapter.class.getSimpleName();
    private Context mContext;
    private TData mData;
    private boolean mFullInfoWindowEnabled = false;
    private int mInfoLayout;
    private View mInfoView;
    private GoogleMap mMap;
    private SparseArray<BitmapDescriptor> mMarkerIcons = new SparseArray<BitmapDescriptor>();
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
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    public void addMarker(TMarkerData markerData) {
        mClusterManager.addItem(markerData);
        mClusterManager.cluster();
    }

    protected Context getContext() {
        return this.mContext;
    }

    public TData getData() {
        return this.mData;
    }

    protected View getInfoView(Marker marker, TMarkerData markerData, View view) {
        if ((view == null) && (this.mInfoLayout > 0)) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mInfoLayout, null);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return view;
    }

    protected View getInfoView(Marker marker, Cluster<TMarkerData> cluster, View view) {
        if ((view == null) && (this.mInfoLayout > 0)) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mInfoLayout, null);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

    protected BitmapDescriptor getClusterItemIcon(TMarkerData markerData) {
        int iconType = getClusterItemIconType(markerData);
        BitmapDescriptor bitmapDescriptor = this.mMarkerIcons.get(iconType);
        if (bitmapDescriptor == null) {
            bitmapDescriptor = createClusterItemIcon(iconType);
            this.mMarkerIcons.put(iconType, bitmapDescriptor);
        }
        return bitmapDescriptor;
    }

    public void setFullInfoWindowEnabled(boolean enabled) {
        this.mFullInfoWindowEnabled = enabled;
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

    @Override
    public View getClusterInfoWindow(Marker marker, Cluster<TMarkerData> cluster) {
        if (mFullInfoWindowEnabled) {
            mInfoView = getInfoView(marker, cluster, mInfoView);
        }
        return mInfoView;
    }

    @Override
    public View getClusterInfoContents(Marker marker, Cluster<TMarkerData> cluster) {
        if (!mFullInfoWindowEnabled) {
            mInfoView = getInfoView(marker, cluster, mInfoView);
        }
        return mInfoView;
    }

    @Override
    public View getClusterItemInfoWindow(Marker marker, TMarkerData item) {
        if (mFullInfoWindowEnabled) {
            mInfoView = getInfoView(marker, item, mInfoView);
        }
        return mInfoView;
    }

    @Override
    public View getClusterItemInfoContents(Marker marker, TMarkerData item) {
        if (!mFullInfoWindowEnabled) {
            mInfoView = getInfoView(marker, item, mInfoView);
        }
        return mInfoView;
    }

    public abstract BitmapDescriptor createClusterIcon(int index, Cluster<TMarkerData> cluster);

    public abstract BitmapDescriptor createClusterItemIcon(int index);

    public abstract int getClusterIconType(Cluster<TMarkerData> cluster);

    public abstract int getClusterItemIconType(TMarkerData markerData);

    protected abstract ClusterRenderer<TMarkerData> getRenderer();

}
