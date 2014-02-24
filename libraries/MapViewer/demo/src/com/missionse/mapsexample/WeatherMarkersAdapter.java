package com.missionse.mapsexample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.mapsexample.openweathermap.OpenWeatherMapData;
import com.missionse.mapsexample.utils.BitmapHelper;
import com.missionse.mapsexample.utils.ResourcesHelper;
import com.missionse.mapviewer.adapters.DataMarkersAdapter;

import java.util.List;

public class WeatherMarkersAdapter extends DataMarkersAdapter<List<OpenWeatherMapData>, OpenWeatherMapData, String> {
    private static final String TAG = WeatherMarkersAdapter.class.getSimpleName();
    private OpenWeatherMapData mCurrentMarkerData;

    public WeatherMarkersAdapter(Context context, GoogleMap map) {
        super(context, map, R.layout.observation_map_callout);

        setFullInfoWindowEnabled(true);
    }

    @Override
    public BitmapDescriptor createIcon(int index) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(ResourcesHelper.getTemperatureColorByIndex(getContext(), index));
        Drawable[] arrayOfDrawable = new Drawable[2];
        arrayOfDrawable[0] = getContext().getResources().getDrawable(R.drawable.bg_marker);
        arrayOfDrawable[1] = shapeDrawable;
        LayerDrawable layerDrawable = new LayerDrawable(arrayOfDrawable);
        int i = (int) (5.0F * getContext().getResources().getDisplayMetrics().density);
        layerDrawable.setLayerInset(1, i, i, i, i);

        return BitmapDescriptorFactory.fromBitmap(BitmapHelper.getDrawableBitmap(layerDrawable));
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public int getIconType(OpenWeatherMapData openWeatherMapData) {
        return ResourcesHelper.getTemperatureIndex(openWeatherMapData.getMain().getTemp());
    }

    @Override
    protected View getInfoView(final Marker marker, OpenWeatherMapData markerData, View paramView) {
        View view;
        Log.d(TAG, "getInfoView");
        if (this.mCurrentMarkerData != markerData) {
            Log.d(TAG, "getInfoView calling super.getInfoView");
            view = super.getInfoView(marker, markerData, paramView);
            ((ObservationCallout) view).setData(markerData);
            if (marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
            this.mCurrentMarkerData = markerData;
        } else {
            view = paramView;
        }
        return view;
    }

    @Override
    public OpenWeatherMapData getMarkerData(int index) {
        return getData().get(index);
    }

    @Override
    public String getMarkerKey(OpenWeatherMapData openWeatherMapData) {
        return Integer.toString(openWeatherMapData.getId());
    }

    @Override
    public LatLng getMarkerPosition(OpenWeatherMapData openWeatherMapData) {
        return new LatLng(openWeatherMapData.getCoord().getLat(), openWeatherMapData.getCoord().getLon());
    }

    @Override
    public double getMarkerLatitude(OpenWeatherMapData openWeatherMapData) {
        return openWeatherMapData.getCoord().getLat();
    }

    @Override
    public double getMarkerLongitude(OpenWeatherMapData openWeatherMapData) {
        return openWeatherMapData.getCoord().getLon();
    }

    @Override
    protected MarkerOptions getMarkerOptions(OpenWeatherMapData openWeatherMapData) {
        return super.getMarkerOptions(openWeatherMapData).draggable(false).anchor(0.5F, 0.5F);
    }
}
