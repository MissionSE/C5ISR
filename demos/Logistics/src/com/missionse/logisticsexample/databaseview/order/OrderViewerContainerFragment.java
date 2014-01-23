package com.missionse.logisticsexample.databaseview.order;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.Order;

/**
 * Contains two side-by-side fragments, managing communication between the two.
 */
public class OrderViewerContainerFragment extends Fragment {

	private OrderListFragment mSiteList;
	private LocalDatabaseHelper mDatabaseHelper;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		mDatabaseHelper = ((LogisticsExample) activity).getDatabaseHelper();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_database_container, parent, false);

		mSiteList = new OrderListFragment();
		mSiteList.setContainer(this);
		mSiteList.setDatabaseHelper(mDatabaseHelper);

		getChildFragmentManager().beginTransaction().replace(R.id.database_list, mSiteList, "orderlist").commit();
		getChildFragmentManager().beginTransaction().replace(R.id.database_detail, new Fragment(), "orderdetail")
				.commit();

		return contentView;
	}

	/**
	 * Displays the details of an Order.
	 * @param order the order for which details should be displayed
	 */
	public void displayOrder(final Order order) {
		//mSiteDetail.displaySite(site);
	}
}
