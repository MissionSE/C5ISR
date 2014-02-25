package com.missionse.mapsexample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.missionse.mapsexample.model.WeatherObservation;
import com.missionse.mapsexample.utils.BitmapHelper;
import com.missionse.mapsexample.utils.ResourcesHelper;
import com.missionse.mapviewer.adapters.DataMarkersAdapter;

import java.util.List;

public class WeatherMarkersAdapter extends DataMarkersAdapter<List<WeatherObservation>, WeatherObservation, String> {
    private static final String TAG = WeatherMarkersAdapter.class.getSimpleName();
    private WeatherObservation mCurrentObservation;

    public WeatherMarkersAdapter(Context context, GoogleMap map) {
        super(context, map, R.layout.observation_map_callout);

        setFullInfoWindowEnabled(true);
    }

    @Override
    public BitmapDescriptor createClusterIcon(int index, Cluster<WeatherObservation> cluster) {
        Drawable shadowDrawable = getContext().getResources().getDrawable(R.drawable.bg_cluster);

        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(ResourcesHelper.getTemperatureColorByIndex(getContext(), index));

        TextDrawable textDrawable = new TextDrawable(getContext().getResources(), Integer.toString(cluster.getSize()));

        Drawable[] arrayOfDrawable = new Drawable[3];
        arrayOfDrawable[0] = shadowDrawable;
        arrayOfDrawable[1] = shapeDrawable;
        arrayOfDrawable[2] = textDrawable;
        LayerDrawable layerDrawable = new LayerDrawable(arrayOfDrawable);
        int i = (int) (10.0F * getContext().getResources().getDisplayMetrics().density);
        layerDrawable.setLayerInset(1, i, i, i, i);
        layerDrawable.setLayerInset(2, 0, 13, 0, -13);

        int j = (int) (getContext().getResources().getDisplayMetrics().density);

        return BitmapDescriptorFactory.fromBitmap(BitmapHelper.getDrawableBitmap(layerDrawable));
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
    public int getClusterIconType(Cluster<WeatherObservation> cluster) {
        int iconType = 0;
        for (WeatherObservation observation : cluster.getItems()) {
            iconType = ResourcesHelper.getTemperatureIndex(observation.getData().getMain().getTemp());
            break;
        }
        return iconType;
    }

    @Override
    public int getIconType(WeatherObservation observation) {
        return ResourcesHelper.getTemperatureIndex(observation.getData().getMain().getTemp());
    }

    @Override
    protected View getInfoView(final Marker marker, WeatherObservation observation, View paramView) {
        View view;
        if (this.mCurrentObservation != observation) {
            view = super.getInfoView(marker, observation, paramView);
            ((ObservationCallout) view).setData(observation);
            if (marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
            this.mCurrentObservation = observation;
        } else {
            view = paramView;
        }
        return view;
    }

    @Override
    public WeatherObservation getMarkerData(int index) {
        return getData().get(index);
    }

    @Override
    public String getMarkerKey(WeatherObservation observation) {
        return Integer.toString(observation.getData().getId());
    }

    @Override
    public LatLng getMarkerPosition(WeatherObservation observation) {
        return observation.getPosition();
    }

    @Override
    public double getMarkerLatitude(WeatherObservation observation) {
        return observation.getData().getCoord().getLat();
    }

    @Override
    public double getMarkerLongitude(WeatherObservation observation) {
        return observation.getData().getCoord().getLon();
    }

    @Override
    protected MarkerOptions getMarkerOptions(WeatherObservation observation) {
        return super.getMarkerOptions(observation).draggable(false).anchor(0.5F, 0.5F);
    }

    @Override
    public ClusterRenderer<WeatherObservation> getRenderer() {
        return new WeatherObservationRenderer();
    }

    private class WeatherObservationRenderer extends DefaultClusterRenderer<WeatherObservation> {

        public WeatherObservationRenderer() {
            super(getContext(), getMap(), getClusterManager());
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<WeatherObservation> cluster) {
            return cluster.getSize() > 1;
        }

        @Override
        protected void onBeforeClusterItemRendered(WeatherObservation observation, MarkerOptions markerOptions) {
            markerOptions.icon(getMarkerIcon(observation));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<WeatherObservation> cluster, MarkerOptions markerOptions) {
            markerOptions.icon(getClusterIcon(cluster));
        }
    }

}
