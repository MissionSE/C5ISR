package com.missionse.kestrelweather.reports.auxiliary;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.missionse.kestrelweather.KestrelWeatherActivity;
import com.missionse.kestrelweather.R;

import java.util.List;

/**
 * Provides a fragment used to display the auxiliary of a report.
 */
public class AuxiliaryDataFragment extends Fragment {
	private static final String TAG = AuxiliaryDataFragment.class.getSimpleName();
	private static final String REPORT_ID = "report_id";
	private static final int INVALID_REPORT_ID = -1;

	private KestrelWeatherActivity mActivity;
	private int mReportId = INVALID_REPORT_ID;
	private AuxiliaryDataAdapter mAuxiliaryDataAdapter;

	/**
	 * Default constructor.
	 */
	public AuxiliaryDataFragment() {
	}

	/**
	 * Creates a new auxiliary data fragment with a specified report id.
	 * @param reportId - The database ID associated with the report.
	 * @return A new instance of the fragment.
	 */
	public static AuxiliaryDataFragment newInstance(final int reportId) {
		AuxiliaryDataFragment fragment = new AuxiliaryDataFragment();

		Bundle bundle = new Bundle();
		bundle.putInt(REPORT_ID, reportId);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			mActivity = (KestrelWeatherActivity) activity;
		} catch (ClassCastException e) {
			Log.e(TAG, "Unable to cast activity.", e);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mReportId = getArguments().getInt(REPORT_ID);
		}

		if (mActivity != null) {
			List<AuxiliaryDataListItem> auxiliaryDataListItems = AuxiliaryDataListFactory.getListItems(
					mActivity, mReportId);
			mAuxiliaryDataAdapter = new AuxiliaryDataAdapter(
					mActivity, R.layout.fragment_report_detail_auxiliary_data_list_entry, auxiliaryDataListItems);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report_detail_auxiliary_data, container, false);
		if (view != null) {
			if (mAuxiliaryDataAdapter != null) {
				ListView auxiliaryDataList = (ListView) view.findViewById(R.id.report_detail_auxiliary_data_list);
				if (auxiliaryDataList != null) {
					auxiliaryDataList.setAdapter(mAuxiliaryDataAdapter);
					auxiliaryDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
							mAuxiliaryDataAdapter.getItem(position).onClick();
						}
					});
				}
			}
		}

		return view;
	}
}
