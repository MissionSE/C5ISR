package com.missionse.logisticsexample.dialog;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
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
	private static final String SITE_SHORTNAME = "site_shortname";

	private EditText mEtSiteName;
	private EditText mEtShortName;
	private TextView mTvLatitude;
	private TextView mTvLongitude;
	private Spinner mSpnParent;
	private Activity mActivity;

	private String mSiteName = "";
	private String mShortName = "";
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
		bundle.putString(SITE_SHORTNAME, site.getShortName());
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
		updateState(getArguments());
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		updateState(getArguments());
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
		mEtShortName = (EditText) root.findViewById(R.id.site_shortname);
		if (mModify) {
			mEtSiteName.setText(mSiteName);
			mEtShortName.setText(mShortName);
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
		addEmptySite(sites);
		mSpnParent.setAdapter(new ArrayAdapter<Site>(mActivity, android.R.layout.simple_list_item_1, sites));

		if (mParentId > 0) {
			Site siteIndx = null;
			for (Site site : sites) {
				if (site.getParentId() == mParentId) {
					siteIndx = site;
				}
			}
			mSpnParent.setSelection(sites.indexOf(siteIndx));
		} else {
			mSpnParent.setSelection(sites.size() - 1);
		}
	}

	private void addEmptySite(List<Site> sites) {
		Site site = new Site();
		site.setId(0);
		site.setLatitude(0.0);
		site.setLongitude(0.0);
		site.setParentId(0);
		site.setName("NONE");
		sites.add(site);
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
				SiteDialogFragment.this.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				SiteDialogFragment.this.dismiss();
			}
		});
	}

	private void onAccept() {
		mSiteName = mEtSiteName.getText().toString().trim();
		mShortName = mEtShortName.getText().toString().trim();
		mParentId = ((Site) mSpnParent.getSelectedItem()).getId();

		Site site = new Site();
		site.setId(mSiteId);
		site.setLatitude(mLatitude);
		site.setLongitude(mLongitude);
		site.setName(mSiteName);
		site.setParentId(mParentId);
		site.setShortName(mShortName);

		LocalDatabaseHelper databaseHelper;
		try {
			databaseHelper = ((LogisticsExample) mActivity).getDatabaseHelper();

			if (mModify) {
				// TODO: Not yet Implemented yet.
				Toast.makeText(mActivity, "Unable to modify Site", Toast.LENGTH_SHORT).show();
			} else {
				databaseHelper.create(site);
			}
		} catch (ClassCastException exception) {
			Toast.makeText(getActivity(), "Unable to create/update site",
					Toast.LENGTH_SHORT).show();
		}
	}

}
