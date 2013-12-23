package com.missionse.logisticsexample.dialog;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Site;

/**
 * This will be a dialog that a user can create and modify a Site object.
 */
public class SiteDialogFragment extends DialogFragment {

	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String SITE_NAME = "site_name";
	private static final String SITE_ID = "site_id";
	private static final String SITE_PARENT_ID = "site_parent_id";
	private static final String SITE_MODIFY = "site_modify";

	private EditText mEtSiteName;
	private TextView mTvLatitude;
	private TextView mTvLongitude;
	private Spinner mSpnParent;
	private Activity mActivity;

	private String mSiteName = "";
	private double mLatitude = 0.0d;
	private double mLongitude = 0.0d;
	private int mSiteId = 0;
	private int mParentId = 0;
	private boolean mModify = false;
	/**
	 * Creates a new SiteDialogFragment, packing the latitude and longitude.
	 *
	 * @param latitude
	 *            The latitude of the location being created, in degrees.
	 * @param longitude
	 *            The longitude of the location being created, in degrees.
	 * @return A new CreateLocationDialogFragment.
	 */
	public static SiteDialogFragment newInstance(final double latitude,
			final double longitude) {
		SiteDialogFragment fragment = new SiteDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putDouble(LATITUDE, latitude);
		bundle.putDouble(LONGITUDE, longitude);
		bundle.putBoolean(SITE_MODIFY, false);
		fragment.setArguments(bundle);

		return fragment;
	}

	/**
	 * Creates a new SiteDailogFragment, used to edit the site parameter.
	 *
	 * @param site
	 *            The site to be edited.
	 * @return A new SiteDialogFragment
	 */
	public static SiteDialogFragment newInstance(final Site site) {
		SiteDialogFragment fragment = new SiteDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putDouble(LATITUDE, site.getLatitude());
		bundle.putDouble(LONGITUDE, site.getLongitude());
		bundle.putString(SITE_NAME, site.getName());
		bundle.putInt(SITE_ID, site.getId());
		bundle.putInt(SITE_PARENT_ID, site.getParentId());
		bundle.putBoolean(SITE_MODIFY, true);
		fragment.setArguments(bundle);

		return fragment;
	}

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
		updateState(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		updateState(savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_site_dialog, container);
		setupSiteName(view);
		setupLatLon(view);
		setupSpinner(view);
		setupButtons(view);
		setupTitle();

		return view;
	}

	private void setupTitle() {
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		if (mModify) {
			getDialog().setTitle(R.string.title_site_modify);
		} else {
			getDialog().setTitle(R.string.title_site_new);
		}
	}

	private void updateState(final Bundle state) {
		if (state != null) {
			if (state.getBoolean(SITE_MODIFY)) {
				mModify = true;
				mSiteName = state.getString(SITE_NAME);
				mLatitude = state.getDouble(LATITUDE);
				mLongitude = state.getDouble(LONGITUDE);
				mSiteId = state.getInt(SITE_ID);
				mParentId = state.getInt(SITE_PARENT_ID);
			} else {
				mModify = false;
				mLatitude = state.getDouble(LATITUDE);
				mLongitude = state.getDouble(LONGITUDE);
			}
		}
	}

	private void setupSiteName(final View root) {
		mEtSiteName = (EditText) root.findViewById(R.id.site_name);
		if (mModify) {
			mEtSiteName.setText(mSiteName);
		}
	}

	private void setupLatLon(final View root) {
		mTvLatitude = (TextView) root.findViewById(R.id.site_latitude);
		mTvLongitude = (TextView) root.findViewById(R.id.site_longitude);

		String lat = getResources().getString(R.string.latitude);
		String lng = getResources().getString(R.string.longitude);

		mTvLatitude.setText(lat + ": " + mLatitude);
		mTvLongitude.setText(lng + ": " + mLongitude);
	}

	private void setupSpinner(final View root) {
		mSpnParent = (Spinner) root.findViewById(R.id.parent_name_spinner);

		List<Site> sites = generateSiteNames();

		mSpnParent.setAdapter(new SiteArrayAdapter(mActivity, sites));

		if (mParentId > 0) {
			Site siteIndx = null;
			for (Site site : sites) {
				if (site.getParentId() == mParentId) {
					siteIndx = site;
				}
			}
			mSpnParent.setSelection(sites.indexOf(siteIndx));
		}
	}

	private List<Site> generateSiteNames() {
		List<Site> sites = new LinkedList<Site>();
		try {
			LocalDatabaseHelper database = ((LogisticsExample) mActivity).getDatabaseHelper();
			sites.addAll(database.getSites());
		} catch (ClassCastException exception) {
			exception.printStackTrace();
		}
		return sites;
	}

	private void setupButtons(final View root) {
		Button accept = (Button) root.findViewById(R.id.ok_button);
		Button cancel = (Button) root.findViewById(R.id.cancel_button);

		if (mModify) {
			accept.setText(R.string.update);
		} else {
			accept.setText(R.string.create);
		}

		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				onAccept();
			}

		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				SiteDialogFragment.this.dismiss();
			}
		});
	}

	/**
	 * Adapter used by the spinner to control the list of sites.
	 */
	private class SiteArrayAdapter extends ArrayAdapter<Site> {

		private List<Site> mSites;
		private Activity mActivity;

		public SiteArrayAdapter(final Activity activity, final List<Site> sites) {
			super(activity, android.R.layout.simple_list_item_1, sites);
			mSites = sites;
			mActivity = activity;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			TextView textview = (TextView) super
					.getView(position, convertView, parent);

			if (textview == null) {
				textview = new TextView(mActivity);
			}
			textview.setTextColor(Color.BLACK);
			textview.setText(mSites.get(position).getName());
			return textview;
		}

		@Override
		public Site getItem(final int position) {
			return mSites.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = mActivity.getLayoutInflater();
				view = inflater.inflate(R.layout.simple_site_list_entry, null);
			}

			TextView lbl = (TextView) view.findViewById(R.id.entry_site_name);
			lbl.setTextColor(Color.BLACK);
			lbl.setText(mSites.get(position).getName());

			return convertView;
		}
	}

	private void onAccept() {
		mSiteName = mEtSiteName.getText().toString().trim();
		mParentId = ((Site) mSpnParent.getSelectedItem()).getId();

		Site site = new Site();
		site.setId(mSiteId);
		site.setLatitude(mLatitude);
		site.setLongitude(mLongitude);
		site.setName(mSiteName);
		site.setParentId(mParentId);

		LocalDatabaseHelper databaseHelper;
		try {
			databaseHelper = ((LogisticsExample) mActivity).getDatabaseHelper();

			if (mModify) {
				// TODO: update call
			} else {
				// TODO: create call
			}
		} catch (ClassCastException exception) {
			Toast.makeText(getActivity(), "Unable to create/update site",
					Toast.LENGTH_SHORT).show();
		}
	}

}
