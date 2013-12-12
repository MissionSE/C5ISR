package com.missionse.logisticsexample.model.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * Mapping a {@link com.missionse.logisticsexample.model.Site} to a inventory 
 * {@link com.missionse.logisticsexample.model.InventoryItem}.
 */
public class SiteToInventoryItem {
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("site_id")
	@DatabaseField(columnName = "site_id")
	private int mSiteId;
	
	@Expose(serialize = true, deserialize = true)
	@SerializedName("item_id")
	@DatabaseField(columnName = "item_id")
	private int mItemId;
	
	/**
	 * Empty constructor.
	 */
	public SiteToInventoryItem() { }

	/**
	 * @return the mSiteId
	 */
	public int getSiteId() {
		return mSiteId;
	}

	/**
	 * @param siteId the mSiteId to set
	 */
	public void setSiteId(int siteId) {
		this.mSiteId = siteId;
	}

	/**
	 * @return the mItemId
	 */
	public int getItemId() {
		return mItemId;
	}

	/**
	 * @param itemId the mItemId to set
	 */
	public void setItemId(int itemId) {
		this.mItemId = itemId;
	}
}
