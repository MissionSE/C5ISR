package com.missionse.logisticsexample.databaseview.site;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.R;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.Site;

/**
 * Manages several views which either prompt the user to select from a list for details, or display details about a
 * given Site.
 */
public class SiteDetailFragment extends Fragment {

	private TextView mEntryTitleView;
	private TextView mEntryTitleIdView;

	private TextView mEntryContentBasicView;
	private TextView mEntryContentParentView;
	private TextView mEntryContentInventoryView;
	private TextView mEntryContentOrdersView;

	private TextView mNothingSelectedView;

	private LinearLayout mEntryDetailContent;

	/**
	 * Displays the provided site, creating text content to represent the site.
	 * @param site the site to display
	 */
	public void displaySite(final Site site) {
		LocalDatabaseHelper databaseHelper = ((LogisticsExample) getActivity()).getDatabaseHelper();

		List<Order> orders = databaseHelper.getOrders(site);
		List<InventoryItem> supplies = databaseHelper.getInventoryItems(site);

		setTitleText(site.getName());
		setTitleId("" + site.getId());

		String basicContent = "";
		basicContent += "Latitude: " + site.getLatitude() + "\n";
		basicContent += "Longitude: " + site.getLongitude() + "\n";
		basicContent += "# of Orders: " + orders.size() + "\n";
		basicContent += "# of Unique Supplies: " + supplies.size() + "\n";

		setBasicContent(basicContent.trim());

		Site parentSite = databaseHelper.getSiteParent(site);
		String parentContent = "";
		if (parentSite != null) {
			parentContent += "Name: " + parentSite.getName() + "\n";
			parentContent += "ID: " + parentSite.getId() + "\n";
		} else {
			parentContent += "Name: N/A\n";
			parentContent += "ID: N/A\n";
		}
		setParentContent(parentContent.trim());

		String inventoryContent = "";
		for (InventoryItem item : supplies) {
			inventoryContent += "" + databaseHelper.getItemName(item.getNameId()) + " [ ID: " + item.getId() + "]:\n";
			inventoryContent += "\tCurrent/Max: " + item.getQuantity() + "/" + item.getMaximum() + "\n";
		}
		setInventoryContent(inventoryContent.trim());

		String orderContent = "";
		for (Order order : orders) {
			orderContent += order.toString() + " [ ID: " + order.getId() + "] [" + order.getTimeStamp() + "]:\n";
			orderContent += "\tStatus: " + order.getStatusId() + "\n";
			orderContent += "\tSeverity: " + order.getSeverityId() + "\n";
		}
		setOrdersContent(orderContent.trim());

		showContentViews();
	}

	private void setTitleText(final CharSequence titleText) {
		mEntryTitleView.setText(titleText);
	}

	private void setTitleId(final CharSequence idText) {
		mEntryTitleIdView.setText(idText);
	}

	private void setBasicContent(final CharSequence basicContentText) {
		mEntryContentBasicView.setText(basicContentText);
	}

	private void setParentContent(final CharSequence parentContentText) {
		mEntryContentParentView.setText(parentContentText);
	}

	private void setInventoryContent(final CharSequence inventoryContentText) {
		mEntryContentInventoryView.setText(inventoryContentText);
	}

	private void setOrdersContent(final CharSequence orderContentText) {
		mEntryContentOrdersView.setText(orderContentText);
	}

	/**
	 * Displays the view for when nothing has been selected (i.e., no details are being shown).
	 */
	public void showNothingSelectedView() {
		mEntryDetailContent.setVisibility(View.GONE);
		mNothingSelectedView.setVisibility(View.VISIBLE);
	}

	/**
	 * Displays the views associated with displaying details.
	 */
	public void showContentViews() {
		mEntryDetailContent.setVisibility(View.VISIBLE);
		mNothingSelectedView.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, final Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_site_database_detail, parent, false);

		mEntryTitleView = (TextView) contentView.findViewById(R.id.entry_detail_title);
		mEntryTitleIdView = (TextView) contentView.findViewById(R.id.entry_detail_id);

		TextView entryContentBasicHeader = (TextView) contentView.findViewById(R.id.entry_detail_basic_header);
		entryContentBasicHeader.setText(R.string.entry_detail_basic_header);
		mEntryContentBasicView = (TextView) contentView.findViewById(R.id.entry_detail_basic_info);

		TextView entryContentParentHeader = (TextView) contentView.findViewById(R.id.entry_detail_parent_header);
		entryContentParentHeader.setText(R.string.entry_detail_parent_header);
		mEntryContentParentView = (TextView) contentView.findViewById(R.id.entry_detail_parent_info);

		TextView entryContentInventoryHeader = (TextView) contentView.findViewById(R.id.entry_detail_inventory_header);
		entryContentInventoryHeader.setText(R.string.entry_detail_inventory_header);
		mEntryContentInventoryView = (TextView) contentView.findViewById(R.id.entry_detail_inventory);

		TextView entryContentOrdersHeader = (TextView) contentView.findViewById(R.id.entry_detail_orders_header);
		entryContentOrdersHeader.setText(R.string.entry_detail_orders_header);
		mEntryContentOrdersView = (TextView) contentView.findViewById(R.id.entry_detail_orders);

		mNothingSelectedView = (TextView) contentView.findViewById(R.id.nothing_selected);

		mEntryDetailContent = (LinearLayout) contentView.findViewById(R.id.entry_detail_container);

		return contentView;
	}
}
