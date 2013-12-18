package com.missionse.logisticsexample.model.orm;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.missionse.logisticsexample.model.mappings.SiteToInventoryItem;
/**
 * Wrapper class to JSON.
 */
public class SiteToInventoryItemResponse extends Response {
	
	@SerializedName("sites_to_inventory_items")
	private List<SiteToInventoryItem> mSiteToInventoryItem;

	/**
	 * @return the mSiteToInventoryItem
	 */
	public List<SiteToInventoryItem> getSiteToInventoryItem() {
		return mSiteToInventoryItem;
	}

	/**
	 * @param siteToInventoryItem the mSiteToInventoryItem to set
	 */
	public void setSiteToInventoryItem(
			List<SiteToInventoryItem> siteToInventoryItem) {
		this.mSiteToInventoryItem = siteToInventoryItem;
	}
}
