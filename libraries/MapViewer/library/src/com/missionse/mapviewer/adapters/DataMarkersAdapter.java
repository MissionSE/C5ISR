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

import java.util.HashMap;
import java.util.Iterator;

public abstract class DataMarkersAdapter<TData, TMarkerData, TMarkerKey>
        implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = DataMarkersAdapter.class.getSimpleName();
    private Context mContext;
    private TData mData;
    private boolean mFullInfoWindowEnabled = false;
    private int mInfoLayout;
    private View mInfoView;
    private DataViewBinder<TMarkerData> mInfoViewBinder;
    private GoogleMap mMap;
    private SparseArray<BitmapDescriptor> mMarkerIcons = new SparseArray<BitmapDescriptor>();
    private HashMap<TMarkerKey, Marker> mMarkers = new HashMap<TMarkerKey, Marker>();
    private HashMap<Marker, TMarkerData> mMarkersData = new HashMap<Marker, TMarkerData>();

    protected DataMarkersAdapter(Context context, GoogleMap map, int infoLayout, DataViewBinder<TMarkerData> infoViewBinder) {
        this.mContext = context;
        this.mMap = map;
        this.mInfoLayout = infoLayout;
        this.mInfoViewBinder = infoViewBinder;
    }

    private View getInfoView(Marker marker) {
        Log.d(TAG, "gentInfoView calling getInfoView");
        this.mInfoView = getInfoView(marker, this.mMarkersData.get(marker), this.mInfoView);
        return this.mInfoView;
    }

    public Marker addMarker(TMarkerData markerData) {
        Marker marker = getMarker(getMarkerKey(markerData));
        if (marker == null) {
            marker = this.mMap.addMarker(getMarkerOptions(markerData));
        }
        setMarkerData(marker, markerData);
        return marker;
    }

    public abstract BitmapDescriptor createIcon(int resourceId);

    protected Context getContext() {
        return this.mContext;
    }

    public abstract int getCount();

    public TData getData() {


        return this.mData;
    }

    public void setData(TData data) {
        setData(data, true);
    }

    public abstract int getIconType(TMarkerData markerData);

    protected View getInfoView(Marker marker, TMarkerData markerData, View view) {
        Log.d(TAG, "getInfoView");
        if ((view == null) && (this.mInfoLayout > 0)) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mInfoLayout, null);
            Log.d(TAG, "inflated view=" + view);
            view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (this.mInfoViewBinder != null)
            this.mInfoViewBinder.setViewValue(view, markerData);
        return view;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Log.d(TAG, "getInfoWindow");
        View view = null;
        if (this.mFullInfoWindowEnabled) {
            Log.d(TAG, "getInfoWindow calling getInfoView");
            view = getInfoView(marker);
        }
        Log.d(TAG, "view=" + view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.d(TAG, "getInfoContents");
        View view = null;
        if (!this.mFullInfoWindowEnabled) {
            Log.d(TAG, "getInfoContents calling getInfoView");
            view = getInfoView(marker);
        }
        return view;
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

    public abstract double getMarkerLatitude(TMarkerData markerData);

    public abstract double getMarkerLongitude(TMarkerData markerData);

    protected MarkerOptions getMarkerOptions(TMarkerData markerData) {
        int iconType = getIconType(markerData);
        BitmapDescriptor bitmapDescriptor = this.mMarkerIcons.get(iconType);
        if (bitmapDescriptor == null) {
            bitmapDescriptor = createIcon(iconType);
            this.mMarkerIcons.put(iconType, bitmapDescriptor);
        }
        return new MarkerOptions()
                .position(new LatLng(getMarkerLatitude(markerData), getMarkerLongitude(markerData)))
                .icon(bitmapDescriptor);
    }

    public void setData(TData data, boolean paramBoolean) {
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

    public void setInfoViewBinder(DataViewBinder<TMarkerData> paramDataViewBinder) {
        this.mInfoViewBinder = paramDataViewBinder;
    }

    public void setMarkerData(Marker marker, TMarkerData markerData) {
        if ((this.mMarkersData.put(marker, markerData) != markerData) && (marker.isInfoWindowShown())) {
            marker.showInfoWindow();
        }
    }
}
