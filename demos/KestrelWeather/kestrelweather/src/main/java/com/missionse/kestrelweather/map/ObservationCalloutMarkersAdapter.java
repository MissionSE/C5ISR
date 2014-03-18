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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.graphics.drawable.TextDrawable;
import com.missionse.kestrelweather.util.BitmapHelper;
import com.missionse.kestrelweather.util.ResourcesHelper;
import com.missionse.kestrelweather.views.ObservationCallout;
import com.missionse.mapviewer.adapters.DataMarkersAdapter;
import com.missionse.mapviewer.clustering.DefaultClusterRenderer;

import java.util.Collection;

/**
 * This class is an adapter for display marker data in a marker info window.
 */
public class ObservationCalloutMarkersAdapter extends DataMarkersAdapter<Report> {
	private static final String TAG = ObservationCalloutMarkersAdapter.class.getSimpleName();
	private static final int DRAWABLE_COUNT = 3;
	private static final float CLUSTER_ITEM_SHAPE_INSET_MULTIPLIER = 5.0F;
	private static final float CLUSTER_SHAPE_INSET_MULTIPLIER = 10.0F;
	private static final float CLUSTER_TEXT_INSET_MULTIPLIER = 6.0F;
	private static final float MARKER_ANCHOR = 0.5F;
	private ReportRenderer mRenderer;
	private MapViewerFragment mFragment;

	/**
	 * Constructor.
	 * @param context the current context
	 * @param map the google map object
	 * @param fragment The map viewer fragment.
	 */
	public ObservationCalloutMarkersAdapter(Context context, GoogleMap map, MapViewerFragment fragment) {
		super(context, map, R.layout.map_observation_callout);
		mFragment = fragment;

		setFullInfoWindowEnabled(true);
	}

	@Override
	public boolean onClusterItemClick(Report report) {
		Marker marker = ((ReportRenderer) getRenderer()).getMarker(report);
		return mFragment.onClusterItemClick(marker, report);
	}

	@Override
	public boolean onClusterClick(Cluster<Report> cluster) {
		Marker marker = ((ReportRenderer) getRenderer()).getMarker(cluster);
		return mFragment.onClusterClick(marker, cluster);
	}

	@Override
	public void onClusterItemInfoWindowClick(Report report) {
		mFragment.showReportDetail(report.getId());
	}

	@Override
	public void onClusterInfoWindowClick(Cluster<Report> cluster) {
		mFragment.showReportDetail(getLatestReport(cluster.getItems()).getId());
	}

	@Override
	public BitmapDescriptor createClusterIcon(int index, Cluster<Report> cluster) {
		Drawable shadowDrawable = getContext().getResources().getDrawable(R.drawable.bg_cluster);

		ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
		shapeDrawable.getPaint().setColor(ResourcesHelper.getTemperatureColorByIndex(getContext(), index));

		TextDrawable textDrawable = new TextDrawable(getContext().getResources(), Integer.toString(cluster.getSize()));

		Drawable[] arrayOfDrawable = new Drawable[DRAWABLE_COUNT];
		arrayOfDrawable[0] = shadowDrawable;
		arrayOfDrawable[1] = shapeDrawable;
		arrayOfDrawable[2] = textDrawable;
		LayerDrawable layerDrawable = new LayerDrawable(arrayOfDrawable);
		float density = getContext().getResources().getDisplayMetrics().density;
		int shapeInset = (int) (density * CLUSTER_SHAPE_INSET_MULTIPLIER);
		layerDrawable.setLayerInset(1, shapeInset, shapeInset, shapeInset, shapeInset);
		int textInset = (int) (density * CLUSTER_TEXT_INSET_MULTIPLIER);
		layerDrawable.setLayerInset(2, 0, textInset, 0, -textInset);

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
		int shapeInset = (int) (CLUSTER_ITEM_SHAPE_INSET_MULTIPLIER * getContext().getResources().getDisplayMetrics().density);
		layerDrawable.setLayerInset(1, shapeInset, shapeInset, shapeInset, shapeInset);

		return BitmapDescriptorFactory.fromBitmap(BitmapHelper.getDrawableBitmap(layerDrawable));
	}

	@Override
	public int getClusterIconType(Cluster<Report> cluster) {
		return ResourcesHelper.getTemperatureIndex(getLatestReport(cluster.getItems()).getKestrelWeather().getTemperature());
	}

	@Override
	public int getClusterItemIconType(Report report) {
		return ResourcesHelper.getTemperatureIndex(report.getKestrelWeather().getTemperature());
	}

	@Override
	protected View getInfoView(final Marker marker, Report report, View currentView) {
		View view;
		view = super.getInfoView(marker, report, currentView);
		((ObservationCallout) view).setData(report);
		if (marker.isInfoWindowShown()) {
			marker.showInfoWindow();
		}
		return view;
	}

	@Override
	protected View getInfoView(final Marker marker, Cluster<Report> cluster, View currentView) {
		View view;
		view = super.getInfoView(marker, cluster, currentView);
		((ObservationCallout) view).setData(getLatestReport(cluster.getItems()));
		if (marker.isInfoWindowShown()) {
			marker.showInfoWindow();
		}
		return view;
	}

	@Override
	public ClusterRenderer<Report> getRenderer() {
		if (mRenderer == null) {
			mRenderer = new ReportRenderer();
			mRenderer.setClusterItemInfoWindowAdapter(this);
			mRenderer.setClusterInfoWindowAdapter(this);
		}
		return mRenderer;
	}

	private Report getLatestReport(Collection<Report> reports) {
		Report latestReport = null;
		for (Report report : reports) {
			if (latestReport == null || report.getUpdateAt().isAfter(latestReport.getUpdateAt())) {
				latestReport = report;
			}
		}
		return latestReport;
	}

	/**
	 * Renderer for marker icons.
	 */
	private class ReportRenderer extends DefaultClusterRenderer<Report> implements GoogleMap.OnCameraChangeListener {

		public ReportRenderer() {
			super(getContext(), getMap(), getClusterManager());
		}

		@Override
		protected boolean shouldRenderAsCluster(Cluster<Report> cluster) {
			return cluster.getSize() > 1;
		}

		@Override
		protected void onBeforeClusterItemRendered(Report observation, MarkerOptions markerOptions) {
			markerOptions.icon(getClusterItemIcon(observation)).draggable(false).anchor(MARKER_ANCHOR, MARKER_ANCHOR);
		}

		@Override
		protected void onBeforeClusterRendered(Cluster<Report> cluster, MarkerOptions markerOptions) {
			markerOptions.icon(getClusterIcon(cluster)).draggable(false).anchor(MARKER_ANCHOR, MARKER_ANCHOR);
		}

		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			mFragment.onCameraChange(cameraPosition);
		}
	}

}
