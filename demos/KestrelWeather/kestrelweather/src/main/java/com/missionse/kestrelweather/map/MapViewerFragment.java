package com.missionse.kestrelweather.map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.reports.ReportAdapter;
import com.missionse.kestrelweather.reports.ReportDetailFragment;
import com.slidinglayer.SlidingLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fragment that displays a google map.
 */
public class MapViewerFragment extends MapFragment implements GoogleMap.OnMapClickListener {
	private static final String TAG = MapViewerFragment.class.getSimpleName();
	private GoogleMap mMap;
	private MapLoadedListener mMapLoadedListener;
	private OptionsMenuListener mOptionsMenuListener;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;
	private SlidingLayer mSlidingLayer;
	private ImageView mSlidingLayerToggle;
	private ReportAdapter mReportAdapter;
	private Activity mActivity;
	private Marker mCurrentMarker;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	/**
	 * Sets the listener that will receive a callback when the map is loaded.
	 * @param listener The listener that will receive the callback.
	 */
	public void setMapLoadedListener(final MapLoadedListener listener) {
		mMapLoadedListener = listener;
	}

	/**
	 * Sets the listener that will receive callbacks to handle the options menu.
	 * @param listener The listener that will receive the callbacks.
	 */
	public void setOptionsMenuListener(final OptionsMenuListener listener) {
		mOptionsMenuListener = listener;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (mActivity != null) {
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header,
					new ArrayList<Report>());
		}

		if (mOptionsMenuListener != null) {
			setHasOptionsMenu(true);
		}

		setUpMapIfNeeded();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_map, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);

		ListView reportList = (ListView) v.findViewById(R.id.map_report_list);
		if (reportList != null) {
			reportList.setAdapter(mReportAdapter);
			reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
					showReportDetail(mReportAdapter.getItem(position).getId());
				}
			});
		}

		mSlidingLayer = (SlidingLayer) v.findViewById(R.id.map_slidingLayer);
		mSlidingLayer.setCloseOnTapEnabled(true);
		mSlidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
			@Override
			public void onOpen() {
				mSlidingLayerToggle.setImageResource(R.drawable.ic_expand);
				if (mCurrentMarker != null) {
					centerMap(mCurrentMarker.getPosition(), true, null);
				}
			}

			@Override
			public void onClose() {
				mSlidingLayerToggle.setImageResource(R.drawable.ic_collapse);
				if (mCurrentMarker != null) {
					centerMap(mCurrentMarker.getPosition(), false, null);
				}
			}

			@Override
			public void onOpened() {

			}

			@Override
			public void onClosed() {

			}
		});
		mSlidingLayerToggle = (ImageView) v.findViewById(R.id.map_slidingLayer_toggle);

		layout.addView(mapView, 0);

		setUpMapIfNeeded();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		setUpMapIfNeeded();
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (mOptionsMenuListener != null) {
			mOptionsMenuListener.onCreateOptionsMenu(menu, inflater);
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		boolean selectionConsumed;
		if (mOptionsMenuListener != null) {
			selectionConsumed = mOptionsMenuListener.onOptionsItemSelected(item);
		} else {
			selectionConsumed = super.onOptionsItemSelected(item);
		}

		return selectionConsumed;
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		KestrelWeatherActivity activity = (KestrelWeatherActivity) getActivity();
		mMap.setOnMapClickListener(this);
		mMap.getUiSettings().setZoomControlsEnabled(false);

		if (activity != null) {
			mMarkersAdapter = new ObservationCalloutMarkersAdapter(activity, mMap, this);

			DatabaseAccessor databaseAccessor = activity.getDatabaseAccessor();
			ReportTable reportTable = databaseAccessor.getReportTable();
			List<Report> reports = reportTable.queryForAll();
			mMarkersAdapter.setData(reports);
		}

		if (mMapLoadedListener != null) {
			mMapLoadedListener.mapLoaded(mMap);
		}
	}

	public void showReportDetail(int reportId) {
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager != null) {
			Fragment reportDetailFragment = ReportDetailFragment.newInstance(reportId);
			fragmentManager.beginTransaction()
					.setCustomAnimations(
							R.animator.fade_in, R.animator.fade_out,
							R.animator.fade_in, R.animator.fade_out)
					.replace(R.id.content, reportDetailFragment, "report_detail")
					.addToBackStack("report_detail")
					.commit();
		}
	}

	private void centerMap(LatLng latLng, boolean reportPaneVisible, GoogleMap.CancelableCallback callback) {
		int xPadding;
		int yPadding;
		if (reportPaneVisible) {
			xPadding = getResources().getInteger(R.integer.map_padding_report_pane_visible_x);
			yPadding = getResources().getInteger(R.integer.map_padding_report_pane_visible_y);
		} else {
			xPadding = getResources().getInteger(R.integer.map_padding_report_pane_gone_x);
			yPadding = getResources().getInteger(R.integer.map_padding_report_pane_gone_y);
		}

		// calculate the new center of the map, taking into account optional
		// padding
		Projection projection = getMap().getProjection();
		Point point = projection.toScreenLocation(latLng);
		float density = getResources().getDisplayMetrics().density;
		point.x = point.x - (int) (xPadding * density);
		point.y = point.y - (int) (yPadding * density);

		mMap.animateCamera(CameraUpdateFactory.newLatLng(projection.fromScreenLocation(point)), callback);
	}

	/**
	 * Called when the activity has detected the user's press of the back key. The default implementation
	 * simply finishes the current activity, but if the map wants to trace backwards in user actions, do
	 * so instead.
	 *
	 * @return Return false to allow normal back pressed processing to proceed, true to consume it here.
	 */
	public boolean onBackPressed() {
		if (mCurrentMarker != null && mCurrentMarker.isInfoWindowShown()) {
			mCurrentMarker.hideInfoWindow();
			mSlidingLayer.closeLayer(true);
			mCurrentMarker = null;
			return true;
		}
		return false;
	}

	@Override
	public void onMapClick(LatLng latLng) {
		mCurrentMarker = null;
		if (mSlidingLayer.isOpened()) {
			mSlidingLayer.closeLayer(true);
		}
	}

	public boolean onClusterItemClick(Marker marker, Report report) {
		mCurrentMarker = marker;
		mReportAdapter.clear();
		mReportAdapter.add(report);
		mSlidingLayer.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.map_slidingLayer_list_single_entry_size);
		onMarkerClick(marker);
		return true;
	}

	public boolean onClusterClick(Marker marker, Cluster<Report> cluster) {
		mCurrentMarker = marker;
		mReportAdapter.clear();
		mReportAdapter.addAll(cluster.getItems());
		mSlidingLayer.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.map_report_pane_size);
		onMarkerClick(marker);
		return true;
	}

	private void onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		mSlidingLayer.openLayer(true);
	}
}
