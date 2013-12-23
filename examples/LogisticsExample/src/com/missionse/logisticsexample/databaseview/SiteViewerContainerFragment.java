package com.missionse.logisticsexample.databaseview;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Site;

/**
 * Contains two side-by-side fragments, managing communication between the two.
 */
public class SiteViewerContainerFragment extends Fragment {

	private SiteListFragment mSiteList;
	private SiteDetailFragment mSiteDetail;
	private LocalDatabaseHelper mDatabaseHelper;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mDatabaseHelper = ((LogisticsExample) activity).getDatabaseHelper();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_container, parent, false);

		mSiteList = new SiteListFragment();
		mSiteList.setContainer(this);
		mSiteList.setDatabaseHelper(mDatabaseHelper);
		mSiteDetail = new SiteDetailFragment();

		getChildFragmentManager().beginTransaction().replace(R.id.database_list, mSiteList, "sitelist").commit();
		getChildFragmentManager().beginTransaction().replace(R.id.database_detail, mSiteDetail, "sitedetail").commit();

		return contentView;
	}

	/**
	 * Displays the details of a Site.
	 * @param site the site for which details should be displayed
	 */
	public void displaySite(final Site site) {
		mSiteDetail.displaySite(site);
	}
}
