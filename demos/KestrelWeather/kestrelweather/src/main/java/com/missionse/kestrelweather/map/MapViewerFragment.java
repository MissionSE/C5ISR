package com.missionse.kestrelweather.map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;
import com.missionse.kestrelweather.database.DatabaseAccessor;
import com.missionse.kestrelweather.database.model.tables.Report;
import com.missionse.kestrelweather.database.model.tables.manipulators.ReportTable;
import com.missionse.kestrelweather.reports.ReportAdapter;
import com.missionse.kestrelweather.reports.ReportDetailFragment;
import com.slidinglayer.SlidingLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides a fragment that displays a google map.
 */
public class MapViewerFragment extends MapFragment {
	private static final String TAG = MapViewerFragment.class.getSimpleName();
	private GoogleMap mMap;
	private MapLoadedListener mMapLoadedListener;
	private ObservationCalloutMarkersAdapter mMarkersAdapter;
	private SlidingLayer mSlidingLayer;
	private ImageView mSlidingLayerToggle;
	private ReportAdapter mReportAdapter;
	private Activity mActivity;

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

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (mActivity != null) {
			mReportAdapter = new ReportAdapter(mActivity, R.layout.fragment_report_detail_header,
					new ArrayList<Report>());
		}

		setUpMapIfNeeded();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mapView = super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.fragment_map, container, false);
		FrameLayout layout = (FrameLayout) v.findViewById(R.id.map_container);

		ListView clusterReportList = (ListView) v.findViewById(R.id.map_cluster_report_list);
		if (clusterReportList != null) {
			clusterReportList.setAdapter(mReportAdapter);
			clusterReportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
				updateMapPadding(true);
				mSlidingLayerToggle.setImageResource(R.drawable.ic_expand);
			}

			@Override
			public void onClose() {
				updateMapPadding(false);
				mSlidingLayerToggle.setImageResource(R.drawable.ic_collapse);
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
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				mSlidingLayer.setOffsetWidth(0);
				showDetailPane(false);
			}
		});

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

	public void showClusterReportList(Collection<Report> reports) {
		showDetailPane(true);
		mReportAdapter.clear();
		mReportAdapter.addAll(reports);
	}

	public void showReportDetail(int reportId) {
		showDetailPane(false);
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

	/**
	 * Sets the listener that will receive a callback when the map is loaded.
	 *
	 * @param listener The listener that will receive the callback.
	 */
	public void setMapLoadedListener(final MapLoadedListener listener) {
		mMapLoadedListener = listener;
	}

	public boolean showDetailPane(boolean show) {
		if (mSlidingLayer.isOpened() && !show) {
			mSlidingLayer.closeLayer(true);
		} else if (!mSlidingLayer.isOpened() && show) {
			mSlidingLayer.openLayer(true);
		} else {
			return false;
		}
		updateMapPadding(show);
		return true;
	}

	private void updateMapPadding(boolean detailPaneShown) {
		float yPixels;
		if (detailPaneShown) {
			yPixels = 150;
		} else {
			yPixels = -150;
		}

		mMap.animateCamera(CameraUpdateFactory.scrollBy(0, yPixels));
	}
}
