package com.missionse.kestrelweather.map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fragment that displays a google map.
 */
public class MapViewerFragment extends MapFragment implements
		GoogleMap.OnMapClickListener,
		GoogleMap.OnMapLoadedCallback,
		GoogleMap.OnCameraChangeListener {
	private static final String TAG = MapViewerFragment.class.getSimpleName();
	private static final int REFRESH_DELAY = 5000;
	private GoogleMap mMap;
	private MapLoadedListener mMapLoadedListener;
	private OptionsMenuListener mOptionsMenuListener;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;
	private SlidingLayer mSlidingLayer;
	private ReportAdapter mReportAdapter;
	private KestrelWeatherActivity mActivity;
	private Marker mCurrentMarker;
	private CameraPosition mPreviousCameraPosition;
	private Handler mHandler = new Handler();
	private DateTime mLastSynced = DateTime.now();

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mActivity = (KestrelWeatherActivity) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
		mHandler.removeCallbacks(mRefreshMapTask);
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
		mSlidingLayer.setSlidingEnabled(false);
		mSlidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
			@Override
			public void onOpen() {
				if (mCurrentMarker != null) {
					centerMap(mCurrentMarker.getPosition(), null);
				}
			}

			@Override
			public void onClose() {

			}

			@Override
			public void onOpened() {

			}

			@Override
			public void onClosed() {

			}
		});

		layout.addView(mapView, 0);

		setUpMapIfNeeded();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		mHandler.post(mRefreshMapTask);
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
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setOnMapLoadedCallback(this);
		mMap.setOnMapClickListener(this);
		mMap.getUiSettings().setZoomControlsEnabled(false);

		if (mActivity != null) {
			mMarkersAdapter = new ObservationCalloutMarkersAdapter(mActivity, mMap, this);
		}
	}

	/**
	 * Show the report detail fragment.
	 * @param reportId The id of the report to display.
	 */
	public void showReportDetail(final int reportId) {
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager != null) {
			Fragment reportDetailFragment = ReportDetailFragment.newInstance(reportId);
			fragmentManager.beginTransaction()
					.setCustomAnimations(
							R.animator.fade_in, R.animator.fade_out,
							R.animator.fade_in, R.animator.fade_out)
					.add(R.id.content, reportDetailFragment, "report_detail")
					.hide(this)
					.addToBackStack("report_detail")
					.commit();
		}
	}

	private void centerMap(LatLng latLng, GoogleMap.CancelableCallback callback) {
		int xPadding = getResources().getInteger(R.integer.map_padding_x);
		int yPadding = getResources().getInteger(R.integer.map_padding_y);

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
	 * @return Return false to allow normal back pressed processing to proceed, true to consume it here.
	 */
	public boolean onBackPressed() {
		if (mCurrentMarker != null && mCurrentMarker.isInfoWindowShown()) {
			mCurrentMarker.hideInfoWindow();
			mCurrentMarker = null;
			mSlidingLayer.closeLayer(true);
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

	/**
	 * Handles the clicking of a cluster item.
	 * @param marker The marker clicked.
	 * @param report The report associated with the cluster item.
	 * @return Whether the click was processed.
	 */
	public boolean onClusterItemClick(final Marker marker, final Report report) {
		if (mCurrentMarker != null) {
			centerMap(marker.getPosition(), null);
		}
		mCurrentMarker = marker;
		mReportAdapter.clear();
		mReportAdapter.add(report);
		onMarkerClick(marker);
		return true;
	}


	/**
	 * Handles the clicking of a cluster.
	 * @param marker The marker clicked.
	 * @param cluster The cluster of reports associated with the marker.
	 * @return Whether the click was processed.
	 */
	public boolean onClusterClick(final Marker marker, final Cluster<Report> cluster) {
		if (mCurrentMarker != null) {
			centerMap(marker.getPosition(), null);
		}
		mCurrentMarker = marker;
		mReportAdapter.clear();
		mReportAdapter.addAll(cluster.getItems());
		onMarkerClick(marker);
		return true;
	}

	private void onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		mSlidingLayer.openLayer(true);
	}

	private void loadReports() {
		DatabaseAccessor databaseAccessor = mActivity.getDatabaseAccessor();
		ReportTable reportTable = databaseAccessor.getReportTable();
		List<Report> reports = reportTable.queryForAll();
		mMarkersAdapter.setData(reports);
	}

	@Override
	public void onMapLoaded() {
		if (mMapLoadedListener != null) {
			mMapLoadedListener.mapLoaded(mMap);
		}
		Location location = mActivity.getLastLocation();
		if (location != null) {
			mMap.setMyLocationEnabled(true);
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 5.0f),
					new GoogleMap.CancelableCallback() {
						@Override
						public void onFinish() {
							loadReports();
						}

						@Override
						public void onCancel() {
							loadReports();
						}
					}
			);
		}
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		// Don't close sliding layer if the map has just been panned/tilted/rotated.
		CameraPosition position = mMap.getCameraPosition();
		if (mPreviousCameraPosition != null && mPreviousCameraPosition.zoom == position.zoom) {
			return;
		}
		mPreviousCameraPosition = mMap.getCameraPosition();

		if (mCurrentMarker != null && !mCurrentMarker.isInfoWindowShown() && mSlidingLayer.isOpened()) {
			mCurrentMarker = null;
			mSlidingLayer.closeLayer(true);
		}
	}

	private Runnable mRefreshMapTask = new Runnable() {
		@Override
		public void run() {
			DateTime dbLastSynced = mActivity.getDatabaseAccessor().getLastSyncedTime();
			if (mLastSynced.isBefore(dbLastSynced)) {
				mLastSynced = dbLastSynced;
				loadReports();
			}

			if (mCurrentMarker == null || !mCurrentMarker.isInfoWindowShown()) {
				mSlidingLayer.closeLayer(true);
			}
			mHandler.postDelayed(this, REFRESH_DELAY);
		}
	};
}
