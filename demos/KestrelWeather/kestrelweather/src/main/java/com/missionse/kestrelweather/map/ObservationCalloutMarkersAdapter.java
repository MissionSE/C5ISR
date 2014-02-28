package com.missionse.kestrelweather.map;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.util.BitmapHelper;
import com.missionse.kestrelweather.database.util.ResourcesHelper;
import com.missionse.kestrelweather.graphics.drawable.TextDrawable;
import com.missionse.kestrelweather.views.ObservationCallout;
import com.missionse.mapviewer.adapters.DataMarkersAdapter;
import com.missionse.mapviewer.clustering.DefaultClusterRenderer;

import org.joda.time.DateTime;

import java.util.List;

public class ObservationCalloutMarkersAdapter extends DataMarkersAdapter<List<ObservationData>, ObservationData, String> {
    private static final String TAG = ObservationCalloutMarkersAdapter.class.getSimpleName();
    private ObservationData mCurrentObservation;
    private Cluster<ObservationData> mCurrentCluster;

    public ObservationCalloutMarkersAdapter(Context context, GoogleMap map) {
        super(context, map, R.layout.map_observation_callout);

        setFullInfoWindowEnabled(true);
    }

    @Override
    public BitmapDescriptor createClusterIcon(int index, Cluster<ObservationData> cluster) {
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

        return BitmapDescriptorFactory.fromBitmap(BitmapHelper.getDrawableBitmap(layerDrawable));
    }

    @Override
    public BitmapDescriptor createClusterItemIcon(int index) {
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
    public int getClusterIconType(Cluster<ObservationData> cluster) {
        int iconType = 0;
        for (ObservationData observation : cluster.getItems()) {
            iconType = ResourcesHelper.getTemperatureIndex(observation.getTemp());
            break;
        }
        return iconType;
    }

    @Override
    public int getClusterItemIconType(ObservationData observation) {
        return ResourcesHelper.getTemperatureIndex(observation.getTemp());
    }

    @Override
    protected View getInfoView(final Marker marker, ObservationData observation, View paramView) {
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
    protected View getInfoView(final Marker marker, Cluster<ObservationData> cluster, View paramView) {
        View view;
        if (mCurrentCluster != cluster) {
            view = super.getInfoView(marker, cluster, paramView);
            DateTime now = DateTime.now();
            for (ObservationData observation : cluster.getItems()) {
                ((ObservationCallout) view).setData(observation);

            }
            if (marker.isInfoWindowShown()) {
                marker.showInfoWindow();
            }
            this.mCurrentCluster = cluster;
        } else {
            view = paramView;
        }
        return view;
    }

    @Override
    public ClusterRenderer<ObservationData> getRenderer() {
        ObservationDataRenderer renderer = new ObservationDataRenderer();
        renderer.setClusterItemInfoWindowAdapter(this);
        renderer.setClusterInfoWindowAdapter(this);
        return renderer;
    }

    private class ObservationDataRenderer extends DefaultClusterRenderer<ObservationData> {

        public ObservationDataRenderer() {
            super(getContext(), getMap(), getClusterManager());
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<ObservationData> cluster) {
            return cluster.getSize() > 1;
        }

        @Override
        protected void onBeforeClusterItemRendered(ObservationData observation, MarkerOptions markerOptions) {
            markerOptions.icon(getClusterItemIcon(observation)).draggable(false).anchor(0.5F, 0.5F);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<ObservationData> cluster, MarkerOptions markerOptions) {
            markerOptions.icon(getClusterIcon(cluster)).draggable(false).anchor(0.5F, 0.5F);
        }
    }

}
