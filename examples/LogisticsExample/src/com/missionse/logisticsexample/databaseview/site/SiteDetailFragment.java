package com.missionse.logisticsexample.databaseview.site;

import java.util.List;

import com.missionse.logisticsexample.LogisticsExample;
import com.missionse.logisticsexample.database.LocalDatabaseHelper;
import com.missionse.logisticsexample.databaseview.DatabaseEntryDetailFragment;
import com.missionse.logisticsexample.model.InventoryItem;
import com.missionse.logisticsexample.model.Order;
import com.missionse.logisticsexample.model.Site;

/**
 * Manages several views which either prompt the user to select from a list for details, or display details about a
 * given Site.
 */
public class SiteDetailFragment extends DatabaseEntryDetailFragment {

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
			parentContent += "ID: " + parentSite.getId() + "\n";
			parentContent += "Name: " + parentSite.getName() + "\n";
		} else {
			parentContent += "ID: N/A\n";
			parentContent += "Name: N/A\n";
		}
		setParentContent(parentContent.trim());

		String inventoryContent = "";
		for (InventoryItem item : supplies) {
			inventoryContent += " (" + item.getNameId() + "):\n";
			inventoryContent += "\tCurrent/Max: " + item.getQuantity() + "/" + item.getMaximum() + "\n";
		}
		setInventoryContent(inventoryContent.trim());

		String orderContent = "";
		for (Order order : orders) {
			orderContent += "ID: " + order.getId() + " (" + order.getTimeStamp() + "):\n";
			orderContent += "\tStatus: " + order.getStatusId() + "\n";
			orderContent += "\tSeverity: " + order.getSeverityId() + "\n";
		}
		setOrdersContent(orderContent.trim());

		showContentViews();
	}

}
